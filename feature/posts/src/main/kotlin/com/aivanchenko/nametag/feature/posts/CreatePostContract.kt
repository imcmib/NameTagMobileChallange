package com.aivanchenko.nametag.feature.posts

import android.net.Uri
import androidx.compose.runtime.Immutable

interface CreatePostContract {
    @Immutable
    data class ViewState(
        val uri: Uri? = null,
        val headline: String = "",
        val isLoading: Boolean = false,
        val error: String? = null,
    ) {
        val isSubmitButtonEnabled: Boolean =
            isLoading.not() &&
                uri != null
    }

    sealed interface ViewEvent {

        data object OnBackClick : ViewEvent

        data object OnPickImageClick : ViewEvent

        data class OnPickImageResult(
            val uri: Uri?,
        ) : ViewEvent

        data class OnHeadlineChange(
            val headline: String,
        ) : ViewEvent

        data object OnSubmitClick : ViewEvent
    }

    sealed interface ViewEffect {
        sealed interface Navigation : ViewEffect {
            data object NavigateBack : Navigation

            data object PickImage : Navigation
        }
    }
}
