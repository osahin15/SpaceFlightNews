package com.onursahin.domain.usecase

import com.onursahin.domain.base.DispatcherIO
import com.onursahin.domain.repository.FavoriteNewsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class IsFavoriteUseCase @Inject constructor(
    private val repository: FavoriteNewsRepository,
    @DispatcherIO private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(newsId: Int): Boolean = withContext(dispatcher) {
        repository.isFavorite(newsId)
    }
}