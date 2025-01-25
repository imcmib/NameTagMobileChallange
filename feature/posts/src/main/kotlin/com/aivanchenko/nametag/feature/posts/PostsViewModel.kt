package com.aivanchenko.nametag.feature.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivanchenko.nametag.domain.model.AuthState
import com.aivanchenko.nametag.domain.model.Post
import com.aivanchenko.nametag.domain.repository.AuthRepository
import com.aivanchenko.nametag.domain.repository.PostsRepository
import com.aivanchenko.nametag.feature.posts.PostsContract.ViewEffect
import com.aivanchenko.nametag.feature.posts.PostsContract.ViewEffect.Navigation
import com.aivanchenko.nametag.feature.posts.PostsContract.ViewEvent
import com.aivanchenko.nametag.feature.posts.PostsContract.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val postsRepository: PostsRepository,
) : ViewModel() {

    private val _viewStateFlow = MutableStateFlow(ViewState())
    val viewStateFlow = _viewStateFlow.combine(authRepository.authStateFlow) { viewState, authState ->
        viewState.copy(
            isSignedIn = authState == AuthState.AUTHENTICATED
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ViewState()
    )

    private val _effectFlow = Channel<ViewEffect>(Channel.BUFFERED)
    val effectFlow = _effectFlow.receiveAsFlow()

    fun onEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.OnInit -> onInit()
            is ViewEvent.OnToolbarActionClick -> onToolbarActionClick()
            is ViewEvent.OnUpVoteClick -> onUpVoteClick(post = event.post)
            is ViewEvent.OnDownVoteClick -> onDownVoteClick(post = event.post)
            is ViewEvent.OnRetryClick -> onInit()
            is ViewEvent.OnCreatePostClick -> _effectFlow.trySend(Navigation.NavigateToCreatePost)
        }
    }

    private fun onToolbarActionClick() {
        if (viewStateFlow.value.isSignedIn) {
            viewModelScope.launch {
                authRepository.signOut()
            }
        } else {
            _effectFlow.trySend(Navigation.NavigateToSignIn)
        }
    }

    private fun onInit() {
        viewModelScope.launch {
            _viewStateFlow.update { it.copy(isLoading = true, error = null) }
            runCatching {
                postsRepository.getPosts()
            }.onSuccess { posts ->
                _viewStateFlow.update { it.copy(posts = posts, isLoading = false) }
            }.onFailure { exception ->
                _viewStateFlow.update { it.copy(isLoading = false, error = exception.message) }
            }
        }
    }

    private fun onUpVoteClick(post: Post) {
        viewModelScope.launch {
            runCatching {
                postsRepository.upVote(post.id)
            }.onSuccess { upvotes ->
                _viewStateFlow.update {
                    it.copy(
                        posts = it.posts.map {
                            if (it.id == post.id) {
                                it.copy(upvotes = upvotes)
                            } else {
                                it
                            }
                        }
                    )
                }
            }.onFailure { exception ->
                _effectFlow.trySend(ViewEffect.ShowToast(message = exception.message.orEmpty()))
            }
        }
    }

    private fun onDownVoteClick(post: Post) {
        viewModelScope.launch {
            runCatching {
                postsRepository.downVote(post.id)
            }.onSuccess { upvotes ->
                _viewStateFlow.update {
                    it.copy(
                        posts = it.posts.map {
                            if (it.id == post.id) {
                                it.copy(upvotes = upvotes)
                            } else {
                                it
                            }
                        }
                    )
                }
            }.onFailure { exception ->
                _effectFlow.trySend(ViewEffect.ShowToast(message = exception.message.orEmpty()))
            }
        }
    }
}
