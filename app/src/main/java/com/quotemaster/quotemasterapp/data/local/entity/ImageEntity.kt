package com.quotemaster.quotemasterapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey
    val id: Int,
    val tags: String,
    val previewURL: String,
    val largeImageURL: String,
    val imageType: String, // photo, illustration, vector
    val category: String,
    val createdAt: Long = System.currentTimeMillis()
)
