package com.quotemaster.quotemasterapp.data.model

data class OpenAIRequest(
    val model: String = "gpt-4o-mini",
    val messages: List<Message>,
    val temperature: Double = 0.7,
    val max_tokens: Int = 1000
)

data class Message(
    val role: String,
    val content: String
)
