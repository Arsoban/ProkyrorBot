package com.arsoban.com.arsoban.serializer

import kotlinx.serialization.Serializable

@Serializable
data class LyricsSerializer(val title: String, val author: String, val lyrics: String, val thumbnail: Map<String, String>, val links: Map<String, String>, val disclaimer: String)
