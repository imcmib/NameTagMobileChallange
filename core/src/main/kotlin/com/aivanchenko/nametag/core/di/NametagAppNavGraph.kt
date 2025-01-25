package com.aivanchenko.nametag.core.di

import kotlinx.serialization.Serializable

object NametagAppNavGraph {

    @Serializable
    data object PostsScreen

    @Serializable
    data object AuthScreen

    @Serializable
    data object CreatePostScreen
}
