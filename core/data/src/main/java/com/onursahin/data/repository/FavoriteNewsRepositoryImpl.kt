package com.onursahin.data.repository

import com.onursahin.data.db.dao.FavoriteNewsDao
import com.onursahin.data.mapper.toEntity
import com.onursahin.data.mapper.toUiModel
import com.onursahin.domain.model.News
import com.onursahin.domain.repository.FavoriteNewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteNewsRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteNewsDao,
) : FavoriteNewsRepository {

    override suspend fun isFavorite(newsId: Int): Boolean {
        return favoriteDao.isFavorite(newsId)
    }

    override fun getAllFavorites(): Flow<List<News>> {
        return favoriteDao.getAllFavorites().map { list ->
            list.map { it.toUiModel() }
        }
    }

    override suspend fun toggleFavorite(news: News): Boolean {
        val currentlyFav = favoriteDao.isFavorite(news.id)
        val operationSucceed = if (currentlyFav) {
            val result = favoriteDao.removeFavorite(news.toEntity())
            result > 0
        } else {
            val result = favoriteDao.addFavorite(news.toEntity())
            result != -1L
        }
        return if (operationSucceed) !currentlyFav else currentlyFav
    }


}