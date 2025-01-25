package com.aivanchenko.nametag.data.model

import com.aivanchenko.nametag.domain.model.Post
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostDto(
    @SerialName("id")
    val id: String?,
    @SerialName("author")
    val author: String?,
    @SerialName("headline")
    val headline: String?,
    @SerialName("image")
    val image: String?,
    @SerialName("upvotes")
    val upvotes: Long,
)

fun PostDto.toPost() =
    Post(
        id = id ?: throw IllegalArgumentException("Post id must not be null"),
        author = author ?: "",
        headline = headline ?: "",
        image = image ?: "",
        upvotes = upvotes,
    )
