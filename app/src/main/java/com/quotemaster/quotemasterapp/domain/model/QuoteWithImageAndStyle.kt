package com.quotemaster.quotemasterapp.domain.model

import androidx.compose.ui.text.TextStyle

data class QuoteWithImageAndStyle(
    val quote: Quote,
    val image: PixabayImage,
    val textStyle: TextStyle
)