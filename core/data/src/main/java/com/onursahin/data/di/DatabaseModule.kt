package com.onursahin.data.di

import android.content.Context
import androidx.room.Room
import com.onursahin.data.db.AppDatabase
import com.onursahin.data.db.dao.NewsArticleDao
import com.onursahin.data.db.dao.RemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DB_NAME = "news_db"

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DB_NAME
        ).build()

    @Provides
    @Singleton
    fun provideArticleDao(db: AppDatabase): NewsArticleDao =
        db.articleDao()

    @Provides
    @Singleton
    fun provideRemoteKeysDao(db: AppDatabase): RemoteKeysDao =
        db.remoteKeysDao()
}