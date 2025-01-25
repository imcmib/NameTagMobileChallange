package com.aivanchenko.nametag.domain.repository

import com.aivanchenko.nametag.domain.exception.AuthException
import com.aivanchenko.nametag.domain.model.AuthState
import kotlin.jvm.Throws
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val tokenFlow: Flow<String?>

    val authStateFlow: Flow<AuthState>

    /**
     * Signs in the user with the provided [email] and [password].
     *
     * @throws AuthException if the sign-in failed.
     */
    @Throws(AuthException::class)
    suspend fun signIn(
        email: String,
        password: String,
    )

    /**
     * Signs up the user with the provided [email] and [password].
     *
     * @throws AuthException if the sign-in failed.
     */
    @Throws(AuthException::class)
    suspend fun signUp(
        email: String,
        password: String,
    )

    /**
     * Signs out the user.
     */
    suspend fun signOut()
}
