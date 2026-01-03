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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quotemaster.quotemasterapp.utils.FrameStyle
import com.quotemaster.quotemasterapp.utils.FrameType

@Composable
fun FramePickerContent(
    onFrameSelected: (FrameStyle) -> Unit,
    onColorClick: () -> Unit,
    selectedFrame: FrameStyle,
    currentFrameColor: Color,
    modifier: Modifier = Modifier,
    onCancelClick: () -> Unit,
    ) {
    Column(modifier = modifier) {
        // Frame Color Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Frame Color",
                color = Color.White,
                fontSize = 16.sp
            )

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(currentFrameColor, RoundedCornerShape(8.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    .clickable { onColorClick() }
            )

            IconButton(onClick = onCancelClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(FrameStyle.valuesExcludingNone) { frame ->
                FramePreview(
                    frameStyle = frame,
                    customColor = if (frame == selectedFrame) currentFrameColor else frame.defaultBorderColor,
                    isSelected = frame == selectedFrame,
                    onClick = { onFrameSelected(frame) }
                )
            }
        }
    }
}

@Composable
fun FramePreview(
    frameStyle: FrameStyle,
    customColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(80.dp)
            .background(
                if (isSelected) Color.White.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.1f),
                RoundedCornerShape(4.dp)
            )
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = Color.White,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable { onClick() }
            .then(createFrameModifier(frameStyle, customColor, isPreview = true)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = frameStyle.name.take(6),
            color = Color.White,
            fontSize = 10.sp
        )
    }
}
@Composable
fun createFrameModifier(
    frameStyle: FrameStyle,
    customColor: Color,
    isPreview: Boolean = false
): Modifier {
    val scaleFactor = if (isPreview) 0.5f else 1f

    return Modifier
        .padding(frameStyle.padding * scaleFactor)
        .then(
            when (frameStyle.frameType) {
                FrameType.SOLID -> Modifier.border(
                    width = frameStyle.borderWidth * scaleFactor,
                    color = customColor,
                    shape = RoundedCornerShape(frameStyle.cornerRadius * scaleFactor)
                )

                FrameType.DOUBLE -> Modifier.drawWithContent {
                    drawContent()
                    drawDoubleFrame(
                        customColor,
                        frameStyle.borderWidth.toPx() * scaleFactor,
                        frameStyle.cornerRadius.toPx() * scaleFactor
                    )
                }

                FrameType.DOTTED -> Modifier.drawWithContent {
                    drawContent()
                    drawDottedFrame(
                        customColor,
                        frameStyle.borderWidth.toPx() * scaleFactor,
                        frameStyle.cornerRadius.toPx() * scaleFactor
                    )
                }

                FrameType.DASHED -> Modifier.drawWithContent {
                    drawContent()
                    drawDashedFrame(
                        customColor,
                        frameStyle.borderWidth.toPx() * scaleFactor,
                        frameStyle.cornerRadius.toPx() * scaleFactor
                    )
                }

                FrameType.ORNATE -> Modifier.drawWithContent {
                    drawContent()
                    drawOrnateFrame(
                        customColor,
                        frameStyle.borderWidth.toPx() * scaleFactor,
                        frameStyle.cornerRadius.toPx() * scaleFactor
                    )
                }

                FrameType.VINTAGE -> Modifier.drawWithContent {
                    drawContent()
                    drawVintageFrame(
                        customColor,
                        frameStyle.borderWidth.toPx() * scaleFactor,
                        frameStyle.cornerRadius.toPx() * scaleFactor
                    )
                }

                FrameType.GRADIENT -> Modifier.drawWithContent {
                    drawContent()
                    drawGradientFrame(
                        customColor,
                        frameStyle.borderWidth.toPx() * scaleFactor,
                        frameStyle.cornerRadius.toPx() * scaleFactor
                    )
                }

                FrameType.NONE ->Modifier.drawWithContent {
                    drawContent()
                }
            }
        )
        .shadow(
            elevation = frameStyle.shadowElevation * scaleFactor,
            shape = RoundedCornerShape(frameStyle.cornerRadius * scaleFactor)
        )
}

private fun DrawScope.drawDoubleFrame(
    color: Color,
    borderWidth: Float,
    cornerRadius: Float
) {
    val outerStroke = Stroke(width = borderWidth / 3)
    val innerStroke = Stroke(width = borderWidth / 3)
    val spacing = borderWidth / 3

    // Outer border
    drawRoundRect(
        color = color,
        topLeft = Offset(0f, 0f),
        size = size,
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius),
        style = outerStroke
    )

    // Inner border
    drawRoundRect(
        color = color,
        topLeft = Offset(spacing * 2, spacing * 2),
        size = Size(size.width - spacing * 4, size.height - spacing * 4),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius - spacing * 2),
        style = innerStroke
    )
}

private fun DrawScope.drawDottedFrame(
    color: Color,
    borderWidth: Float,
    cornerRadius: Float
) {
    val dotRadius = borderWidth / 4
    val spacing = borderWidth
    val perimeter = 2 * (size.width + size.height)
    val dotCount = (perimeter / spacing).toInt()

    for (i in 0 until dotCount) {
        val progress = i.toFloat() / dotCount
        val point = getPointOnRectBorder(progress, cornerRadius)
        drawCircle(
            color = color,
            radius = dotRadius,
            center = point
        )
    }
}

private fun DrawScope.drawDashedFrame(
    color: Color,
    borderWidth: Float,
    cornerRadius: Float
) {
    val dashLength = borderWidth * 3
    val gapLength = borderWidth * 2
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashLength, gapLength))

    drawRoundRect(
        color = color,
        topLeft = Offset(borderWidth / 2, borderWidth / 2),
        size = Size(size.width - borderWidth, size.height - borderWidth),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius),
        style = Stroke(width = borderWidth, pathEffect = pathEffect)
    )
}

private fun DrawScope.drawOrnateFrame(
    color: Color,
    borderWidth: Float,
    cornerRadius: Float
) {
    // Main border
    drawRoundRect(
        color = color,
        topLeft = Offset(0f, 0f),
        size = size,
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius),
        style = Stroke(width = borderWidth)
    )

    // Decorative corners
    val cornerSize = borderWidth * 2
    val corners = listOf(
        Offset(cornerSize, cornerSize),
        Offset(size.width - cornerSize, cornerSize),
        Offset(size.width - cornerSize, size.height - cornerSize),
        Offset(cornerSize, size.height - cornerSize)
    )

    corners.forEach { corner ->
        drawCircle(
            color = color,
            radius = cornerSize / 2,
            center = corner
        )
    }

    // Decorative center dots on each side
    val midpoints = listOf(
        Offset(size.width / 2, borderWidth / 2),
        Offset(size.width - borderWidth / 2, size.height / 2),
        Offset(size.width / 2, size.height - borderWidth / 2),
        Offset(borderWidth / 2, size.height / 2)
    )

    midpoints.forEach { midpoint ->
        drawCircle(
            color = color,
            radius = borderWidth / 3,
            center = midpoint
        )
    }
}

private fun DrawScope.drawVintageFrame(
    color: Color,
    borderWidth: Float,
    cornerRadius: Float
) {
    // Multiple layered borders for vintage effect
    for (i in 0 until 3) {
        val currentWidth = borderWidth - (i * borderWidth / 4)
        val offset = i * borderWidth / 4

        drawRoundRect(
            color = color.copy(alpha = 1f - (i * 0.2f)),
            topLeft = Offset(offset, offset),
            size = Size(size.width - offset * 2, size.height - offset * 2),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius),
            style = Stroke(width = currentWidth / 3)
        )
    }
}

private fun DrawScope.drawGradientFrame(
    color: Color,
    borderWidth: Float,
    cornerRadius: Float
) {
    val gradient = Brush.linearGradient(
        colors = listOf(
            color,
            color.copy(alpha = 0.7f),
            Color.White.copy(alpha = 0.3f),
            color
        ),
        start = Offset(0f, 0f),
        end = Offset(size.width, size.height)
    )

    drawRoundRect(
        brush = gradient,
        topLeft = Offset(borderWidth / 2, borderWidth / 2),
        size = Size(size.width - borderWidth, size.height - borderWidth),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius),
        style = Stroke(width = borderWidth)
    )
}

private fun DrawScope.getPointOnRectBorder(progress: Float, cornerRadius: Float): Offset {
    val totalPerimeter = 2 * (size.width + size.height)
    val distance = progress * totalPerimeter

    return when {
        distance <= size.width -> Offset(distance, 0f)
        distance <= size.width + size.height -> Offset(size.width, distance - size.width)
        distance <= 2 * size.width + size.height -> Offset(2 * size.width + size.height - distance, size.height)
        else -> Offset(0f, 2 * size.width + 2 * size.height - distance)
    }
}