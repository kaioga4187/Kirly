package com.gomguk.kirly.di

import com.gomguk.kirly.data.SectionType
import com.gomguk.kirly.mockserver.MockInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(SectionType::class.java, JsonDeserializer { json, _, _ ->
                val typeString = json.asString
                when (typeString.lowercase()) {
                    "vertical" -> SectionType.Vertical
                    "horizontal" -> SectionType.Horizontal
                    "grid" -> SectionType.Grid
                    else -> SectionType.Unknown(typeString)
                }
            })
            .create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        mockInterceptor: MockInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(mockInterceptor)
            .build()
    }
}
