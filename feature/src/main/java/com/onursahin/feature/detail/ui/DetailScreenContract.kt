package com.onursahin.feature.detail.ui

import com.onursahin.domain.model.News
import com.onursahin.ui.base.ViewEvent
import com.onursahin.ui.base.ViewSideEffect
import com.onursahin.ui.base.ViewState

class DetailScreenContract {

    sealed class Event : ViewEvent {

        data class OnAddFavorite(val news: News?) : Event()
        data class GetIsFavorite(val newsId : Int) : Event()
        data class GetDetailWithId(val id: Int) : Event()

    }

    data class State(
        val news: News? = null,
        val isLoadings: Boolean = false,
        val isFavorite : Boolean = false
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