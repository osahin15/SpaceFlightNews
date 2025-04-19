package com.onursahin.data.network

import com.onursahin.data.response.NewsResults
import retrofit2.http.GET

interface ApiService {

    @GET("v4/articles")
    suspend fun getArticles(): List<NewsResults>

}