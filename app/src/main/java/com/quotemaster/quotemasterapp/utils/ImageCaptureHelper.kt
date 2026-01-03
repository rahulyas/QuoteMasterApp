package com.quotemaster.quotemasterapp.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.drawToBitmap
import com.quotemaster.quotemasterapp.presentation.quoteEditor.QuoteEditorViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class ImageCaptureHelper(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
    private val viewModel: QuoteEditorViewModel
) {
    fun captureAndSave(view: View?, onSuccess: () -> Unit = {}) {
        view ?: return

        coroutineScope.launch {
            try {
                val bitmap = withContext(Dispatchers.Main) {
                    // Wait for next frame
                    view.awaitNextFrame()
                    view.captureToBitmap()
                }

                saveToGallery(bitmap, onSuccess)
            } catch (e: Exception) {
                showError("Failed to capture: ${e.message}")
            }
        }
    }

    private suspend fun saveToGallery(bitmap: Bitmap, onSuccess: () -> Unit) {
        val filename = "quote_${System.currentTimeMillis()}.jpg"
        viewModel.saveImageToGallery(bitmap, filename)
            .onSuccess {
                showSuccess("Saved to gallery")
                onSuccess()
            }
            .onFailure { e ->
                showError("Failed to save: ${e.message}")
            }
    }

    private fun showSuccess(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

suspend fun View.awaitNextFrame() {
    suspendCancellableCoroutine { continuation ->
        post {
            continuation.resume(Unit)
        }
    }
}

// Extension function for view capture
fun View.captureToBitmap(): Bitmap {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        captureWithPixelCopy()
    } else {
        drawToBitmap(Bitmap.Config.ARGB_8888)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun View.captureWithPixelCopy(): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val location = IntArray(2)
    getLocationInWindow(location)

    PixelCopy.request(
        (context as Activity).window,
        Rect(location[0], location[1], location[0] + width, location[1] + height),
        bitmap,
        { result ->
            if (result != PixelCopy.SUCCESS) {
                throw RuntimeException("PixelCopy failed with result $result")
            }
        },
        Handler(Looper.getMainLooper())
    )

    return bitmap
}