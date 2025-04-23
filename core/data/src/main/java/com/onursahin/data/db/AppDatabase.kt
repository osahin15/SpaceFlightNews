package com.onursahin.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.onursahin.data.db.dao.FavoriteNewsDao
import com.onursahin.data.db.dao.NewsArticleDao
import com.onursahin.data.db.dao.RemoteKeysDao
import com.onursahin.data.db.entity.FavoriteEntity
import com.onursahin.data.db.entity.NewsEntity
import com.onursahin.data.db.entity.RemoteKeys

@Database(entities = [NewsEntity::class, RemoteKeys::class, FavoriteEntity::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): NewsArticleDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun favoriteNewsDao(): FavoriteNewsDao
}