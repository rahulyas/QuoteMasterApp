package com.quotemaster.quotemasterapp.data.local.dao

import androidx.room.*
import com.quotemaster.quotemasterapp.data.local.entity.QuoteEntity

@Dao
interface QuoteDao {
    @Query("SELECT * FROM quotes WHERE category = :category ORDER BY createdAt DESC")
    suspend fun getQuotesByCategory(category: String): List<QuoteEntity>

    @Query("SELECT * FROM quotes WHERE imageId = :imageId LIMIT 1")
    suspend fun getQuoteByImageId(imageId: Int): QuoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuotes(quotes: List<QuoteEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: QuoteEntity)

    @Update
    suspend fun updateQuote(quote: QuoteEntity)

    @Query("DELETE FROM quotes WHERE category = :category")
    suspend fun deleteQuotesByCategory(category: String)

    @Query("SELECT COUNT(*) FROM quotes WHERE category = :category")
    suspend fun getQuoteCountByCategory(category: String): Int

    @Query("SELECT * FROM quotes ORDER BY createdAt DESC")
    suspend fun getAllQuotes(): List<QuoteEntity>

    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM quotes 
            WHERE category = :category 
            AND lastUpdated < :timestamp
            LIMIT 1
        )
    """)
    suspend fun hasQuotesOlderThan(category: String, timestamp: Long): Boolean

    @Query("SELECT * FROM quotes WHERE category = :category ORDER BY lastUpdated DESC LIMIT 1")
    suspend fun getMostRecentQuote(category: String): QuoteEntity?
}
