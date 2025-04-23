package com.onursahin.feature.list.ui

import AnimatedSearchBar
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.onursahin.domain.model.News
import com.onursahin.feature.detail.navigation.DetailRoute
import com.onursahin.feature.favorite.navigation.FavoriteNewsRoute
import com.onursahin.feature.list.navigation.GoBack
import com.onursahin.feature.list.ui.ListScreenContract.Event.OnErrorSnackBar
import com.onursahin.feature.list.ui.ListScreenContract.Event.OnSearchQueryChanged
import com.onursahin.ui.component.ArticleItem
import com.onursahin.ui.utils.DevicePreviews
import com.onursahin.ui.utils.NavigateHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    state: ListScreenContract.State,
    onEvent: (ListScreenContract.Event) -> Unit,
    effect: Flow<ListScreenContract.Effect>,
    navigate: NavigateHandler
) {
    val lazyPagingItems = state.list.collectAsLazyPagingItems()
    val snackBarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()
    var showSearchBar by remember { mutableStateOf(true) }

    var isSearchExpanded by remember { mutableStateOf(false) }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val searchWidth by animateDpAsState(
        targetValue = if (isSearchExpanded) screenWidth - 16.dp else 56.dp,
        animationSpec = tween(durationMillis = 300)
    )

    BackHandler {
        navigate.invoke(GoBack)
    }

    LaunchedEffect(listState) {
        var prevIndex = listState.firstVisibleItemIndex
        var prevOffset = listState.firstVisibleItemScrollOffset
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .collect { (index, offset) ->
                showSearchBar = if (index != prevIndex) {
                    index <= prevIndex
                } else {
                    offset <= prevOffset
                }
                prevIndex = index
                prevOffset = offset
            }
    }


    LaunchedEffect(effect) {
        effect.collect { effect ->
            when (effect) {
                is ListScreenContract.Effect.ShowSnackBar -> {
                    val result = snackBarHostState.showSnackbar(
                        effect.message,
                        actionLabel = effect.actionLabel,
                        withDismissAction = effect.isDismiss
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        lazyPagingItems.retry()
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AnimatedVisibility(
                    visible = showSearchBar,
                    enter = slideInVertically(initialOffsetY = { -it }, animationSpec = tween(300)),
                    exit = slideOutVertically(targetOffsetY = { -it }, animationSpec = tween(300))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AnimatedSearchBar(
                            isSearchExpanded = isSearchExpanded,
                            searchExpanded = { isSearchExpanded = it },
                            searchWidth = searchWidth,
                            query = state.searchQuery.orEmpty(),
                            setSearchQuery = { onEvent(OnSearchQueryChanged(it)) },
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        FavoriteButton(!isSearchExpanded) {
                            navigate(FavoriteNewsRoute)
                        }

                    }

                }
                PullToRefreshBox(
                    state = rememberPullToRefreshState(),
                    modifier = Modifier.weight(1f),
                    onRefresh = { lazyPagingItems.refresh() },
                    isRefreshing = lazyPagingItems.loadState.refresh is LoadState.Loading
                ) {
                    ArticleListContent(
                        listState = listState,
                        lazyPagingItems = lazyPagingItems,
                        onEvent = onEvent,
                        onItemClick = {
                            navigate.invoke(DetailRoute(it))
                        }
                    )
                }
            }
            lazyPagingItems.HandleLoadErrors {
                onEvent(it)
            }
        }
    }
}


@Composable
private fun ArticleListContent(
    listState: LazyListState,
    lazyPagingItems: LazyPagingItems<News>,
    onEvent: (ListScreenContract.Event) -> Unit,
    onItemClick: (Int) -> Unit
) {
    LazyColumn(state = listState) {
        items(
            count = lazyPagingItems.itemCount,
            key = { idx -> lazyPagingItems[idx]?.id ?: idx }
        ) { idx ->
            lazyPagingItems[idx]?.let { article ->
                ArticleItem(
                    article = article,
                    onItemClick = onItemClick
                )
            }
        }
        item {
            when {
                lazyPagingItems.loadState.append is LoadState.Loading -> {
                    Box(
                        Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

        }
    }

}

@Composable
fun LazyPagingItems<News>.HandleLoadErrors(onEvent: (ListScreenContract.Event) -> Unit) {
    val appendError = (loadState.append as? LoadState.Error)?.error
        ?: (loadState.mediator?.append as? LoadState.Error)?.error
    val refreshError = (loadState.refresh as? LoadState.Error)?.error
        ?: (loadState.mediator?.refresh as? LoadState.Error)?.error

    LaunchedEffect(appendError, refreshError) {
        when {
            appendError != null ->
                onEvent(OnErrorSnackBar(appendError, isDismiss = true))

            refreshError != null ->
                onEvent(
                    OnErrorSnackBar(
                        refreshError,
                        actionLabel = "Try again",
                        isDismiss = true
                    )
                )
        }
    }
}

@Composable
fun FavoriteButton(isVisible: Boolean, onClick: () -> Unit) {
    AnimatedVisibility(
        visible = isVisible
    ) {
        IconButton(
            onClick = {
                onClick.invoke()
            },
            modifier = Modifier
                .padding(end = 12.dp)
                .size(56.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(percent = 50)
                )
        ) {
            Icon(
                Icons.Outlined.FavoriteBorder,
                modifier = Modifier.size(28.dp),
                contentDescription = "Favorite"
            )
        }
    }

}


@DevicePreviews
@Composable
fun ListScreenPreview() {
    val fakeData = List(10) {
        News(
            id = it,
            title = "SpaceX launches third mid-inclination rideshare mission",
            url = "https://spacenews.com/spacex-launches-third-mid-inclination-rideshare-mission/",
            imageUrl = "https://i0.wp.com/spacenews.com/wp-content/uploads/2025/04/f9-bandwagon3.jpeg?fit=1024%2C768&quality=89&ssl=1",
            newsSite = "Example News Site",
            authors = emptyList(),
            featured = false,
            launches = emptyList(),
            events = emptyList(),
            updatedAt = "",
            summary = "SpaceX launched the third in its series of mid-inclination dedicated rideshare missions April 21, but with very few rideshare payloads on board.\\r\\nThe post SpaceX launches third mid-inclination rideshare mission appeared first on SpaceNews",
            publishedAt = ""
        )
    }

    val pagingData = PagingData.from(fakeData)
    val fakeDataFlow = MutableStateFlow(pagingData)
    ArticleListContent(
        listState = rememberLazyListState(),
        lazyPagingItems = fakeDataFlow.collectAsLazyPagingItems(),
        onItemClick = {},
        onEvent = {}
    )
}

