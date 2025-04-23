package com.onursahin.spaceflightnews.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.onursahin.feature.detail.navigation.detailScreen
import com.onursahin.feature.list.navigation.GoBack
import com.onursahin.feature.list.navigation.NewsListRoute
import com.onursahin.feature.list.navigation.newsListScreen
import com.onursahin.ui.utils.NavigateHandler

@Composable
fun NewsNavHost(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NewsListRoute,
        modifier = modifier
    ) {

        val navigateHandler: NavigateHandler = { route ->
            when (route) {
                is GoBack -> navController.navigateUp()
                else -> navController.navigate(route)
            }
        }
        newsListScreen(navigateHandler)
        detailScreen(navigateHandler)
    }
}