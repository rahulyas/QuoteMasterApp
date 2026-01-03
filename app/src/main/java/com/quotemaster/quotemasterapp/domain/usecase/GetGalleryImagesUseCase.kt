package com.quotemaster.quotemasterapp.domain.usecase

import com.quotemaster.quotemasterapp.domain.model.GalleryImage
import com.quotemaster.quotemasterapp.domain.repository.GalleryImageRepository
import javax.inject.Inject

class GetGalleryImagesUseCase @Inject constructor(
    private val repository: GalleryImageRepository
) {
    suspend operator fun invoke(): List<GalleryImage> = repository.getAllImages()
}
