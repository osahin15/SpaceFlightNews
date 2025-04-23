package com.onursahin.feature.favorite.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.onursahin.feature.detail.navigation.DetailRoute
import com.onursahin.feature.list.navigation.GoBack
import com.onursahin.ui.component.ArticleItem
import com.onursahin.ui.utils.NavigateHandler
import kotlinx.coroutines.flow.Flow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteNewsScreen(
    state: FavoriteNewsScreenContract.State,
    onEvent: (FavoriteNewsScreenContract.Event) -> Unit,
    effect: Flow<FavoriteNewsScreenContract.Effect>,
    navigate: NavigateHandler
) {
    val listState = rememberLazyListState()
    BackHandler {
        navigate(GoBack)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Favorites News")
                },
                navigationIcon = {
                    IconButton(onClick = { navigate(GoBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {}
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = listState
        ) {
            items(state.list.size) { idx ->
                val item = state.list[idx]
                ArticleItem(item) {
                    navigate(DetailRoute(it))
                }
            }
        }
    }

}