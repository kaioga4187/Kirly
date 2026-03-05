package com.gomguk.kirly.mockserver.di

import com.gomguk.kirly.mockserver.core.AssetFileProvider
import com.gomguk.kirly.mockserver.core.FileProvider
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
