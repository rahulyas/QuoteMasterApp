package com.quotemaster.quotemasterapp.data.repository

import android.util.Log
import androidx.compose.ui.text.TextStyle
import com.quotemaster.quotemasterapp.data.local.dao.ImageDao
import com.quotemaster.quotemasterapp.data.local.dao.QuoteDao
import com.quotemaster.quotemasterapp.data.local.entity.ImageEntity
import com.quotemaster.quotemasterapp.data.local.entity.QuoteEntity
import com.quotemaster.quotemasterapp.data.local.entity.toTextStyle
import com.quotemaster.quotemasterapp.data.model.Message
import com.quotemaster.quotemasterapp.data.model.OpenAIRequest
import com.quotemaster.quotemasterapp.data.remote.OpenAIService
import com.quotemaster.quotemasterapp.data.remote.PixabayApi
import com.quotemaster.quotemasterapp.domain.model.PixabayImage
import com.quotemaster.quotemasterapp.domain.model.Quote
import com.quotemaster.quotemasterapp.domain.model.QuoteWithImageAndStyle
import com.quotemaster.quotemasterapp.domain.repository.QuoteWithImageRepository
import com.quotemaster.quotemasterapp.utils.Author
import com.quotemaster.quotemasterapp.utils.CommonNamePrefix
import com.quotemaster.quotemasterapp.utils.Constants.LIST_SIZE
import com.quotemaster.quotemasterapp.utils.Constants.QUOTE_WITH_IMAGE_REPOSITORY
import com.quotemaster.quotemasterapp.utils.Constants.YOUR_SECRET_KEY
import com.quotemaster.quotemasterapp.utils.randomTextStyle
import com.quotemaster.quotemasterapp.utils.toImageEntity
import com.quotemaster.quotemasterapp.utils.toPixabayImage
import com.quotemaster.quotemasterapp.utils.toQuote
import com.quotemaster.quotemasterapp.utils.toQuoteEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class QuoteWithImageRepositoryImpl @Inject constructor(
    private val openAIService: OpenAIService,
    private val pixabayApi: PixabayApi,
    private val quoteDao: QuoteDao,
    private val imageDao: ImageDao,
    private val dispatcher: CoroutineDispatcher
): QuoteWithImageRepository {
    private var lastOpenAIRequest = 0L
    private var lastPixabayRequest = 0L
    private val minRequestInterval = 1000L

    override suspend fun getQuotesWithImagesAndStyles(
        category: String,
        forceRefresh: Boolean,
        perPage: Int,
        firstRequest: Boolean
    ): Result<List<QuoteWithImageAndStyle>> {
        return try {
            val shouldFetchFromApi = forceRefresh ||  (shouldUpdateData(category))

            if (shouldFetchFromApi) {
                fetchFromApiAndCache(category,perPage,firstRequest)
            } else {
                Log.e(QUOTE_WITH_IMAGE_REPOSITORY,"Fetching from cache")
                getFromCache(category,perPage,firstRequest)
            }
        } catch (e: retrofit2.HttpException) {
            when (e.code()) {
                429 -> {
                    Log.w(QUOTE_WITH_IMAGE_REPOSITORY, "Rate limit hit, falling back to cache")
                    val cacheResult = getFromCache(category, perPage, firstRequest)
                    if (cacheResult.isSuccess && cacheResult.getOrNull()?.isNotEmpty() == true) {
                        cacheResult
                    } else {
                        delay(1000)
                        fetchFromApiAndCache(category, perPage, firstRequest)
                    }
                }
                else -> {
                    Log.e(QUOTE_WITH_IMAGE_REPOSITORY, "HTTP Error ${e.code()}: ${e.message()}")
                    getFromCache(category, perPage, firstRequest)
                }
            }
        } catch (e: Exception) {
            Log.e(QUOTE_WITH_IMAGE_REPOSITORY, "Unexpected error: ${e.message}", e)
            getFromCache(category, perPage, firstRequest)
        }
    }

    override suspend fun getCachedQuotesWithImagesAndStyles(category: String): List<QuoteWithImageAndStyle> {
        val images = imageDao.getImagesByCategory(category)
        val quotes = quoteDao.getQuotesByCategory(category)

        return if (images.isNotEmpty() && quotes.isNotEmpty()) {
            images.zip(quotes) { imageEntity, quoteEntity ->
                QuoteWithImageAndStyle(
                    quote = quoteEntity.toQuote(),
                    image = imageEntity.toPixabayImage(),
                    textStyle = quoteEntity.textStyle.toTextStyle()
                )
            }
        } else {
            emptyList()
        }
    }

    override suspend fun clearCachedData(category: String) {
        imageDao.deleteImagesByCategory(category)
        quoteDao.deleteQuotesByCategory(category)
    }

    override suspend fun getAllImages(): List<ImageEntity> {
        return imageDao.getAllImages()
    }

    override suspend fun getAllQuotes(): List<QuoteEntity> {
        return quoteDao.getAllQuotes()
    }

    override suspend fun refreshData(
        category: String,
        perPage: Int
    ): Result<List<QuoteWithImageAndStyle>> {
        return getQuotesWithImagesAndStyles(category, forceRefresh = true,perPage)
    }

    override suspend fun searchImages(query: String, perPage: Int): List<PixabayImage> {
        return try {
            // Check if we have cached images for this category
            val cachedImages = imageDao.getImagesByCategory(query)

            if (cachedImages.isNotEmpty()) {
                // Return cached images
                cachedImages.map { it.toPixabayImage() }
            } else {
                // Fetch from API and cache
                val apiImages = pixabayApi.searchImages(query = query, perPage = perPage).hits
                val imageEntities = apiImages.map { it.toImageEntity(query) }
                imageDao.insertImages(imageEntities)
                apiImages
            }
        } catch (e: Exception) {
            // Return cached images if API fails
            imageDao.getImagesByCategory(query).map { it.toPixabayImage() }
        }
    }

    private suspend fun shouldUpdateData(category: String): Boolean {
        val oneDayAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)

        val mostRecentQuote = quoteDao.getMostRecentQuote(category)
        val isDataOld = mostRecentQuote == null || mostRecentQuote.lastUpdated < oneDayAgo

        val quoteCount = quoteDao.getQuoteCountByCategory(category)
        val isDataInsufficient = quoteCount < LIST_SIZE

        return isDataOld || isDataInsufficient
    }

    private suspend fun fetchFromApiAndCache(
        category: String,
        perPage: Int,
        firstRequest: Boolean
    ): Result<List<QuoteWithImageAndStyle>> {
        return try {
            coroutineScope {
                val currentTime = System.currentTimeMillis()
                val imageDeferred = async(dispatcher) {
                    val timeSinceLastPixabay = currentTime - lastPixabayRequest
                    if (timeSinceLastPixabay < minRequestInterval) {
                        delay(minRequestInterval - timeSinceLastPixabay)
                    }
                    lastPixabayRequest = System.currentTimeMillis()

                    retryWithBackoff {
                        pixabayApi.searchImages(query = category, perPage = perPage).hits
                    }
                }

                val quoteDeferred = async(dispatcher) {
                    val timeSinceLastOpenAI = currentTime - lastOpenAIRequest
                    if (timeSinceLastOpenAI < minRequestInterval) {
                        delay(minRequestInterval - timeSinceLastOpenAI)
                    }
                    lastOpenAIRequest = System.currentTimeMillis()

                    val action = if (firstRequest) "Provide me with" else "Provide me next"
                    val prompt = if (queryLooksLikeName(category)) {
                        "$action $perPage quotes by $category with the author's name."
                    } else {
                        "$action $perPage quotes about $category, including the author's name."
                    }

                    retryWithBackoff {
                        val response = openAIService.getQuote(
                            token = "Bearer $YOUR_SECRET_KEY",
                            request = OpenAIRequest(
                                messages = listOf(
                                    Message("system", "You are a wise quote generator."),
                                    Message("user", prompt)
                                )
                            )
                        )
                        extractQuotesFromText(response.choices.first().message.content.trim())
                    }
                }

                val images = imageDeferred.await()
                val quotes = quoteDeferred.await()

                if (images.isEmpty() && quotes.isEmpty()) {
                    return@coroutineScope Result.failure(Exception("Failed to fetch both images and quotes"))
                }

                val textStyles = List(minOf(images.size, quotes.size)) { randomTextStyle() }

                // Save to cache in background
                launch {
                    clearCachedData(category)
                    saveToCache(images, quotes, textStyles, category)
                }

                val result = createQuoteWithImageAndStyleList(images, quotes, textStyles)
                Result.success(result)
            }
        } catch (e: Exception) {
            Log.e(QUOTE_WITH_IMAGE_REPOSITORY, "Error in parallel fetch: ${e.message}", e)
            Result.failure(e)
        }
    }

    private suspend fun <T> retryWithBackoff(
        maxRetries: Int = 3,
        initialDelayMs: Long = 1000,
        maxDelayMs: Long = 10000,
        backoffMultiplier: Double = 2.0,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelayMs
        repeat(maxRetries) { attempt ->
            try {
                return block()
            } catch (e: retrofit2.HttpException) {
                if (e.code() == 429 && attempt < maxRetries - 1) {
                    Log.d("Retry", "Rate limited, retrying in ${currentDelay}ms (attempt ${attempt + 1})")
                    delay(currentDelay)
                    currentDelay = minOf(maxDelayMs, (currentDelay * backoffMultiplier).toLong())
                } else {
                    throw e
                }
            }
        }
        return block() // Final attempt
    }

    private suspend fun getFromCache(
        category: String,
        perPage: Int,
        firstRequest: Boolean
    ): Result<List<QuoteWithImageAndStyle>> {
        return try {
            val cachedData = getCachedQuotesWithImagesAndStyles(category)
            if (cachedData.isNotEmpty()) {
                Result.success(cachedData)
            } else {
                // If no cached data, fetch from API with backoff
                fetchFromApiAndCache(category, perPage, firstRequest)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    private suspend fun saveToCache(
        images: List<PixabayImage>,
        quotes: List<Quote>,
        textStyles: List<TextStyle>,
        category: String
    ) {
        withContext(dispatcher){
            val imageEntities = images.map { it.toImageEntity(category) }
            val quoteEntities = quotes.mapIndexed { index, quote ->
                quote.toQuoteEntity(
                    imageId = images[index].id,
                    category = category,
                    textStyle = textStyles[index]
                )
            }

            imageDao.insertImages(imageEntities)
            quoteDao.insertQuotes(quoteEntities)
        }
    }

    private fun createQuoteWithImageAndStyleList(
        images: List<PixabayImage>,
        quotes: List<Quote>,
        textStyles: List<TextStyle>
    ): List<QuoteWithImageAndStyle> {
        val count = minOf(images.size, quotes.size, textStyles.size)
        return List(count) { index ->
            QuoteWithImageAndStyle(
                quote = quotes[index],
                image = images[index],
                textStyle = textStyles[index]
            )
        }
    }


    private fun extractQuotesFromText(text: String): List<Quote> {
        val regex = Regex("""\d+\.\s*"(.*?)"(?:\s*[—-]\s*(.*))?""")
        return regex.findAll(text).map { match ->
            val content = match.groupValues[1].trim()
            val author = match.groupValues.getOrNull(2)?.takeIf { it.isNotBlank() } ?: "UNKNOWN"
            Quote(content, author)
        }.toList()
    }

    private fun queryLooksLikeName(query: String): Boolean {
        val trimmed = query.trim()
        if (CommonNamePrefix.matches(trimmed) || Author.existsWithDisplayName(trimmed)) return true
        if (trimmed.contains(" ") && trimmed.split(" ").all { it.firstOrNull()?.isUpperCase() == true }) {
            return true
        }
        if (trimmed.length > 3 && trimmed[0].isUpperCase() && trimmed.drop(1).all { it.isLowerCase() }) {
            return true
        }
        return false
    }

}