package com.onursahin.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.onursahin.data.db.dao.NewsArticleDao
import com.onursahin.data.db.entity.NewsEntity
import com.onursahin.data.mapper.toUiModel
import com.onursahin.data.network.SearchNewsPagingSource
import com.onursahin.data.network.SpaceNewsDataSource
import com.onursahin.data.network.SpaceNewsPagingRemoteMediator
import com.onursahin.data.network.SpaceNewsPagingRemoteMediator.Companion.DEFAULT_PAGE_SIZE
import com.onursahin.data.response.NewsResultsResponse
import com.onursahin.domain.UiResponse
import com.onursahin.domain.model.News
import com.onursahin.domain.repository.SpaceNewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlinx.coroutines.flow.map as flowMap

class SpaceNewsRepositoryImpl @Inject constructor(
    private val articleDao: NewsArticleDao,
    private val remoteMediator: SpaceNewsPagingRemoteMediator,
    private val dataSource: SpaceNewsDataSource
) : SpaceNewsRepository {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getArticles(search: String?): Flow<PagingData<News>> {
        val pager = if (search.isNullOrBlank()) {
            Pager(
                config = PagingConfig(
                    pageSize = DEFAULT_PAGE_SIZE,
                    enablePlaceholders = false,
                    initialLoadSize = DEFAULT_PAGE_SIZE,
                    prefetchDistance = 5
                ),
                remoteMediator = remoteMediator,
                pagingSourceFactory = { articleDao.pagingSource() }
            )
        } else {
            Pager(
                config = PagingConfig(
                    pageSize = DEFAULT_PAGE_SIZE,
                    enablePlaceholders = false,
                    initialLoadSize = DEFAULT_PAGE_SIZE,
                    prefetchDistance = 5
                ),
                pagingSourceFactory = {
                    SearchNewsPagingSource(
                        dataSource,
                        query = search
                    )
                }
            )
        }
        return pager.flow.flowMap { pagingData ->
            pagingData.map {
                when (val item = it) {
                    is NewsEntity -> item.toUiModel()
                    is NewsResultsResponse -> item.toUiModel()
                    else -> throw IllegalArgumentException("Unsupported item type: $item")
                }
            }
        }
    }

    override suspend fun getArticleWithId(id: Int): UiResponse<News> {
        TODO("Not yet implemented")
    }
}