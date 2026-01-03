package com.quotemaster.quotemasterapp.domain.repository

import com.quotemaster.quotemasterapp.data.local.entity.ImageEntity
import com.quotemaster.quotemasterapp.data.local.entity.QuoteEntity
import com.quotemaster.quotemasterapp.domain.model.PixabayImage
import com.quotemaster.quotemasterapp.domain.model.Quote
import com.quotemaster.quotemasterapp.domain.model.QuoteWithImageAndStyle

interface QuoteWithImageRepository {

    suspend fun getQuotesWithImagesAndStyles(
        category: String,
        forceRefresh: Boolean = false,
        perPage: Int,
        firstRequest: Boolean = true
    ): Result<List<QuoteWithImageAndStyle>>

    suspend fun getCachedQuotesWithImagesAndStyles(category: String): List<QuoteWithImageAndStyle>

    suspend fun clearCachedData(category: String)

    suspend fun getAllImages(): List<ImageEntity>

    suspend fun getAllQuotes(): List<QuoteEntity>

    suspend fun refreshData(category: String,perPage: Int): Result<List<QuoteWithImageAndStyle>>

    suspend fun searchImages(query: String,perPage: Int): List<PixabayImage>

}