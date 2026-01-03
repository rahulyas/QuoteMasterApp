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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.items

@Composable
fun ShadowPickerContent(
    currentRadius: Float,
    currentColor: Color,
    onShadowChanged: (Float, Color) -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF2C3E50))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Text Shadow",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            IconButton(onClick = onCancelClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel",
                    tint = Color.White
                )
            }
        }

        var radiusValue by remember { mutableFloatStateOf(currentRadius) }

        Text(
            text = "Shadow Radius",
            color = Color.White,
            fontSize = 14.sp
        )

        Slider(
            value = radiusValue,
            onValueChange = {
                radiusValue = it
                onShadowChanged(it, currentColor)
            },
            valueRange = 0f..10f,
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.White.copy(alpha = 0.3f)
            )
        )

        Text(
            text = "Radius: ${radiusValue.toInt()}",
            color = Color.White,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Shadow Color",
            color = Color.White,
            fontSize = 14.sp
        )

        val shadowColors = listOf(
            Color.Black, Color.White, Color.Gray, Color.Red,
            Color.Blue, Color.Green, Color.Yellow, Color.Magenta
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(shadowColors) { color ->
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(color, RoundedCornerShape(4.dp))
                        .border(
                            2.dp,
                            if (color == currentColor) Color.White else Color.Transparent,
                            RoundedCornerShape(4.dp)
                        )
                        .clickable { onShadowChanged(radiusValue, color) }
                )
            }
        }
    }
}