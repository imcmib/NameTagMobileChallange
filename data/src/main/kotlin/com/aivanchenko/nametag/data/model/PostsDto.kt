package com.aivanchenko.nametag.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostsDto(
    @SerialName("Posts")
    val posts: List<PostDto>,
)
