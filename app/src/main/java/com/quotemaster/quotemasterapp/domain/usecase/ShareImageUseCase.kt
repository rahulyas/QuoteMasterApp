package com.quotemaster.quotemasterapp.domain.usecase

import android.content.Context
import android.graphics.Bitmap
import com.quotemaster.quotemasterapp.domain.repository.ImageShareRepository
import javax.inject.Inject

class ShareImageUseCase @Inject constructor(
    private val repository: ImageShareRepository
) {
    suspend operator fun invoke(bitmap: Bitmap, context: Context): Result<Unit> {
        return repository.shareImage(bitmap, context)
    }
}