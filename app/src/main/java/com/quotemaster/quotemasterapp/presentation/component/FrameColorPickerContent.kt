package com.quotemaster.quotemasterapp.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment


@Composable
fun FrameColorPickerContent(
    onColorSelected: (Color) -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Header with Cancel button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Frame Color",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(16.dp)
            )

            IconButton(onClick = onCancelClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel",
                    tint = Color.White
                )
            }
        }

        // Color palette
        val colors = listOf(
            Color.White, Color.Black, Color.Red, Color.Green,
            Color.Blue, Color.Yellow, Color.Cyan, Color.Magenta,
            Color(0xFFFFD700), Color(0xFF9C27B0), Color(0xFF2196F3),
            Color(0xFF4CAF50), Color(0xFFFF9800), Color(0xFF795548),
            Color(0xFF607D8B), Color(0xFFE91E63), Color(0xFF3F51B5),
            Color(0xFF009688), Color(0xFF8BC34A), Color(0xFFFFC107)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(colors) { color ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color, RoundedCornerShape(8.dp))
                        .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                        .clickable { onColorSelected(color) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
