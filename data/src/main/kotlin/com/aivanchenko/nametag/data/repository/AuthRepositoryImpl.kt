package com.aivanchenko.nametag.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.aivanchenko.nametag.data.model.AuthBodyDto
import com.aivanchenko.nametag.data.provider.DispatchersProvider
import com.aivanchenko.nametag.data.service.AuthService
import com.aivanchenko.nametag.domain.exception.AuthException
import com.aivanchenko.nametag.domain.model.AuthState
import com.aivanchenko.nametag.domain.repository.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatchersProvider: DispatchersProvider,
    private val authService: AuthService,
) : AuthRepository {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    override val tokenFlow: Flow<String?> = context.dataStore.data.map { it[KEY_TOKEN] }

    override val authStateFlow: Flow<AuthState> = context.dataStore.data
        .map { preferences ->
            when (preferences[KEY_TOKEN]) {
                null -> AuthState.UNAUTHENTICATED
                else -> AuthState.AUTHENTICATED
            }
        }

    override suspend fun signIn(
        email: String,
        password: String,
    ) {
        withContext(dispatchersProvider.io) {
            val result = runCatching {
                authService.signIn(
                    AuthBodyDto(
                        email = email,
                        password = password
                    )
                )
            }
            if (result.isSuccess) {
                val token = result.getOrNull()?.token
                    ?: throw AuthException.Unknown
                context.dataStore.edit {
                    it[KEY_TOKEN] = token
                }
            } else {
                val throwable = result.exceptionOrNull()
                when (throwable) {
                    is HttpException -> {
                        when (throwable.code()) {
                            403 -> throw AuthException.InvalidCredentials
                            404 -> throw AuthException.UserNotFound
                            else -> throw AuthException.Unknown
                        }
                    }

                    else -> throw AuthException.Unknown
                }
            }
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
    ) {
        withContext(dispatchersProvider.io) {
            val result = runCatching {
                authService.signUp(
                    AuthBodyDto(
                        email = email,
                        password = password
                    )
                )
            }
            if (result.isSuccess) {
                val token = result.getOrNull()?.token
                    ?: throw AuthException.Unknown
                context.dataStore.edit {
                    it[KEY_TOKEN] = token
                }
            } else {
                val throwable = result.exceptionOrNull()
                when (throwable) {
                    is HttpException -> {
                        when (throwable.code()) {
                            409 -> throw AuthException.UserAlreadyExists
                            else -> throw AuthException.Unknown
                        }
                    }

                    else -> throw AuthException.Unknown
                }
            }
        }
    }

    override suspend fun signOut() {
        withContext(dispatchersProvider.io) {
            context.dataStore.edit {
                it.remove(KEY_TOKEN)
            }
        }
    }

    companion object {
        private val KEY_TOKEN = stringPreferencesKey("auth_state")
    }
}
