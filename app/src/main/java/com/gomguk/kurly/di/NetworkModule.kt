package com.gomguk.kurly.di

import com.gomguk.kurly.data.SectionType
import com.gomguk.kurly.data.api.KurlyApiService
import com.gomguk.kurly.mockserver.MockInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://kurly.com/") // 실제 베이스 URL
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideKurlyApiService(retrofit: Retrofit): KurlyApiService {
        return retrofit.create(KurlyApiService::class.java)
    }
}
