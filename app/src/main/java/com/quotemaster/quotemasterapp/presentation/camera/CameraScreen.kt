package com.quotemaster.quotemasterapp.presentation.camera

import android.content.Context
import android.util.Log
import android.view.Surface
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.camera.core.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.io.File
import androidx.camera.core.*
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.quotemaster.quotemasterapp.utils.Constants.CAMERA_SCREEN

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    onImageCaptured: (String) -> Unit,
    onCameraClosed: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var camera by remember { mutableStateOf<Camera?>(null) }
    val zoomRatio by remember { mutableFloatStateOf(1.0f) }
    var flashEnabled by remember { mutableStateOf(false) }

    val previewView = remember { PreviewView(context).apply { scaleType = PreviewView.ScaleType.FILL_CENTER } }

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(cameraProviderFuture) {
        cameraProvider = cameraProviderFuture.get()
    }

    fun bindCamera() {
        val provider = cameraProvider ?: return
        provider.unbindAll()

        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        imageCapture = ImageCapture.Builder()
            .setFlashMode(if (flashEnabled) ImageCapture.FLASH_MODE_ON else ImageCapture.FLASH_MODE_OFF)
            .setTargetRotation(Surface.ROTATION_0)
            .build()

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()

        try {
            camera = provider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
            camera?.cameraControl?.setZoomRatio(zoomRatio)
        } catch (e: Exception) {
            Log.e(CAMERA_SCREEN, "Camera bind failed", e)
        }
    }

    LaunchedEffect(cameraProvider, lensFacing, flashEnabled) {
        if (cameraPermissionState.status.isGranted) {
            bindCamera()
        }
    }

    if (cameraPermissionState.status.isGranted) {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            // Add gesture modifier
            val gestureModifier = Modifier.pointerInput(camera) {
                detectTransformGestures { _, _, zoomChange, _ ->
                    val cameraInfo = camera?.cameraInfo ?: return@detectTransformGestures
                    val cameraControl = camera?.cameraControl ?: return@detectTransformGestures
                    val currentZoomRatio = cameraInfo.zoomState.value?.zoomRatio ?: 1f
                    val newZoomRatio = (currentZoomRatio * zoomChange)
                        .coerceIn(
                            cameraInfo.zoomState.value?.minZoomRatio ?: 1f,
                            cameraInfo.zoomState.value?.maxZoomRatio ?: 10f
                        )
                    cameraControl.setZoomRatio(newZoomRatio)
                }
            }

            AndroidView(
                factory = { previewView },
                modifier = gestureModifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CameraControls(
                    onCaptureClick = {
                        captureImage(
                            context = context,
                            imageCapture = imageCapture,
                            onImageCaptured = onImageCaptured
                        )
                    },
                    onSwitchCamera = {
                        lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK)
                            CameraSelector.LENS_FACING_FRONT
                        else
                            CameraSelector.LENS_FACING_BACK
                    },
                    onCloseClick = onCameraClosed,
                    modifier = modifier,
                    onToggleFlash = {
                        flashEnabled = !flashEnabled
                    }
                )
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Camera permission is required",
                    color = Color.White,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text("Grant Permission")
                }
            }
        }
    }
}

private fun captureImage(
    context: Context,
    imageCapture: ImageCapture?,
    onImageCaptured: (String) -> Unit
) {
    val outputFileOptions = ImageCapture.OutputFileOptions.Builder(
        File(context.filesDir, "captured_image_${System.currentTimeMillis()}.jpg")
    ).build()

    imageCapture?.takePicture(
        outputFileOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                output.savedUri?.let { uri ->
                    onImageCaptured(uri.toString())
                }
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e(CAMERA_SCREEN, "Image capture failed", exception)
            }
        }
    )
}

