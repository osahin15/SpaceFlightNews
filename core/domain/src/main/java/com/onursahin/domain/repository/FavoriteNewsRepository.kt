package com.onursahin.domain.repository

import com.onursahin.domain.model.News
import kotlinx.coroutines.flow.Flow

interface FavoriteNewsRepository {

    suspend fun isFavorite(newsId: Int): Boolean
    fun getAllFavorites(): Flow<List<News>>
    suspend fun toggleFavorite(news: News): Boolean
}