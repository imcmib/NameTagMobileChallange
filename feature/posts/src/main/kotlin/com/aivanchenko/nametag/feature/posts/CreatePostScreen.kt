package com.aivanchenko.nametag.feature.posts

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.SubcomposeAsyncImage
import com.aivanchenko.nametag.feature.posts.CreatePostContract.ViewEffect
import com.aivanchenko.nametag.feature.posts.CreatePostContract.ViewEffect.Navigation
import com.aivanchenko.nametag.feature.posts.CreatePostContract.ViewEvent
import com.aivanchenko.nametag.feature.posts.CreatePostContract.ViewState
import com.aivanchenko.nametag.feature.posts.previewprovider.CreatePostScreenPreviewProvider
import com.aivanchenko.nametag.presentation.theme.NametagTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun CreatePostScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val viewModel: CreatePostViewModel = hiltViewModel()
    val viewState by viewModel.viewStateFlow.collectAsStateWithLifecycle()

    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        viewModel.onEvent(ViewEvent.OnPickImageResult(uri))
    }

    CreatePostScreen(
        modifier = modifier,
        viewState = viewState,
        effectFlow = viewModel.effectFlow,
        onEvent = viewModel::onEvent,
        onNavigation = { effect ->
            when (effect) {
                is Navigation.NavigateBack -> navController.popBackStack()
                is Navigation.PickImage -> pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun CreatePostScreen(
    modifier: Modifier = Modifier,
    viewState: ViewState,
    effectFlow: Flow<ViewEffect>,
    onEvent: (ViewEvent) -> Unit,
    onNavigation: (Navigation) -> Unit,
) {
    LaunchedEffect(key1 = effectFlow) {
        effectFlow.collectLatest { effect ->
            when (effect) {
                is Navigation -> onNavigation(effect)
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.create_post_screen_title))
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onEvent(ViewEvent.OnBackClick) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp)
                .padding(paddingValues = innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            if (viewState.uri == null) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    onClick = { onEvent(ViewEvent.OnPickImageClick) }
                ) {
                    Text(text = stringResource(R.string.pick_image))
                }
            } else {
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .fillMaxWidth(),
                    model = viewState.uri,
                    contentDescription = stringResource(R.string.image),
                    contentScale = ContentScale.FillWidth,
                    loading = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    },
                    error = {
                        Box(
                            modifier = Modifier
                                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.error),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                )
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                value = viewState.headline,
                onValueChange = { onEvent(ViewEvent.OnHeadlineChange(it)) },
                label = { Text(text = stringResource(R.string.headline)) },
                placeholder = { Text(text = stringResource(R.string.headline)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onEvent(ViewEvent.OnSubmitClick) }
                ),
                minLines = 3
            )

            viewState.error?.let { error ->
                Text(
                    modifier = Modifier
                        .padding(top = 16.dp),
                    text = error,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                enabled = viewState.isSubmitButtonEnabled,
                onClick = { onEvent(ViewEvent.OnSubmitClick) }
            ) {
                if (viewState.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(text = stringResource(R.string.submit))
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview(
    @PreviewParameter(CreatePostScreenPreviewProvider::class) viewState: ViewState,
) {
    NametagTheme {
        CreatePostScreen(
            viewState = viewState,
            effectFlow = emptyFlow(),
            onEvent = {},
            onNavigation = {}
        )
    }
}
