package com.aivanchenko.nametag.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostBodyDto(
    @SerialName("headline")
    val headline: String,

    @SerialName("image")
    val image: String,
)
