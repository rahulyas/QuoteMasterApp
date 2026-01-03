package com.quotemaster.quotemasterapp.domain.usecase

import com.quotemaster.quotemasterapp.domain.repository.QuoteWithImageRepository
import javax.inject.Inject

class SearchImagesUseCase @Inject constructor(private val repository: QuoteWithImageRepository) {
    suspend operator fun invoke(query: String,perPage: Int) = repository.searchImages(query,perPage)
}