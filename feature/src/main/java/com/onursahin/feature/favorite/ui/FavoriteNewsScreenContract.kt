package com.onursahin.feature.favorite.ui

import com.onursahin.domain.model.News
import com.onursahin.ui.base.ViewEvent
import com.onursahin.ui.base.ViewSideEffect
import com.onursahin.ui.base.ViewState

class FavoriteNewsScreenContract {

    sealed class Event : ViewEvent

    data class State(
        val list: List<News> = emptyList<News>(),
        val isLoading : Boolean = false
    ) : ViewState

    sealed class Effect : ViewSideEffect {

        sealed class Navigation : Effect()
    }
}