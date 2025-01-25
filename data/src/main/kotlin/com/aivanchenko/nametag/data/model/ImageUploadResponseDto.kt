package com.aivanchenko.nametag.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageUploadResponseDto(
    @SerialName("url")
    val url: String,
)
