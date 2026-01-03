package com.quotemaster.quotemasterapp.utils

import androidx.compose.ui.graphics.Color

enum class AppColors(val color: Color, val displayName: String) {
    WHITE(Color.White, "White"),
    BLACK(Color.Black, "Black"),
    RED(Color.Red, "Red"),
    GREEN(Color.Green, "Green"),
    BLUE(Color.Blue, "Blue"),
    YELLOW(Color.Yellow, "Yellow"),
    CYAN(Color.Cyan, "Cyan"),
    MAGENTA(Color.Magenta, "Magenta"),
    GRAY(Color.Gray, "Gray"),
    CORAL(Color(0xFFFF6B6B), "Coral"),
    AQUA(Color(0xFF4ECDC4), "Aqua"),
    SKY_BLUE(Color(0xFF45B7D1), "Sky Blue"),
    MINT(Color(0xFF96CEB4), "Mint"),
    CREAM(Color(0xFFFCEAA6), "Cream"),
    DARK_RED(Color(0xFFD63031), "Dark Red"),
    LIGHT_BLUE(Color(0xFF74B9FF), "Light Blue"),
    TEAL(Color(0xFF00B894), "Teal"),
    ORANGE(Color(0xFFE17055), "Orange"),
    PURPLE(Color(0xFF6C5CE7), "Purple"),
    LAVENDER(Color(0xFFA29BFE), "Lavender"),
    PINK(Color(0xFFE84393), "Pink");

    companion object {
        val allColors = entries
    }
}