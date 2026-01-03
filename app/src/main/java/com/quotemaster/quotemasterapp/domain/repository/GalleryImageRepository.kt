package com.quotemaster.quotemasterapp.domain.repository

import com.quotemaster.quotemasterapp.domain.model.GalleryImage


interface GalleryImageRepository {
    suspend fun getAllImages(): List<GalleryImage>
}
