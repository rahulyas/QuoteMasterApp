package com.quotemaster.quotemasterapp.domain.usecase

import com.quotemaster.quotemasterapp.data.local.entity.ImageEntity
import com.quotemaster.quotemasterapp.domain.repository.QuoteWithImageRepository
import javax.inject.Inject

class GetAllImagesUseCase @Inject constructor(
    private val repository: QuoteWithImageRepository
) {
    suspend operator fun invoke(): List<ImageEntity> {
        return repository.getAllImages()
    }
}
