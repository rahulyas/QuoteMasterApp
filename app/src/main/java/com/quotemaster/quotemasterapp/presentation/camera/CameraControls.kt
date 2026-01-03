package com.quotemaster.quotemasterapp.presentation.camera

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CameraControls(
    onCaptureClick: () -> Unit,
    onSwitchCamera: () -> Unit,
    onCloseClick: () -> Unit,
    onToggleFlash: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = onToggleFlash) {
                Icon(Icons.Default.FlashOn, contentDescription = "Toggle Flash", tint = Color.Yellow)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = onCloseClick) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }

            IconButton(onClick = onCaptureClick) {
                Icon(Icons.Default.CameraAlt, contentDescription = "Capture", tint = Color.White)
            }

            IconButton(onClick = onSwitchCamera) {
                Icon(Icons.Default.Cameraswitch, contentDescription = "Switch Camera", tint = Color.White)
            }
        }
    }
}
