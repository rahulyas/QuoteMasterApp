package com.quotemaster.quotemasterapp.domain.usecase

import com.quotemaster.quotemasterapp.domain.model.QuoteWithImageAndStyle
import com.quotemaster.quotemasterapp.domain.repository.QuoteWithImageRepository
import javax.inject.Inject

class GetQuotesWithImagesUseCase @Inject constructor(
    private val repository: QuoteWithImageRepository
) {
    suspend operator fun invoke(
        category: String,
        forceRefresh: Boolean = false,
        perPage:Int,
        isInitialLoad: Boolean = true
    ): Result<List<QuoteWithImageAndStyle>> {
        return repository.getQuotesWithImagesAndStyles(category, forceRefresh,perPage,isInitialLoad)
    }
}
