package com.aivanchenko.nametag.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aivanchenko.nametag.core.di.NametagAppNavGraph.AuthScreen
import com.aivanchenko.nametag.core.di.NametagAppNavGraph.CreatePostScreen
import com.aivanchenko.nametag.core.di.NametagAppNavGraph.PostsScreen
import com.aivanchenko.nametag.feature.auth.AuthScreen
import com.aivanchenko.nametag.feature.posts.CreatePostScreen
import com.aivanchenko.nametag.feature.posts.PostsScreen

@Composable
fun NametagApp() {
    val navController = rememberNavController()

    NavHost(
        modifier = Modifier
            .fillMaxSize(),
        navController = navController,
        startDestination = PostsScreen
    ) {
        composable<PostsScreen> {
            PostsScreen(navController = navController)
        }

        composable<AuthScreen> {
            AuthScreen(navController = navController)
        }

        composable<CreatePostScreen> {
            CreatePostScreen(navController = navController)
        }
    }
}
