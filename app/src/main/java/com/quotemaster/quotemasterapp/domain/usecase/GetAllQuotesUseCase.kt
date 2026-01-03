package com.quotemaster.quotemasterapp.domain.usecase

import com.quotemaster.quotemasterapp.data.local.entity.QuoteEntity
import com.quotemaster.quotemasterapp.domain.repository.QuoteWithImageRepository
import javax.inject.Inject

class GetAllQuotesUseCase @Inject constructor(
    private val repository: QuoteWithImageRepository
) {
    suspend operator fun invoke(): List<QuoteEntity> {
        return repository.getAllQuotes()
    }
}
