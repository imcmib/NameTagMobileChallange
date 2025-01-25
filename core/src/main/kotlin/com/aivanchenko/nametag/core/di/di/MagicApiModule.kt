package com.aivanchenko.nametag.core.di.di

import com.aivanchenko.nametag.core.di.di.qualifier.MagicApi
import com.aivanchenko.nametag.data.service.MagicApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@InstallIn(SingletonComponent::class)
@Module
object MagicApiModule {

    private const val BASE_URL = "https://api.magicapi.dev/"

    @MagicApi
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                // TODO: Change logging level for production
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()

    @MagicApi
    @Provides
    @Singleton
    fun provideRetrofit(
        @MagicApi okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun provideAuthService(
        @MagicApi retrofit: Retrofit,
    ): MagicApiService = retrofit.create(MagicApiService::class.java)
}
