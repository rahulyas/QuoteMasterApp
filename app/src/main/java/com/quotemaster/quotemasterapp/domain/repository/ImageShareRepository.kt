package com.quotemaster.quotemasterapp.domain.repository

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.ui.text.TextStyle

interface ImageShareRepository {
    suspend fun saveImageToGallery(bitmap: Bitmap, filename: String): Result<String>
    suspend fun shareImage(bitmap: Bitmap, context: Context): Result<Unit>
    suspend fun createQuoteBitmap(
        imageUrl: String,
        quoteText: String,
        author: String?,
        context: Context,
        textStyle: TextStyle
    ): Result<Bitmap>
    suspend fun setWallpaper(context: Context, imageUrl: String): Boolean

}