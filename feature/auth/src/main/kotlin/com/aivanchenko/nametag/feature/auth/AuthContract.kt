package com.aivanchenko.nametag.feature.auth

import androidx.compose.runtime.Immutable

interface AuthContract {
    @Immutable
    data class ViewState(
        val email: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val error: String? = null,
    ) {
        val isSubmitButtonEnabled: Boolean =
            isLoading.not() &&
                email.isNotBlank() &&
                password.isNotBlank()
    }

    sealed interface ViewEvent {
        data object OnBackClick : ViewEvent

        data class OnEmailChange(
            val email: String,
        ) : ViewEvent

        data class OnPasswordChange(
            val password: String,
        ) : ViewEvent

        data object OnSubmitClick : ViewEvent
    }

    sealed interface ViewEffect {
        sealed interface Navigation : ViewEffect {
            object NavigateBack : Navigation
        }
    }
}
