package com.quotemaster.quotemasterapp.data.repository

import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import android.provider.MediaStore
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.compose.ui.text.TextStyle
import androidx.core.content.FileProvider
import coil.imageLoader
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.quotemaster.quotemasterapp.domain.repository.ImageShareRepository
import com.quotemaster.quotemasterapp.utils.getFontFamilyName
import com.quotemaster.quotemasterapp.utils.getTypefaceStyle
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageShareRepositoryImpl @Inject constructor(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher,
) : ImageShareRepository {

    override suspend fun saveImageToGallery(bitmap: Bitmap, filename: String): Result<String> {
        return try {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }

            val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            uri?.let { imageUri ->
                context.contentResolver.openOutputStream(imageUri)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
                Result.success("Image saved successfully")
            } ?: Result.failure(Exception("Failed to create image file"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun shareImage(bitmap: Bitmap, context: Context): Result<Unit> {
        return try {
            val cachePath = File(context.cacheDir, "images")
            cachePath.mkdirs()
            val file = File(cachePath, "quote_${System.currentTimeMillis()}.jpg")

            withContext(dispatcher) {
                FileOutputStream(file).use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
            }

            val fileUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/jpeg"
                putExtra(Intent.EXTRA_STREAM, fileUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(intent, "Share Quote"))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createQuoteBitmap(
        imageUrl: String,
        quoteText: String,
        author: String?,
        context: Context,
        textStyle: TextStyle
    ): Result<Bitmap> {
        return withContext(dispatcher) {
            try {
                // Load the background image
                val backgroundBitmap = loadBitmapFromUrl(imageUrl, context)

                // Create the final bitmap with quote overlay
                val finalBitmap = createBitmapWithQuoteOverlay(
                    backgroundBitmap,
                    quoteText,
                    author,
                    textStyle
                )

                Result.success(finalBitmap)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    private suspend fun loadBitmapFromUrl(url: String, context: Context): Bitmap {
        return withContext(dispatcher) {
            val request = ImageRequest.Builder(context)
                .data(url)
                .build()

            val drawable = context.imageLoader.execute(request).drawable
            val hardwareBitmap = (drawable as? BitmapDrawable)?.bitmap
                ?: throw IllegalStateException("Failed to load bitmap from URL")

            hardwareBitmap.copy(Bitmap.Config.ARGB_8888, true)
        }
    }

    private fun createBitmapWithQuoteOverlay(
        backgroundBitmap: Bitmap,
        quoteText: String,
        author: String?,
        textStyle: TextStyle
    ): Bitmap {
        val width = backgroundBitmap.width
        val height = backgroundBitmap.height + 100

        // Create a scaled background
        val scaledBackground = Bitmap.createScaledBitmap(backgroundBitmap, width, height, true)

        // Create the final bitmap
        val finalBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(finalBitmap)

        // Draw background
        canvas.drawBitmap(scaledBackground, 0f, 0f, null)

        // Draw dark overlay
        val overlayPaint = Paint().apply {
            color = Color.TRANSPARENT
            alpha = 77 // 30% opacity
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), overlayPaint)

        // Draw quote text
        drawQuoteText(canvas, quoteText, author, width, height, textStyle)

        // Draw bottom action bar
        //drawBottomActionBar(canvas, width, height)

        return finalBitmap
    }

    @Suppress("DEPRECATION")
    private fun drawQuoteText(
        canvas: Canvas,
        quoteText: String,
        author: String?,
        width: Int,
        height: Int,
        textStyle: TextStyle
    ) {
        val density = Resources.getSystem().displayMetrics.density
        val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity

        val bottomPaddingPx = 24 * density
        val startPaddingPx = 24 * density
        val endPaddingPx = 24 * density
        val availableWidth = width - startPaddingPx - endPaddingPx

        val textSizeInPx = textStyle.fontSize.value * scaledDensity

        val paint = TextPaint().apply {
            color = Color.WHITE
            isAntiAlias = true
            textAlign = Paint.Align.LEFT
            textSize = textSizeInPx
            setShadowLayer(5f, 2f, 2f, Color.BLACK)
            typeface = Typeface.create(
                getFontFamilyName(textStyle.fontFamily),
                getTypefaceStyle(textStyle.fontWeight)
            )
        }

        val completeText = if (!author.isNullOrBlank() && !author.contains("UNKNOWN", ignoreCase = true)) {
            "“$quoteText”\n— $author"
        } else {
            "“$quoteText”"
        }


        val staticLayout = StaticLayout(
            completeText, paint, availableWidth.toInt(),
            Layout.Alignment.ALIGN_CENTER,
            1.2f, 0f, false
        )

        val totalTextHeight = staticLayout.height
        val startY = (height - bottomPaddingPx - totalTextHeight)

        canvas.save()
        canvas.translate(startPaddingPx, startY)
        staticLayout.draw(canvas)
        canvas.restore()
    }


    override suspend fun setWallpaper(context: Context, imageUrl: String): Boolean {
        return withContext(dispatcher) {
            try {
                val bitmap = Glide.with(context)
                    .asBitmap()
                    .load(imageUrl)
                    .submit()
                    .get()

                if (bitmap == null) {
                    return@withContext false
                }
                WallpaperManager.getInstance(context).setBitmap(bitmap)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

}