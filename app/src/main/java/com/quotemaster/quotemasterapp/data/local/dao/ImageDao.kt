package com.quotemaster.quotemasterapp.data.local.dao

import androidx.room.*
import com.quotemaster.quotemasterapp.data.local.entity.ImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    @Query("SELECT * FROM images WHERE category = :category")
    suspend fun getImagesByCategory(category: String): List<ImageEntity>

    @Query("SELECT * FROM images WHERE category = :category")
    fun getImagesByCategoryFlow(category: String): Flow<List<ImageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(images: List<ImageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: ImageEntity)

    @Query("DELETE FROM images WHERE category = :category")
    suspend fun deleteImagesByCategory(category: String)

    @Query("SELECT COUNT(*) FROM images WHERE category = :category")
    suspend fun getImageCountByCategory(category: String): Int

    @Query("SELECT * FROM images WHERE id = :imageId LIMIT 1")
    suspend fun getImageById(imageId: Int): ImageEntity?

    @Query("SELECT * FROM images")
    suspend fun getAllImages(): List<ImageEntity>

}
