package com.quotemaster.quotemasterapp.data.repository

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.quotemaster.quotemasterapp.domain.model.GalleryImage
import com.quotemaster.quotemasterapp.domain.repository.GalleryImageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GalleryImageRepositoryImpl @Inject constructor(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher,
    ) : GalleryImageRepository {

    override suspend fun getAllImages(): List<GalleryImage> = withContext(dispatcher) {
        val imageList = mutableListOf<GalleryImage>()
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        context.contentResolver.query(uri, projection, null, null, sortOrder)?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(uri, id)
                imageList.add(GalleryImage(contentUri))
            }
        }

        imageList
    }
}
