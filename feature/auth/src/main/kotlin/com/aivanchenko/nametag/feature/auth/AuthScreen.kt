package com.aivanchenko.nametag.feature.auth

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.aivanchenko.nametag.feature.auth.AuthContract.ViewEffect
import com.aivanchenko.nametag.feature.auth.AuthContract.ViewEffect.Navigation
import com.aivanchenko.nametag.feature.auth.AuthContract.ViewEvent
import com.aivanchenko.nametag.feature.auth.AuthContract.ViewState
import com.aivanchenko.nametag.feature.auth.previewprovider.AuthScreenPreviewProvider
import com.aivanchenko.nametag.presentation.theme.NametagTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val viewModel: AuthViewModel = hiltViewModel()
    val viewState by viewModel.viewStateFlow.collectAsStateWithLifecycle()

    AuthScreen(
        modifier = modifier,
        viewState = viewState,
        effectFlow = viewModel.effectFlow,
        onEvent = viewModel::onEvent,
        onNavigation = { effect ->
            when (effect) {
                is Navigation.NavigateBack -> navController.popBackStack()
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun AuthScreen(
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
                    Text(text = stringResource(R.string.auth_screen_title))
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
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = viewState.email,
                onValueChange = { onEvent(ViewEvent.OnEmailChange(it)) },
                label = { Text(text = stringResource(R.string.email)) },
                placeholder = { Text(text = stringResource(R.string.email)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email
                ),
                singleLine = true
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                value = viewState.password,
                onValueChange = { onEvent(ViewEvent.OnPasswordChange(it)) },
                label = { Text(text = stringResource(R.string.password)) },
                placeholder = { Text(text = stringResource(R.string.password)) },
                visualTransformation = remember { PasswordVisualTransformation() },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onEvent(ViewEvent.OnSubmitClick) }
                ),
                singleLine = true
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
    @PreviewParameter(AuthScreenPreviewProvider::class) viewState: ViewState,
) {
    NametagTheme {
        AuthScreen(
            viewState = viewState,
            effectFlow = emptyFlow(),
            onEvent = {},
            onNavigation = {}
        )
    }
}
