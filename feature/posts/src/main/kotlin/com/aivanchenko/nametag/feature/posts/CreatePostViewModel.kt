package com.aivanchenko.nametag.feature.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivanchenko.nametag.domain.repository.PostsRepository
import com.aivanchenko.nametag.feature.posts.CreatePostContract.ViewEffect
import com.aivanchenko.nametag.feature.posts.CreatePostContract.ViewEffect.Navigation
import com.aivanchenko.nametag.feature.posts.CreatePostContract.ViewEvent
import com.aivanchenko.nametag.feature.posts.CreatePostContract.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val postsRepository: PostsRepository,
) : ViewModel() {

    private val _viewStateFlow = MutableStateFlow(ViewState())
    val viewStateFlow = _viewStateFlow.asStateFlow()

    private val _effectFlow = Channel<ViewEffect>(Channel.BUFFERED)
    val effectFlow = _effectFlow.receiveAsFlow()

    fun onEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.OnBackClick -> _effectFlow.trySend(Navigation.NavigateBack)
            is ViewEvent.OnPickImageClick -> _effectFlow.trySend(Navigation.PickImage)
            is ViewEvent.OnPickImageResult -> _viewStateFlow.update { it.copy(uri = event.uri) }
            is ViewEvent.OnHeadlineChange -> _viewStateFlow.update { it.copy(headline = event.headline) }
            is ViewEvent.OnSubmitClick -> onSubmitClick()
        }
    }

    private fun onSubmitClick() {
        viewModelScope.launch {
            runCatching {
                _viewStateFlow.update { it.copy(isLoading = true) }

                postsRepository.createPost(
                    imagePath = _viewStateFlow.value.uri.toString(),
                    headline = _viewStateFlow.value.headline
                )
            }.onSuccess {
                _effectFlow.trySend(Navigation.NavigateBack)
            }.onFailure { exception ->
                _viewStateFlow.update { it.copy(isLoading = false, error = exception.message) }
            }
        }
    }
}
