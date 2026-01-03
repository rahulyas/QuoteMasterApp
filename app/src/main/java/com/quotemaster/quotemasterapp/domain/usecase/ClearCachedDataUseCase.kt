package com.quotemaster.quotemasterapp.domain.usecase

import com.quotemaster.quotemasterapp.domain.repository.QuoteWithImageRepository
import javax.inject.Inject

class ClearCachedDataUseCase @Inject constructor(
    private val repository: QuoteWithImageRepository
) {
    suspend operator fun invoke(category: String) {
        repository.clearCachedData(category)
    }
}