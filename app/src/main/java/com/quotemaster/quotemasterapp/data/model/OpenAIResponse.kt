package com.quotemaster.quotemasterapp.data.model

data class OpenAIResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)
