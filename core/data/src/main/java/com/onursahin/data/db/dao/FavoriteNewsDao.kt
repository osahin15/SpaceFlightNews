package com.onursahin.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.onursahin.data.db.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteNewsDao {
    @Query("SELECT * FROM favorite_entity ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(favorite: FavoriteEntity) : Long

    @Delete
    suspend fun removeFavorite(favorite: FavoriteEntity) : Int

    @Query("DELETE FROM favorite_entity WHERE newsId = :newsId")
    suspend fun removeFavoriteById(newsId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_entity WHERE newsId = :newsId)")
    suspend fun isFavorite(newsId: Int): Boolean
}
