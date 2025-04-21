package com.onursahin.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.onursahin.data.db.entity.NewsEntity

@Dao
interface NewsArticleDao {
    @Query("SELECT * FROM news_article ORDER BY publishedAt DESC")
    fun pagingSource(): PagingSource<Int, NewsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(newsArticles: List<NewsEntity>)

    @Query("DELETE FROM news_article")
    suspend fun clearAll()
}
