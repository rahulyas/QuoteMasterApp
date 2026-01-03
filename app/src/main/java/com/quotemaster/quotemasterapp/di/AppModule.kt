package com.quotemaster.quotemasterapp.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.quotemaster.quotemasterapp.data.local.QuoteDatabase
import com.quotemaster.quotemasterapp.data.local.dao.ImageDao
import com.quotemaster.quotemasterapp.data.local.dao.QuoteDao
import com.quotemaster.quotemasterapp.data.network.NetworkMonitor
import com.quotemaster.quotemasterapp.data.remote.OpenAIService
import com.quotemaster.quotemasterapp.data.remote.PixabayApi
import com.quotemaster.quotemasterapp.data.repository.GalleryImageRepositoryImpl
import com.quotemaster.quotemasterapp.data.repository.ImageShareRepositoryImpl
import com.quotemaster.quotemasterapp.data.repository.QuoteWithImageRepositoryImpl
import com.quotemaster.quotemasterapp.domain.repository.GalleryImageRepository
import com.quotemaster.quotemasterapp.domain.repository.ImageShareRepository
import com.quotemaster.quotemasterapp.domain.repository.QuoteWithImageRepository
import com.quotemaster.quotemasterapp.utils.Constants.OPEN_AI_URL
import com.quotemaster.quotemasterapp.utils.Constants.PIXABAY_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val rateLimitInterceptor = Interceptor { chain ->
        val request = chain.request()
        var response = chain.proceed(request)
        var retryCount = 0
        val maxRetries = 3

        while (response.code == 429 && retryCount < maxRetries) {
            response.close() // Important: close the previous response
            val retryAfter = response.header("Retry-After")?.toLongOrNull() ?: 1
            val delayMs = minOf(retryAfter * 1000, (1000 * (1 shl retryCount)).toLong())

            try {
                Thread.sleep(delayMs)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                break
            }

            response = chain.proceed(request)
            retryCount++
        }

        response
    }

    val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(rateLimitInterceptor)
        .build()

    @Provides
    fun provideBaseUrl() =OPEN_AI_URL

    @Provides
    fun provideRetrofit(base_url: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    @Provides
    fun provideOpenAIService(retrofit: Retrofit): OpenAIService =
        retrofit.create(OpenAIService::class.java)


    @Provides
    fun providePixabayApi(): PixabayApi {
        return Retrofit.Builder()
            .baseUrl(PIXABAY_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PixabayApi::class.java)
    }

    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun imageShareRepository(@ApplicationContext context: Context,dispatcher: CoroutineDispatcher): ImageShareRepository {
        return ImageShareRepositoryImpl(context,dispatcher)
    }

    // Database
    @Provides
    @Singleton
    fun provideQuoteDatabase(@ApplicationContext context: Context): QuoteDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            QuoteDatabase::class.java,
            "quote_database"
        ).build()
    }

    @Provides
    fun provideImageDao(database: QuoteDatabase): ImageDao = database.imageDao()

    @Provides
    fun provideQuoteDao(database: QuoteDatabase): QuoteDao = database.quoteDao()

    @Provides
    fun quoteWithImageRepository(
        openAIService: OpenAIService,
        pixabayApi: PixabayApi,
        quoteDao: QuoteDao,
        imageDao: ImageDao,
        dispatcher: CoroutineDispatcher
    ): QuoteWithImageRepository {
        return QuoteWithImageRepositoryImpl(
            openAIService = openAIService,
            pixabayApi = pixabayApi,
            quoteDao = quoteDao,
            imageDao = imageDao,
            dispatcher = dispatcher
        )
    }

    @Provides
    fun provideLocalImageRepository(@ApplicationContext context: Context, dispatcher: CoroutineDispatcher): GalleryImageRepository {
        return GalleryImageRepositoryImpl(context, dispatcher)
    }

    @Provides
    fun provideNetworkMonitor(@ApplicationContext context: Context): NetworkMonitor {
        return NetworkMonitor(context)
    }

}
