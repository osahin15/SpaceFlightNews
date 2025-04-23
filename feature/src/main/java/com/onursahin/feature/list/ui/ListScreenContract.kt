package com.onursahin.feature.list.ui

import androidx.paging.PagingData
import com.onursahin.domain.model.News
import com.onursahin.ui.base.ViewEvent
import com.onursahin.ui.base.ViewSideEffect
import com.onursahin.ui.base.ViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ListScreenContract {

    sealed class Event : ViewEvent {
        data class OnSearchQueryChanged(val query: String) : Event()
        data class OnErrorSnackBar(
            val error: Throwable,
            val actionLabel: String? = null,
            val isDismiss: Boolean
        ) : Event()
    }

    data class State(
        val list: Flow<PagingData<News>> = flowOf(PagingData.empty()),
        val searchQuery: String? = ""
    ) : ViewState

    sealed class Effect : ViewSideEffect {

        sealed class Navigation : Effect() {}

        data class ShowSnackBar(
            val message: String,
            val actionLabel: String? = null,
            val isDismiss: Boolean = false
        ) : Effect()

    }
}