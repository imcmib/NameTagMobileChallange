package com.aivanchenko.nametag.core.di.di

import com.aivanchenko.nametag.data.provider.DispatchersProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CommonModule {

    @Provides
    @Singleton
    fun provideDispatchersProvider(): DispatchersProvider =
        DispatchersProvider.Companion.Default
}
