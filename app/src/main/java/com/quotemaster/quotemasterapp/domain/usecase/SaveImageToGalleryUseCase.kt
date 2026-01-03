package com.quotemaster.quotemasterapp.domain.usecase

import android.graphics.Bitmap
import com.quotemaster.quotemasterapp.domain.repository.ImageShareRepository
import javax.inject.Inject

class SaveImageToGalleryUseCase @Inject constructor(
    private val repository: ImageShareRepository
) {
    suspend operator fun invoke(bitmap: Bitmap, filename: String): Result<String> {
        return repository.saveImageToGallery(bitmap, filename)
    }
}