package com.aivanchenko.nametag.domain.model

data class Post(
    val id: String,
    val author: String,
    val headline: String,
    val image: String,
    val upvotes: Long,
)
