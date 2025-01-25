package com.aivanchenko.nametag.domain.exception

import java.lang.Exception

sealed class AuthException : Exception() {
    data object InvalidCredentials : AuthException() {
        private fun readResolve(): Any = InvalidCredentials
    }

    data object UserNotFound : AuthException() {
        private fun readResolve(): Any = UserNotFound
    }

    data object UserAlreadyExists : AuthException() {
        private fun readResolve(): Any = UserAlreadyExists
    }

    data object Unknown : AuthException() {
        private fun readResolve(): Any = Unknown
    }
}
