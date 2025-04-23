package com.onursahin.feature.favorite.ui

import androidx.lifecycle.viewModelScope
import com.onursahin.domain.usecase.GetAllFavoritesUseCase
import com.onursahin.ui.base.BaseComposeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteNewsViewModel @Inject constructor(
    private val getAllFavoritesUseCase: GetAllFavoritesUseCase
) : BaseComposeViewModel<FavoriteNewsScreenContract.Event, FavoriteNewsScreenContract.State, FavoriteNewsScreenContract.Effect>() {


    init {
        favoritesUiFlow()
    }

    private fun favoritesUiFlow() {
        viewModelScope.launch {
            setState {
                copy(isLoading = true)
            }
            getAllFavoritesUseCase().collect { favoriteNews ->
                setState {
                    copy(list = favoriteNews, isLoading = false)
                }
            }
        }
    }

    override fun setInitialState(): FavoriteNewsScreenContract.State {
        return FavoriteNewsScreenContract.State(
            list = emptyList(),
            isLoading = false
        )
    }

    override fun handleEvents(event: FavoriteNewsScreenContract.Event) {
    }
}