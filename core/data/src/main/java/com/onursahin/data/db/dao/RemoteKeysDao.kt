package com.onursahin.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.onursahin.data.db.entity.RemoteKeys

@Dao
interface RemoteKeysDao {
    @Query("SELECT * FROM remote_keys WHERE articleId = :id")
    suspend fun remoteKeysArticleId(id: Int): RemoteKeys?

    @Query("SELECT * FROM remote_keys ORDER BY nextOffset DESC LIMIT 1")
    suspend fun getLastRemoteKey(): RemoteKeys?

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(keys: List<RemoteKeys>)

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}