package com.onursahin.data.network

import com.onursahin.data.base.getResult
import javax.inject.Inject

class SpaceNewsDataSource @Inject constructor(val apiService: ApiService) {

    suspend fun getArticles(limit: Int, offset: Int, search: String? = null) = getResult {
        apiService.getArticles(limit = limit, offset = offset, search = search)
    }

    suspend fun getArticleDetailWithId(id: Int) = getResult {
        apiService.getArticleDetailWithId(id)
    }
}