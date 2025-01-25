package com.aivanchenko.nametag.feature.posts

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.aivanchenko.nametag.domain.model.Post

interface PostsContract {
    @Immutable
    data class ViewState(
        val posts: List<Post> = emptyList(),
        val isSignedIn: Boolean = false,
        val isLoading: Boolean = false,
        val error: String? = null,
    ) {
        @StringRes val toolbarActionTextRes =
            if (isSignedIn) R.string.sign_out else R.string.sign_in

        val canVote: Boolean = isSignedIn
    }

    sealed interface ViewEvent {
        data object OnInit : ViewEvent

        data object OnToolbarActionClick : ViewEvent

        data class OnUpVoteClick(
            val post: Post,
        ) : ViewEvent

        data class OnDownVoteClick(
            val post: Post,
        ) : ViewEvent

        data object OnRetryClick : ViewEvent

        data object OnCreatePostClick : ViewEvent
    }

    sealed interface ViewEffect {
        data class ShowToast(
            val message: String,
        ) : ViewEffect

        sealed interface Navigation : ViewEffect {
            object NavigateToSignIn : Navigation

            object NavigateToCreatePost : Navigation
        }
    }
}
