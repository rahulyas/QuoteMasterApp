package com.quotemaster.quotemasterapp.utils

import android.graphics.Typeface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.quotemaster.quotemasterapp.data.local.entity.ImageEntity
import com.quotemaster.quotemasterapp.data.local.entity.QuoteEntity
import com.quotemaster.quotemasterapp.data.local.entity.toTextStyleData
import com.quotemaster.quotemasterapp.domain.model.PixabayImage
import com.quotemaster.quotemasterapp.domain.model.Quote

fun randomTextStyle(): TextStyle {
    val fontList = listOf(
        FontFamily.SansSerif,
        FontFamily.Serif,
        FontFamily.Monospace,
        FontFamily.Cursive
    )

    val fontSizeList = listOf(20.sp, 24.sp, 28.sp, 32.sp)

    val fontWeight = listOf(
        FontWeight.Normal,
        FontWeight.Bold,
        FontWeight.ExtraBold,
        FontWeight.Light,
        FontWeight.W500
    )

    return TextStyle(
        fontFamily = fontList.random(),
        fontSize = fontSizeList.random(),
        fontWeight = fontWeight.random()
    )
}

fun getFontFamilyName(fontFamily: FontFamily?): String {
    return when (fontFamily) {
        FontFamily.SansSerif -> "sans-serif"
        FontFamily.Serif -> "serif"
        FontFamily.Monospace -> "monospace"
        FontFamily.Cursive -> "cursive"
        else -> "sans-serif" // default fallback
    }
}

fun getTypefaceStyle(fontWeight: FontWeight?): Int {
    return when (fontWeight) {
        FontWeight.Bold,
        FontWeight.ExtraBold,
        FontWeight.W500 -> Typeface.BOLD
        FontWeight.Light,
        FontWeight.Normal -> Typeface.NORMAL
        else -> Typeface.NORMAL
    }
}

fun getQuoteBackgroundColor(): Color {
    val colors = listOf(
        Color(0xFF9B59B6), // Purple
        Color(0xFF3498DB), // Blue
        Color(0xFF2ECC71), // Green
        Color(0xFFE74C3C), // Red
        Color(0xFFF39C12), // Orange-Yellow
        Color(0xFF1ABC9C), // Cyan
        Color(0xFFE67E22), // Dark Orange
        Color(0xFF34495E), // Dark Blue-Grey
        Color(0xFF16A085), // Teal
        Color(0xFF27AE60)  // Emerald
    )
    return colors.random()
}


fun ImageEntity.toPixabayImage(): PixabayImage {
    return PixabayImage(
        id = id,
        tags = tags,
        previewURL = previewURL,
        largeImageURL = largeImageURL
    )
}

fun PixabayImage.toImageEntity(category: String): ImageEntity {
    return ImageEntity(
        id = id,
        tags = tags,
        previewURL = previewURL,
        largeImageURL = largeImageURL,
        imageType = "photo", // default
        category = category
    )
}


fun QuoteEntity.toQuote(): Quote {
    return Quote(
        content = content,
        author = author
    )
}

fun Quote.toQuoteEntity(imageId: Int, category: String, textStyle: TextStyle): QuoteEntity {
    return QuoteEntity(
        imageId = imageId,
        content = content,
        author = author,
        category = category,
        textStyle = textStyle.toTextStyleData()
    )
}
