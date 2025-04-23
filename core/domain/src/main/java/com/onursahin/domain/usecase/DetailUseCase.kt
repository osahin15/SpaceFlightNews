package com.onursahin.domain.usecase

import com.onursahin.domain.base.DispatcherIO
import com.onursahin.domain.base.UiResponse
import com.onursahin.domain.model.News
import com.onursahin.domain.repository.SpaceNewsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DetailUseCase @Inject constructor(
    private val repository: SpaceNewsRepository,
    @DispatcherIO private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(id: Int): Flow<UiResponse<News>> = flow {
        emit(UiResponse.Loading)
        emit(repository.getArticleWithId(id))
    }.flowOn(dispatcher)
}