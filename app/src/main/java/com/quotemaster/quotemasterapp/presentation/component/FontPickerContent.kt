package com.quotemaster.quotemasterapp.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
fun FontPickerContent(
    currentWeight: FontWeight,
    onFontSelected: (FontWeight) -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fontWeights = listOf(
        FontWeight.Light to "Light",
        FontWeight.Normal to "Normal",
        FontWeight.Medium to "Medium",
        FontWeight.SemiBold to "SemiBold",
        FontWeight.Bold to "Bold",
        FontWeight.ExtraBold to "ExtraBold"
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
                text = "Font Weight",
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

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(fontWeights) { (weight, name) ->
                Card(
                    modifier = Modifier
                        .clickable { onFontSelected(weight) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (currentWeight == weight) {
                            Color.White.copy(alpha = 0.3f)
                        } else {
                            Color.Transparent
                        }
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (currentWeight == weight) Color.White else Color.White.copy(alpha = 0.3f)
                    )
                ) {
                    Text(
                        text = "Abc",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = weight,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}