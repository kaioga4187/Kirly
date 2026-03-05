package com.gomguk.kirly.di

import android.content.Context
import androidx.room.Room
import com.gomguk.kirly.data.local.KirlyDatabase
import com.gomguk.kirly.data.local.dao.FavoriteProductDao
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
    fun provideDatabase(@ApplicationContext context: Context): KirlyDatabase {
        return Room.databaseBuilder(
            context,
            KirlyDatabase::class.java,
            "kirly.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoriteProductDao(database: KirlyDatabase): FavoriteProductDao {
        return database.favoriteProductDao()
    }
}
