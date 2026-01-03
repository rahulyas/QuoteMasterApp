package com.quotemaster.quotemasterapp.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class FrameStyle(
    val id: Int,
    val frameName: String,
    val borderWidth: Dp,
    val defaultBorderColor: Color,
    val cornerRadius: Dp,
    val padding: Dp,
    val shadowElevation: Dp,
    val frameType: FrameType = FrameType.SOLID
) {
    THIN_WHITE(
        id = 1,
        frameName = "Thin White",
        borderWidth = 1.dp,
        defaultBorderColor = Color.White,
        cornerRadius = 8.dp,
        padding = 8.dp,
        shadowElevation = 0.dp
    ),
    THICK_GOLD(
        id = 2,
        frameName = "Thick Gold",
        borderWidth = 4.dp,
        defaultBorderColor = Color(0xFFFFD700),
        cornerRadius = 12.dp,
        padding = 12.dp,
        shadowElevation = 4.dp
    ),
    DOUBLE_BLACK(
        id = 3,
        frameName = "Double Black",
        borderWidth = 6.dp,
        defaultBorderColor = Color.Black,
        cornerRadius = 16.dp,
        padding = 16.dp,
        shadowElevation = 8.dp,
        frameType = FrameType.DOUBLE
    ),
    DOTTED_PURPLE(
        id = 4,
        frameName = "Dotted Purple",
        borderWidth = 2.dp,
        defaultBorderColor = Color(0xFF9C27B0),
        cornerRadius = 8.dp,
        padding = 12.dp,
        shadowElevation = 2.dp,
        frameType = FrameType.DOTTED
    ),
    DASHED_BLUE(
        id = 5,
        frameName = "Dashed Blue",
        borderWidth = 3.dp,
        defaultBorderColor = Color(0xFF2196F3),
        cornerRadius = 12.dp,
        padding = 10.dp,
        shadowElevation = 4.dp,
        frameType = FrameType.DASHED
    ),
    ORNATE_GOLD(
        id = 6,
        frameName = "Ornate Gold",
        borderWidth = 8.dp,
        defaultBorderColor = Color(0xFFD4AF37),
        cornerRadius = 20.dp,
        padding = 20.dp,
        shadowElevation = 6.dp,
        frameType = FrameType.ORNATE
    ),
    VINTAGE_BROWN(
        id = 7,
        frameName = "Vintage Brown",
        borderWidth = 6.dp,
        defaultBorderColor = Color(0xFF8B4513),
        cornerRadius = 0.dp,
        padding = 18.dp,
        shadowElevation = 8.dp,
        frameType = FrameType.VINTAGE
    ),
    MODERN_GRADIENT(
        id = 8,
        frameName = "Modern Gradient",
        borderWidth = 4.dp,
        defaultBorderColor = Color(0xFF6A1B9A),
        cornerRadius = 24.dp,
        padding = 14.dp,
        shadowElevation = 12.dp,
        frameType = FrameType.GRADIENT
    ),
    NONE(
        id = 0,
        frameName = "None",
        borderWidth = 0.dp,
        defaultBorderColor = Color.Transparent,
        cornerRadius = 0.dp,
        padding = 0.dp,
        shadowElevation = 0.dp,
        frameType = FrameType.NONE
    );

    companion object {
        val default = NONE
        val valuesExcludingNone = entries.filter { it != NONE }
    }
}

enum class FrameType {
    SOLID,
    DOUBLE,
    DOTTED,
    DASHED,
    ORNATE,
    VINTAGE,
    GRADIENT,
    NONE
}