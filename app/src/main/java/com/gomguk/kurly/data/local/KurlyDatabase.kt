package com.gomguk.kurly.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gomguk.kurly.data.local.dao.FavoriteProductDao
import com.gomguk.kurly.data.local.entity.FavoriteProductEntity

@Database(entities = [FavoriteProductEntity::class], version = 1)
abstract class KurlyDatabase : RoomDatabase() {
    abstract fun favoriteProductDao(): FavoriteProductDao
}
