package com.onursahin.feature.favorite.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.onursahin.feature.favorite.ui.FavoriteNewsScreen
import com.onursahin.feature.favorite.ui.FavoriteNewsViewModel
import com.onursahin.ui.utils.NavigateHandler
import kotlinx.serialization.Serializable

@Serializable
data object FavoriteNewsRoute

fun NavGraphBuilder.favoriteNewsScreen(navigateHandler: NavigateHandler) {
    composable<FavoriteNewsRoute> {
        val viewModel = hiltViewModel<FavoriteNewsViewModel>()
        FavoriteNewsScreen(
            state = viewModel.viewState.value,
            onEvent = viewModel::setEvent,
            effect = viewModel.effect,
            navigate = navigateHandler
        )
    }
}