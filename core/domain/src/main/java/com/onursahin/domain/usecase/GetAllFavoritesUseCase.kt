package com.onursahin.domain.usecase

import com.onursahin.domain.base.DispatcherIO
import com.onursahin.domain.model.News
import com.onursahin.domain.repository.FavoriteNewsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAllFavoritesUseCase @Inject constructor(
    private val repository: FavoriteNewsRepository,
    @DispatcherIO private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(): Flow<List<News>> {
        return repository.getAllFavorites().flowOn(dispatcher)
    }
}