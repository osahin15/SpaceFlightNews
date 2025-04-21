package com.onursahin.data.network

import com.onursahin.data.response.NewsResultsResponse
import com.onursahin.data.response.PagedResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("v4/articles")
    suspend fun getArticles(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("search") search: String? = null
    ): PagedResponse<NewsResultsResponse>

    @GET("v4/articles/{id}")
    suspend fun getArticleDetailWithId(@Path("id") id: Int): NewsResultsResponse

}