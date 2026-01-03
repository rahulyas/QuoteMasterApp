package com.quotemaster.quotemasterapp.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.quotemaster.quotemasterapp.data.local.converters.TextStyleConverter
import com.quotemaster.quotemasterapp.data.local.dao.ImageDao
import com.quotemaster.quotemasterapp.data.local.dao.QuoteDao
import com.quotemaster.quotemasterapp.data.local.entity.ImageEntity
import com.quotemaster.quotemasterapp.data.local.entity.QuoteEntity

@Database(
    entities = [ImageEntity::class, QuoteEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TextStyleConverter::class)
abstract class QuoteDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
    abstract fun quoteDao(): QuoteDao

    companion object {
        @Volatile
        private var INSTANCE: QuoteDatabase? = null

        fun getDatabase(context: Context): QuoteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuoteDatabase::class.java,
                    "quote_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}