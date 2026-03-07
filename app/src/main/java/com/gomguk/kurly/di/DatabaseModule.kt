package com.gomguk.kurly.di

import android.content.Context
import androidx.room.Room
import com.gomguk.kurly.data.local.KurlyDatabase
import com.gomguk.kurly.data.local.dao.FavoriteProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): KurlyDatabase {
        return Room.databaseBuilder(
            context,
            KurlyDatabase::class.java,
            "kurly.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoriteProductDao(database: KurlyDatabase): FavoriteProductDao {
        return database.favoriteProductDao()
    }
}
