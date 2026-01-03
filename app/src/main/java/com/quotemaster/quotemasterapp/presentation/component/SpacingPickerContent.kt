package com.quotemaster.quotemasterapp.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SpacingPickerContent(
    currentLetterSpacing: TextUnit,
    currentLineHeight: TextUnit,
    onLetterSpacingChanged: (TextUnit) -> Unit,
    onLineHeightChanged: (TextUnit) -> Unit,
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
                text = "Text Spacing",
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

        var letterSpacingValue by remember { mutableFloatStateOf(currentLetterSpacing.value) }
        var lineHeightValue by remember { mutableFloatStateOf(currentLineHeight.value) }

        Text(
            text = "Letter Spacing",
            color = Color.White,
            fontSize = 14.sp
        )

        Slider(
            value = letterSpacingValue,
            onValueChange = {
                letterSpacingValue = it
                onLetterSpacingChanged(it.sp)
            },
            valueRange = -2f..8f,
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.White.copy(alpha = 0.3f)
            )
        )

        Text(
            text = "Letter Spacing: ${letterSpacingValue.toInt()}sp",
            color = Color.White,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Line Height",
            color = Color.White,
            fontSize = 14.sp
        )

        Slider(
            value = lineHeightValue,
            onValueChange = {
                lineHeightValue = it
                onLineHeightChanged(it.sp)
            },
            valueRange = 12f..60f,
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.White.copy(alpha = 0.3f)
            )
        )

        Text(
            text = "Line Height: ${lineHeightValue.toInt()}sp",
            color = Color.White,
            fontSize = 12.sp
        )
    }
}