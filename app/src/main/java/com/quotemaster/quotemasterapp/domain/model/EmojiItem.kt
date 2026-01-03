package com.quotemaster.quotemasterapp.domain.model

import java.util.UUID

data class EmojiItem(
    val id: String = UUID.randomUUID().toString(),
    val emoji: String,
    val x: Float = 0f,
    val y: Float = 0f,
    val scale: Float = 1f,
    val rotation: Float = 0f
)