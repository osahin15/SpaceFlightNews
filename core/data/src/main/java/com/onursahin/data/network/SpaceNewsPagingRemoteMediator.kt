package com.onursahin.data.network

import android.util.Log
import androidx.core.net.toUri
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.paging.RemoteMediator.MediatorResult.Error
import androidx.paging.RemoteMediator.MediatorResult.Success
import androidx.room.withTransaction
import com.onursahin.data.base.PagingSourceThrowable
import com.onursahin.data.base.RemoteResponse
import com.onursahin.data.db.AppDatabase
import com.onursahin.data.db.entity.NewsEntity
import com.onursahin.data.db.entity.RemoteKeys
import com.onursahin.data.mapper.toEntity
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class SpaceNewsPagingRemoteMediator @Inject constructor(
    private val dataSource: SpaceNewsDataSource,
    private val db: AppDatabase
) : RemoteMediator<Int, NewsEntity>() {

    private val articleDao = db.articleDao()
    private val remoteKeysDao = db.remoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewsEntity>
    ): MediatorResult {
        try {
            val offset = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val last = state.lastItemOrNull()
                    val keys = last?.let { remoteKeysDao.remoteKeysArticleId(it.id) }
                        ?: remoteKeysDao.getLastRemoteKey()
                    keys?.nextOffset ?: return Success(endOfPaginationReached = true)
                }
            }
            val response = dataSource.getArticles(limit = DEFAULT_PAGE_SIZE, offset = offset)

            when (val data = response) {
                is RemoteResponse.Error -> {
                    return Error(PagingSourceThrowable(data.exception.message))
                }

                is RemoteResponse.Success -> {
                    val result = data.result
                    val articles = result.results.map { it.toEntity() }

                    val nextOffset = result.next?.toUri()?.getQueryParameter("offset")
                        ?.toIntOrNull()
                    val prevOffset = result.previous?.toUri()?.getQueryParameter("offset")
                        ?.toIntOrNull()

                    db.withTransaction {
                        if (loadType == LoadType.REFRESH) {
                            remoteKeysDao.clearRemoteKeys()
                            articleDao.clearAll()
                        }
                        val keys = articles.map { article ->
                            RemoteKeys(
                                articleId = article.id,
                                prevOffset = prevOffset,
                                nextOffset = nextOffset
                            )
                        }
                        remoteKeysDao.insertAll(keys)
                        articleDao.insertAll(articles)
                        keys.forEach { println(it.toString()) }
                    }
                    return Success(endOfPaginationReached = (nextOffset == null))
                }
            }

        } catch (e: Exception) {
            Log.e("PagingRM", "Mediator Error ", e)
            return Error(e)
        }
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }

}