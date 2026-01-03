package com.quotemaster.quotemasterapp.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.quotemaster.quotemasterapp.domain.model.EmojiItem
import kotlin.math.roundToInt

@Composable
fun InteractiveEmoji(
    emoji: EmojiItem,
    isSelected: Boolean,
    onPositionChange: (Float, Float) -> Unit,
    onScaleChange: (Float) -> Unit,
    onRotationChange: (Float) -> Unit,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
    containerSize: IntSize,
    modifier: Modifier = Modifier
) {
    // Calculate absolute position
    val absoluteX = (emoji.x * containerSize.width).roundToInt()
    val absoluteY = (emoji.y * containerSize.height).roundToInt()

    println("Emoji ${emoji.id}: relative(${emoji.x}, ${emoji.y}) -> absolute($absoluteX, $absoluteY) in container $containerSize")

    Box(
        modifier = modifier
            .offset { IntOffset(absoluteX, absoluteY) }
            .size(64.dp) // Fixed size for easier interaction
            .graphicsLayer {
                scaleX = emoji.scale
                scaleY = emoji.scale
                rotationZ = emoji.rotation
            }
            .zIndex(2f) // Higher than background
            .pointerInput(emoji.id) {
                detectDragGestures(
                    onDragStart = {
                        onSelect()
                        println("Drag started for emoji ${emoji.id}")
                    }
                ) { change, dragAmount ->
                    // Calculate new position
                    val newAbsoluteX = absoluteX + dragAmount.x.roundToInt()
                    val newAbsoluteY = absoluteY + dragAmount.y.roundToInt()

                    // Convert back to relative coordinates
                    val newRelativeX = newAbsoluteX.toFloat() / containerSize.width
                    val newRelativeY = newAbsoluteY.toFloat() / containerSize.height

                    // Apply bounds with some margin
                    val boundedX = newRelativeX.coerceIn(-0.1f, 1.1f)
                    val boundedY = newRelativeY.coerceIn(-0.1f, 1.1f)

                    println("Drag: absolute($newAbsoluteX, $newAbsoluteY) -> relative($boundedX, $boundedY)")

                    onPositionChange(boundedX, boundedY)
                }
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onSelect()
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = if (isSelected) 2.dp else 0.dp,
                    color = if (isSelected) Color.Blue else Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                )
                .background(
                    if (isSelected) Color.White.copy(alpha = 0.2f) else Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji.emoji,
                fontSize = 32.sp,
                textAlign = TextAlign.Center
            )
        }

        if (isSelected) {
            // Delete Button
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.TopEnd)
                    .background(Color.Red, CircleShape)
                    .clickable {
                        onDelete()
                        println("Delete clicked for emoji ${emoji.id}")
                    }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Delete",
                    tint = Color.White,
                    modifier = Modifier
                        .size(14.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}