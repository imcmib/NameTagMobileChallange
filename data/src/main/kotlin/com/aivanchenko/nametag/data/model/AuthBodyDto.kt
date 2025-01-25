package com.aivanchenko.nametag.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthBodyDto(
    @SerialName("email")
    val email: String,

    @SerialName("password")
    val password: String,
)
