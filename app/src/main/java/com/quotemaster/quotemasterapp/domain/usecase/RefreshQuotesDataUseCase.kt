package com.quotemaster.quotemasterapp.domain.usecase

import com.quotemaster.quotemasterapp.domain.model.QuoteWithImageAndStyle
import com.quotemaster.quotemasterapp.domain.repository.QuoteWithImageRepository
import javax.inject.Inject

class RefreshQuotesDataUseCase @Inject constructor(
    private val repository: QuoteWithImageRepository
) {
    suspend operator fun invoke(category: String,perPage: Int): Result<List<QuoteWithImageAndStyle>> {
        return repository.refreshData(category,perPage)
    }
}