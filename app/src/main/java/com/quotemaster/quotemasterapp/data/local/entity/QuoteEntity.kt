package com.quotemaster.quotemasterapp.data.local.entity

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.quotemaster.quotemasterapp.data.local.converters.TextStyleConverter
import com.quotemaster.quotemasterapp.data.model.TextStyleData

@Entity(tableName = "quotes")
@TypeConverters(TextStyleConverter::class)
data class QuoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val imageId: Int,
    val content: String,
    val author: String?,
    val category: String,
    val textStyle: TextStyleData,
    val createdAt: Long = System.currentTimeMillis(),
    val lastUpdated: Long = System.currentTimeMillis()
)

fun TextStyle.toTextStyleData(): TextStyleData {
    return TextStyleData(
        fontFamily = when (this.fontFamily) {
            FontFamily.SansSerif -> "SansSerif"
            FontFamily.Serif -> "Serif"
            FontFamily.Monospace -> "Monospace"
            FontFamily.Cursive -> "Cursive"
            else -> "SansSerif"
        },
        fontSize = this.fontSize.value,
        fontWeight = when (this.fontWeight) {
            FontWeight.Normal -> "Normal"
            FontWeight.Bold -> "Bold"
            FontWeight.ExtraBold -> "ExtraBold"
            FontWeight.Light -> "Light"
            FontWeight.W500 -> "W500"
            else -> "Normal"
        }
    )
}

fun TextStyleData.toTextStyle(): TextStyle {
    return TextStyle(
        fontFamily = when (fontFamily) {
            "SansSerif" -> FontFamily.SansSerif
            "Serif" -> FontFamily.Serif
            "Monospace" -> FontFamily.Monospace
            "Cursive" -> FontFamily.Cursive
            else -> FontFamily.SansSerif
        },
        fontSize = fontSize.sp,
        fontWeight = when (fontWeight) {
            "Normal" -> FontWeight.Normal
            "Bold" -> FontWeight.Bold
            "ExtraBold" -> FontWeight.ExtraBold
            "Light" -> FontWeight.Light
            "W500" -> FontWeight.W500
            else -> FontWeight.Normal
        }
    )
}
