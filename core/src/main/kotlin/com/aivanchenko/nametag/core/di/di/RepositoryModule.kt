package com.aivanchenko.nametag.core.di.di

import com.aivanchenko.nametag.data.repository.AuthRepositoryImpl
import com.aivanchenko.nametag.data.repository.PostsRepositoryImpl
import com.aivanchenko.nametag.domain.repository.AuthRepository
import com.aivanchenko.nametag.domain.repository.PostsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    fun bindPostsRepository(impl: PostsRepositoryImpl): PostsRepository
}
