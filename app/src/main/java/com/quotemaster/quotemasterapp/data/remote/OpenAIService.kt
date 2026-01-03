package com.quotemaster.quotemasterapp.data.remote

import com.quotemaster.quotemasterapp.data.model.OpenAIRequest
import com.quotemaster.quotemasterapp.data.model.OpenAIResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAIService {
    @POST("v1/chat/completions")
    suspend fun getQuote(
        @Header("Authorization") token: String,
        @Body request: OpenAIRequest
    ): OpenAIResponse
}
