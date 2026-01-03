package com.quotemaster.quotemasterapp.data.model

import com.quotemaster.quotemasterapp.domain.model.PixabayImage

data class PixabayResponse(
    val hits: List<PixabayImage>
)