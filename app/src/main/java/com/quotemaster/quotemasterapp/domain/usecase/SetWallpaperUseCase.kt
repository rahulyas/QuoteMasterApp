package com.quotemaster.quotemasterapp.domain.usecase

import android.content.Context
import com.quotemaster.quotemasterapp.domain.repository.ImageShareRepository
import javax.inject.Inject

class SetWallpaperUseCase @Inject constructor(private val repository: ImageShareRepository) {
    suspend operator fun invoke(context: Context, imageUrl: String): Boolean {
        return repository.setWallpaper(context, imageUrl)
    }
}