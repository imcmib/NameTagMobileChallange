package com.aivanchenko.nametag.feature.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivanchenko.nametag.domain.exception.AuthException
import com.aivanchenko.nametag.domain.repository.AuthRepository
import com.aivanchenko.nametag.feature.auth.AuthContract.ViewEffect
import com.aivanchenko.nametag.feature.auth.AuthContract.ViewEffect.Navigation
import com.aivanchenko.nametag.feature.auth.AuthContract.ViewEvent
import com.aivanchenko.nametag.feature.auth.AuthContract.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _viewStateFlow = MutableStateFlow(ViewState())
    val viewStateFlow = _viewStateFlow.asStateFlow()

    private val _effectFlow = Channel<ViewEffect>(Channel.BUFFERED)
    val effectFlow = _effectFlow.receiveAsFlow()

    fun onEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.OnBackClick -> _effectFlow.trySend(Navigation.NavigateBack)
            is ViewEvent.OnEmailChange -> _viewStateFlow.update { it.copy(email = event.email) }
            is ViewEvent.OnPasswordChange -> _viewStateFlow.update { it.copy(password = event.password) }
            is ViewEvent.OnSubmitClick -> onSubmitClick()
        }
    }

    private fun onSubmitClick() {
        viewModelScope.launch {
            _viewStateFlow.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            runCatching {
                val email = viewStateFlow.value.email.validate()
                val password = viewStateFlow.value.password

                authRepository.signIn(
                    email = email,
                    password = password
                )
            }.onSuccess { result ->
                _viewStateFlow.update { it.copy(isLoading = false) }
                _effectFlow.trySend(Navigation.NavigateBack)
            }.onFailure { exception ->
                val message = when (exception) {
                    is AuthException.UserNotFound -> null
                    is AuthException.InvalidCredentials -> "Invalid credentials" // TODO: Localize
                    else -> exception.message ?: "Unknown error" // TODO: Localize
                }
                _viewStateFlow.update {
                    it.copy(
                        isLoading = false,
                        error = message
                    )
                }

                // If user not found, try to sign up
                if (exception is AuthException.UserNotFound) {
                    signUp()
                }
            }
        }
    }

    private fun signUp() {
        viewModelScope.launch {
            _viewStateFlow.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            runCatching {
                val email = viewStateFlow.value.email.validate()
                val password = viewStateFlow.value.password

                authRepository.signUp(
                    email = email,
                    password = password
                )
            }.onSuccess {
                _effectFlow.trySend(Navigation.NavigateBack)
            }.onFailure { exception ->
                val message = when (exception) {
                    is AuthException.UserAlreadyExists -> "User already exists" // TODO: Localize
                    else -> exception.message ?: "Unknown error" // TODO: Localize
                }
                _viewStateFlow.update {
                    it.copy(
                        isLoading = false,
                        error = message
                    )
                }
            }
        }
    }

    @Throws(IllegalArgumentException::class)
    private fun String.validate(): String {
        if (Patterns.EMAIL_ADDRESS.matcher(this).matches().not()) {
            throw IllegalArgumentException("Invalid email") // TODO: Localize
        }
        return this
    }
}
