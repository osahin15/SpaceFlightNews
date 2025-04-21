package com.onursahin.data.network

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.onursahin.data.base.RemoteResponse
import com.onursahin.data.response.NewsResultsResponse
import javax.inject.Inject
import javax.inject.Named

class SearchNewsPagingSource @Inject constructor(
    private val dataSource: SpaceNewsDataSource,
    @Named("searchQuery") private val query: String
) : PagingSource<Int, NewsResultsResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsResultsResponse> {
        return try {
            val offset = params.key ?: 0
            val response =
                dataSource.getArticles(limit = DEFAULT_PAGE_SIZE, offset = offset, search = query)
            if (response is RemoteResponse.Error) {
                return LoadResult.Error(response.exception)
            }
            val result = (response as RemoteResponse.Success).result

            val nextOffset = result.next
                ?.let { Uri.parse(it).getQueryParameter("offset") }
                ?.toIntOrNull()
            val prevOffset = result.previous
                ?.let { Uri.parse(it).getQueryParameter("offset") }
                ?.toIntOrNull()

            LoadResult.Page(
                data = result.results,
                prevKey = prevOffset,
                nextKey = nextOffset
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NewsResultsResponse>): Int? {
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor) ?: return null
        return page.prevKey?.plus(state.config.pageSize)
            ?: page.nextKey?.minus(state.config.pageSize)
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }
}