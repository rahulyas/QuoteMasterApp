package com.quotemaster.quotemasterapp.domain.model

data class PixabayImage(
    val id: Int = 0,
    val tags: String = "",
    val previewURL: String = "",
    val largeImageURL: String = ""
)
