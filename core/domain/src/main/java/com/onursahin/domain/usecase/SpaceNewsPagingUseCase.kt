package com.onursahin.domain.usecase

import androidx.paging.PagingData
import com.onursahin.domain.DispatcherIO
import com.onursahin.domain.model.News
import com.onursahin.domain.repository.SpaceNewsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SpaceNewsPagingUseCase @Inject constructor(
    private val repository: SpaceNewsRepository,
    @DispatcherIO private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(search : String?): Flow<PagingData<News>> = flow {
        val response = repository.getArticles(search)
        emitAll(response)
    }.flowOn(dispatcher)
}