package com.quotemaster.quotemasterapp.presentation.quote

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quotemaster.quotemasterapp.domain.model.Quote
import com.quotemaster.quotemasterapp.domain.model.QuoteWithImageAndStyle
import com.quotemaster.quotemasterapp.domain.usecase.CreateQuoteBitmapUseCase
import com.quotemaster.quotemasterapp.domain.usecase.GetQuotesWithImagesUseCase
import com.quotemaster.quotemasterapp.domain.usecase.SaveImageToGalleryUseCase
import com.quotemaster.quotemasterapp.domain.usecase.ShareImageUseCase
import com.quotemaster.quotemasterapp.utils.Constants.INITIAL_PAGE_SIZE
import com.quotemaster.quotemasterapp.utils.Constants.TOTAL_DESIRED_ITEMS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val getQuotesWithImagesUseCase: GetQuotesWithImagesUseCase,
    private val saveImageToGalleryUseCase: SaveImageToGalleryUseCase,
    private val shareImageUseCase: ShareImageUseCase,
    private val createQuoteBitmapUseCase: CreateQuoteBitmapUseCase
) : ViewModel()
{

    private val _quotesWithImagesAndStyles = MutableStateFlow<List<QuoteWithImageAndStyle>>(emptyList())
    val quotesWithImagesAndStyles: StateFlow<List<QuoteWithImageAndStyle>> = _quotesWithImagesAndStyles.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _showMessage = MutableStateFlow<String?>(null)
    val showMessage: StateFlow<String?> = _showMessage.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    // Legacy properties for backward compatibility
    var quotesWithImagesAndStylesLegacy by mutableStateOf<List<QuoteWithImageAndStyle>>(emptyList())
        private set
    var isLoadingLegacy by mutableStateOf(false)
        private set
    var showMessageLegacy by mutableStateOf<String?>(null)
        private set
    var isProcessingLegacy by mutableStateOf(false)
        private set


    init {
        // Sync StateFlow with legacy properties
        viewModelScope.launch {
            _quotesWithImagesAndStyles.collect { quotesWithImagesAndStylesLegacy = it }
        }
        viewModelScope.launch {
            _isLoading.collect { isLoadingLegacy = it }
        }
        viewModelScope.launch {
            _showMessage.collect { showMessageLegacy = it }
        }
        viewModelScope.launch {
            _isProcessing.collect { isProcessingLegacy = it }
        }
    }

    /**
     * Fetches quotes with images and styles for the given category
     * First checks database, if not found or outdated, fetches from API
     */
    fun fetchQuotesWithImagesAndStyles(categoryName: String, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val result = getQuotesWithImagesUseCase(
                    categoryName,
                    forceRefresh,
                    INITIAL_PAGE_SIZE,
                    isInitialLoad = true
                )

                result.onSuccess { quotesWithImages ->
                    _quotesWithImagesAndStyles.value = quotesWithImages

                    // Load remaining items in background with delay to avoid rate limits
                    if (quotesWithImages.size < TOTAL_DESIRED_ITEMS) {
                        launch {
                            // Add delay before making another request
                            delay(2000) // 2 second delay
                            val remainingResult = getQuotesWithImagesUseCase(
                                categoryName,
                                true,
                                TOTAL_DESIRED_ITEMS - quotesWithImages.size,
                                isInitialLoad = false
                            )
                            remainingResult.onSuccess { moreQuotes ->
                                _quotesWithImagesAndStyles.value += moreQuotes
                            }.onFailure { exception ->
                                // Handle background loading failure silently or show subtle notification
                                Log.w("ViewModel", "Failed to load additional quotes: ${exception.localizedMessage}")
                            }
                        }
                    }
                }.onFailure { exception ->
                    when (exception) {
                        is retrofit2.HttpException -> {
                            when (exception.code()) {
                                429 -> _showMessage.value = "Too many requests. Please wait a moment and try again."
                                401 -> _showMessage.value = "Authentication failed. Please check your API keys."
                                403 -> _showMessage.value = "Access forbidden. Please check your API permissions."
                                else -> _showMessage.value = "Failed to load quotes: ${exception.message()}"
                            }
                        }
                        else -> _showMessage.value = "Failed to load quotes: ${exception.localizedMessage}"
                    }
                }

            } catch (e: Exception) {
                _showMessage.value = "An unexpected error occurred while loading quotes"
                Log.e("ViewModel", "Unexpected error in fetchQuotesWithImagesAndStyles", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Downloads the current quote as an image
     */
    fun downloadCurrentQuote(
        imageUrl: String,
        quote: Quote,
        context: Context,
        textStyle: TextStyle
    ) {
        viewModelScope.launch {
            _isProcessing.value = true
            try {
                val bitmap = createQuoteBitmapUseCase(
                    imageUrl,
                    quote.content,
                    quote.author,
                    context,
                    textStyle
                ).getOrThrow()

                val filename = "quote_${System.currentTimeMillis()}.jpg"
                saveImageToGalleryUseCase(bitmap, filename)
                    .onSuccess {
                        _showMessage.value = "Image saved to gallery"
                    }
                    .onFailure {
                        _showMessage.value = "Failed to save image"
                    }
            } catch (e: Exception) {
                Log.e("QuoteViewModel", "Download Error: ${e.message}")
                _showMessage.value = "Failed to create image"
            } finally {
                _isProcessing.value = false
            }
        }
    }

    /**
     * Shares the current quote as an image
     */
    fun shareCurrentQuote(
        imageUrl: String,
        quote: Quote,
        context: Context,
        textStyle: TextStyle
    ) {
        viewModelScope.launch {
            _isProcessing.value = true
            try {
                val bitmap = createQuoteBitmapUseCase(
                    imageUrl,
                    quote.content,
                    quote.author,
                    context,
                    textStyle
                ).getOrThrow()

                shareImageUseCase(bitmap, context)
                    .onFailure {
                        _showMessage.value = "Failed to share image"
                    }
            } catch (e: Exception) {
                Log.e("QuoteViewModel", "Share Error: ${e.message}")
                _showMessage.value = "Failed to create image"
            } finally {
                _isProcessing.value = false
            }
        }
    }

    /**
     * Clears the current message
     */
    fun clearMessage() {
        _showMessage.value = null
    }

}
