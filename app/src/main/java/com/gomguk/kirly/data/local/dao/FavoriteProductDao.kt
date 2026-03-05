package com.gomguk.kirly.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gomguk.kirly.data.local.entity.FavoriteProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favoriteProductEntity: FavoriteProductEntity)

    @Query("DELETE FROM favorite_products WHERE id = :productId")
    suspend fun deleteFavorite(productId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_products WHERE id = :productId)")
    fun isFavorite(productId: Int): Flow<Boolean>

    @Query("SELECT id FROM favorite_products")
    fun getAllFavoriteIds(): Flow<List<Int>>

    @Query("SELECT id FROM favorite_products")
    suspend fun getFavoriteIdList(): List<Int>
}
