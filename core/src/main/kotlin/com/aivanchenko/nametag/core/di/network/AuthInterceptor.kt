package com.aivanchenko.nametag.core.di.network

import com.aivanchenko.nametag.domain.repository.AuthRepository
import dagger.Lazy
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val authRepository: Lazy<AuthRepository>,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token: String? = runBlocking { authRepository.get().tokenFlow.firstOrNull() }
        val request = chain.request()
            .newBuilder()
            .apply {
                token?.let {
                    addHeader("Authorization", "Bearer $it")
                }
            }
            .build()
        return chain.proceed(request)
    }
}
