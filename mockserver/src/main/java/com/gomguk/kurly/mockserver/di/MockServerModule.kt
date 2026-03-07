package com.gomguk.kurly.mockserver.di

import com.gomguk.kurly.mockserver.core.AssetFileProvider
import com.gomguk.kurly.mockserver.core.FileProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MockServerModule {

    @Binds
    @Singleton
    abstract fun bindFileProvider(
        assetFileProvider: AssetFileProvider
    ): FileProvider
}
