package com.gomguk.kirly.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gomguk.kirly.data.local.dao.FavoriteProductDao
import com.gomguk.kirly.data.local.entity.FavoriteProductEntity

@Database(entities = [FavoriteProductEntity::class], version = 1)
abstract class KirlyDatabase : RoomDatabase() {
    abstract fun favoriteProductDao(): FavoriteProductDao
}
