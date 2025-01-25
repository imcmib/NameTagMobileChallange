package com.aivanchenko.nametag.feature.posts

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.aivanchenko.nametag.core.di.NametagAppNavGraph.AuthScreen
import com.aivanchenko.nametag.core.di.NametagAppNavGraph.CreatePostScreen
import com.aivanchenko.nametag.feature.posts.PostsContract.ViewEffect
import com.aivanchenko.nametag.feature.posts.PostsContract.ViewEffect.Navigation
import com.aivanchenko.nametag.feature.posts.PostsContract.ViewEvent
import com.aivanchenko.nametag.feature.posts.PostsContract.ViewState
import com.aivanchenko.nametag.feature.posts.composable.PostCard
import com.aivanchenko.nametag.feature.posts.previewprovider.PostsScreenPreviewProvider
import com.aivanchenko.nametag.presentation.theme.NametagTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun PostsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val viewModel: PostsViewModel = hiltViewModel()
    val viewState by viewModel.viewStateFlow.collectAsStateWithLifecycle()

    PostsScreen(
        modifier = modifier,
        viewState = viewState,
        effectFlow = viewModel.effectFlow,
        onEvent = viewModel::onEvent,
        onNavigation = { effect ->
            when (effect) {
                is Navigation.NavigateToSignIn -> navController.navigate(AuthScreen)
                is Navigation.NavigateToCreatePost -> navController.navigate(CreatePostScreen)
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun PostsScreen(
    modifier: Modifier = Modifier,
    viewState: ViewState,
    effectFlow: Flow<ViewEffect>,
    onEvent: (ViewEvent) -> Unit,
    onNavigation: (Navigation) -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = effectFlow) {
        effectFlow.collectLatest { effect ->
            when (effect) {
                is Navigation -> onNavigation(effect)
                is ViewEffect.ShowToast -> Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        onEvent(ViewEvent.OnInit)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.posts_screen_title))
                },
                actions = {
                    TextButton(
                        onClick = { onEvent(ViewEvent.OnToolbarActionClick) }
                    ) {
                        Text(text = stringResource(viewState.toolbarActionTextRes))
                    }
                }
            )
        },
        floatingActionButton = {
            if (viewState.isSignedIn) {
                FloatingActionButton(
                    onClick = { onEvent(ViewEvent.OnCreatePostClick) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.create_post)
                    )
                }
            }
        }
    ) { innerPadding ->
        if (viewState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (viewState.error != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .padding(all = 16.dp),
                    text = viewState.error,
                    style = MaterialTheme.typography.headlineMedium
                )

                TextButton(
                    onClick = { onEvent(ViewEvent.OnRetryClick) }
                ) {
                    Text(text = stringResource(R.string.retry))
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = innerPadding)
                    .consumeWindowInsets(paddingValues = innerPadding),
                contentPadding = PaddingValues(all = 16.dp),
                verticalArrangement = Arrangement.spacedBy(space = 16.dp)
            ) {
                items(
                    items = viewState.posts,
                    key = { it.id }
                ) { item ->
                    PostCard(
                        modifier = Modifier
                            .fillMaxWidth(),
                        post = item,
                        canVote = viewState.canVote,
                        onUpVoteClick = { onEvent(ViewEvent.OnUpVoteClick(item)) },
                        onDownVoteClick = { onEvent(ViewEvent.OnDownVoteClick(item)) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview(
    @PreviewParameter(PostsScreenPreviewProvider::class) viewState: ViewState,
) {
    NametagTheme {
        PostsScreen(
            viewState = viewState,
            effectFlow = emptyFlow(),
            onEvent = {},
            onNavigation = {}
        )
    }
}
