package com.onursahin.domain.repository

import androidx.paging.PagingData
import com.onursahin.domain.UiResponse
import com.onursahin.domain.model.News
import kotlinx.coroutines.flow.Flow

interface SpaceNewsRepository {

    suspend fun getArticles(search : String?): Flow<PagingData<News>>

    suspend fun getArticleWithId(id: Int): UiResponse<News>
}