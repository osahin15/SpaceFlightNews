package com.onursahin.feature.list.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.onursahin.feature.list.ui.ListScreen
import com.onursahin.feature.list.ui.NewsArticlesViewModel
import com.onursahin.ui.utils.NavigateHandler
import kotlinx.serialization.Serializable

@Serializable
data object NewsListRoute

object GoBack

fun NavGraphBuilder.newsListScreen(navigateHandler: NavigateHandler) {
    composable<NewsListRoute> {
        val viewModel = hiltViewModel<NewsArticlesViewModel>()
        ListScreen(
            state = viewModel.viewState.value,
            onEvent = viewModel::setEvent,
            effect = viewModel.effect,
            navigate = navigateHandler
        )
    }
}