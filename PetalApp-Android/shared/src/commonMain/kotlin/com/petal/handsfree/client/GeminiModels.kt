package com.petal.handsfree.client

import kotlinx.serialization.Serializable

@Serializable
data class GeminiRequest(
    val contents: List<GeminiContent>,
    val generationConfig: GeminiGenerationConfig
)

@Serializable
data class GeminiContent(val parts: List<GeminiPart>)

@Serializable
data class GeminiPart(val text: String)

@Serializable
data class GeminiGenerationConfig(
    val temperature: Double,
    val topK: Int,
    val topP: Double,
    val maxOutputTokens: Int,
    val stopSequences: List<String>
)

@Serializable
data class GeminiApiResponse(val candidates: List<GeminiCandidate>? = null)

@Serializable
data class GeminiCandidate(val content: GeminiContent? = null)

@Serializable
data class GeminiResponse(
    val intent: String = "",
    val argument: String = ""
)
