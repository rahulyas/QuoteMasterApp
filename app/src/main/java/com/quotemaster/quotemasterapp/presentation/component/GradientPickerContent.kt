package com.quotemaster.quotemasterapp.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@Composable
fun GradientPickerContent(
    onGradientSelected: (Brush) -> Unit,
    modifier: Modifier = Modifier
) {
    // Define some preset gradients
    val gradients = listOf(
        Brush.verticalGradient(listOf(Color(0xFF6A11CB), Color(0xFF2575FC))), // Purple to blue
        Brush.verticalGradient(listOf(Color(0xFFF12711), Color(0xFFF5AF19))), // Red to yellow
        Brush.verticalGradient(listOf(Color(0xFF11998E), Color(0xFF38EF7D))), // Teal to green
        Brush.verticalGradient(listOf(Color(0xFFFC466B), Color(0xFF3F5EFB))), // Pink to purple
        Brush.verticalGradient(listOf(Color(0xFF00F260), Color(0xFF0575E6))), // Green to blue
        Brush.verticalGradient(listOf(Color(0xFFFDC830), Color(0xFFF37335))), // Yellow to orange
        Brush.verticalGradient(listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364))), // Dark gradient
        Brush.verticalGradient(listOf(Color(0xFFE55D87), Color(0xFF5FC3E4)))  // Pink to light blue
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp),
        modifier = modifier
    ) {
        items(gradients) { gradient ->
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(gradient, RoundedCornerShape(8.dp))
                    .clickable { onGradientSelected(gradient) }
            )
        }

        // Add a random gradient option
        item {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        Color.White.copy(alpha = 0.1f),
                        RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        val randomGradient = createRandomGradient()
                        onGradientSelected(randomGradient)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Random",
                    color = Color.White,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

fun createRandomGradient(): Brush {
    val colors = listOf(
        Color(
            red = Random.nextFloat(),
            green = Random.nextFloat(),
            blue = Random.nextFloat()
        ),
        Color(
            red = Random.nextFloat(),
            green = Random.nextFloat(),
            blue = Random.nextFloat()
        )
    )

    return if (Random.nextBoolean()) {
        Brush.verticalGradient(colors)
    } else {
        Brush.horizontalGradient(colors)
    }
}