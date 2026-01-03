package com.quotemaster.quotemasterapp.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatAlignLeft
import androidx.compose.material.icons.automirrored.filled.FormatAlignRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatAlignJustify
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AlignPickerContent(
    currentAlign: TextAlign,
    onAlignSelected: (TextAlign) -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val alignments = listOf(
        TextAlign.Left to Icons.AutoMirrored.Filled.FormatAlignLeft,
        TextAlign.Center to Icons.Default.FormatAlignCenter,
        TextAlign.Right to Icons.AutoMirrored.Filled.FormatAlignRight,
        TextAlign.Justify to Icons.Default.FormatAlignJustify
    )

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
                text = "Text Alignment",
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            alignments.forEach { (align, icon) ->
                IconButton(
                    onClick = { onAlignSelected(align) },
                    modifier = Modifier
                        .background(
                            if (currentAlign == align) Color.White.copy(alpha = 0.3f)
                            else Color.Transparent,
                            RoundedCornerShape(8.dp)
                        )
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
