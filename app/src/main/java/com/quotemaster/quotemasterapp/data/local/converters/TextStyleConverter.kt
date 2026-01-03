package com.quotemaster.quotemasterapp.data.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quotemaster.quotemasterapp.data.model.TextStyleData

class TextStyleConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromTextStyleData(textStyleData: TextStyleData?): String {
        return if (textStyleData != null) {
            try {
                gson.toJson(textStyleData)
            } catch (e: Exception) {
                gson.toJson(TextStyleData())
            }
        } else {
            gson.toJson(TextStyleData())
        }
    }

    @TypeConverter
    fun toTextStyleData(textStyleDataString: String?): TextStyleData {
        return if (!textStyleDataString.isNullOrBlank()) {
            try {
                val type = object : TypeToken<TextStyleData>() {}.type
                gson.fromJson<TextStyleData>(textStyleDataString, type) ?: TextStyleData()
            } catch (e: Exception) {
                TextStyleData()
            }
        } else {
            TextStyleData()
        }
    }
}
