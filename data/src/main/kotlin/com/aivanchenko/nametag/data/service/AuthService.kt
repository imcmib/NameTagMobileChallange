package com.aivanchenko.nametag.data.service

import com.aivanchenko.nametag.data.model.AuthBodyDto
import com.aivanchenko.nametag.data.model.TokenDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("signin")
    suspend fun signIn(
        @Body body: AuthBodyDto,
    ): TokenDto

    @POST("signup")
    suspend fun signUp(
        @Body body: AuthBodyDto,
    ): TokenDto
}
