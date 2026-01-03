package com.quotemaster.quotemasterapp.domain.usecase

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.ui.text.TextStyle
import com.quotemaster.quotemasterapp.domain.repository.ImageShareRepository
import javax.inject.Inject

class CreateQuoteBitmapUseCase @Inject constructor(
    private val repository: ImageShareRepository
) {
    suspend operator fun invoke(
        imageUrl: String,
        quoteText: String,
        author: String?,
        context: Context,
        textStyle: TextStyle
    ): Result<Bitmap> {
        return repository.createQuoteBitmap(imageUrl, quoteText, author, context,textStyle)
    }
}