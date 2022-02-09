package com.arsoban.prokyrorapi.serializer

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class ReportSerializer(
    @SerialName("reporter")
    val reporter: String,
    @SerialName("userReported")
    val userReported: String,
    @SerialName("report")
    val report: String
)