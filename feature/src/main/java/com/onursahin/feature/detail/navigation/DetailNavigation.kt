package com.onursahin.feature.detail.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.onursahin.feature.detail.ui.DetailScreen
import com.onursahin.feature.detail.ui.DetailScreenContract
import com.onursahin.feature.detail.ui.DetailViewModel
import com.onursahin.ui.utils.NavigateHandler
import kotlinx.serialization.Serializable

@Serializable
data class DetailRoute(val id: Int)

fun NavGraphBuilder.detailScreen(navigateHandler: NavigateHandler) {
    composable<DetailRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<DetailRoute>()
        val viewModel = hiltViewModel<DetailViewModel>()
        LaunchedEffect(route) {
            viewModel.setEvent(DetailScreenContract.Event.GetDetailWithId(route.id))
        }
        DetailScreen(
            state = viewModel.viewState.value,
            onEvent = viewModel::setEvent,
            effect = viewModel.effect,
            navigate = navigateHandler
        )
    }
}