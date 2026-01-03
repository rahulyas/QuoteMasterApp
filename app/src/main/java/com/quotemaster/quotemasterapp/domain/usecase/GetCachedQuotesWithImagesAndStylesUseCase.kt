package com.quotemaster.quotemasterapp.domain.usecase

import com.quotemaster.quotemasterapp.domain.repository.QuoteWithImageRepository
import javax.inject.Inject

class GetCachedQuotesWithImagesAndStylesUseCase@Inject constructor(
    private val quoteWithImageRepository: QuoteWithImageRepository
) {
    suspend operator fun invoke(category: String) = quoteWithImageRepository.getCachedQuotesWithImagesAndStyles(category)
}