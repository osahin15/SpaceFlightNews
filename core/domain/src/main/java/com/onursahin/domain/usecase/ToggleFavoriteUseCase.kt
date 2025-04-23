package com.onursahin.domain.usecase

import com.onursahin.domain.base.DispatcherIO
import com.onursahin.domain.model.News
import com.onursahin.domain.repository.FavoriteNewsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: FavoriteNewsRepository,
    @DispatcherIO private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(news: News) = flow {
        emit(repository.toggleFavorite(news))
    }.flowOn(dispatcher)
}