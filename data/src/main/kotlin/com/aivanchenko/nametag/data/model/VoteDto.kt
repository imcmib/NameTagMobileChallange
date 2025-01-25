package com.aivanchenko.nametag.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VoteDto(
    @SerialName("upvotes")
    val upvotes: Long,
)
