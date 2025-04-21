package com.onursahin.feature.list.ui

import AnimatedSearchBar
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.onursahin.domain.model.News
import com.onursahin.feature.list.viewmodel.NewsArticlesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(vm: NewsArticlesViewModel, onItemClick: (Int) -> Unit) {
    val query by vm.searchQuery.collectAsStateWithLifecycle()
    val lazyPagingItems = vm.articlesUiFlow.collectAsLazyPagingItems()
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()

    var isSearchExpanded by remember { mutableStateOf(false) }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val searchWidth by animateDpAsState(
        targetValue = if (isSearchExpanded) screenWidth - 16.dp else 56.dp,
        animationSpec = tween(durationMillis = 300)
    )

    LaunchedEffect(lazyPagingItems.loadState.refresh) {
        val refreshState = lazyPagingItems.loadState.refresh
        if (refreshState is LoadState.Error) {
            val result = snackbarHostState.showSnackbar(
                message = "İnternet bağlantısı yok.",
                actionLabel = "Yeniden Dene"
            )
            if (result == SnackbarResult.ActionPerformed) {
                lazyPagingItems.retry()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column {
                AnimatedSearchBar(
                    isSearchExpanded = isSearchExpanded,
                    searchExpanded = { isSearchExpanded = it },
                    searchWidth = searchWidth,
                    query = query.orEmpty(),
                    setSearchQuery = vm::setSearchQuery,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                ArticleListContent(
                    listState = listState,
                    lazyPagingItems = lazyPagingItems,
                    onItemClick = onItemClick
                )
            }
            if (lazyPagingItems.loadState.refresh is LoadState.Loading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }

    }
}

@Composable
private fun SearchField(
    isSearchExpanded: Boolean,
    searchExpanded: (Boolean) -> Unit,
    searchWidth: Dp,
    query: String?,
    setSearchQuery: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .width(searchWidth)
            .height(56.dp)
    ) {
        if (!isSearchExpanded) {
            IconButton(
                onClick = { searchExpanded(true) },
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(12.dp)
                    .border(1.dp, Color.Black)
            ) {
                Icon(Icons.Default.Search, contentDescription = "Open search")
            }
        } else {
            OutlinedTextField(
                value = query.orEmpty(),
                onValueChange = { setSearchQuery(it) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { searchExpanded(false) }) {
                        Icon(Icons.Default.Close, contentDescription = "Close search")
                    }
                },
                label = { Text("Filter Title or Summary Text") },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
private fun ArticleListContent(
    listState: LazyListState,
    lazyPagingItems: LazyPagingItems<News>,
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
            when (lazyPagingItems.loadState.append) {
                is LoadState.Loading -> {
                    Box(
                        Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is LoadState.Error -> {
                    Text("Yüklenirken hata oldu")
                }

                else -> {}
            }
        }
    }
}

@Composable
private fun ArticleItem(
    article: News,
    onItemClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        onClick = {
            onItemClick.invoke(article.id)
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(article.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Loading -> {
                        Box(
                            Modifier
                                .matchParentSize()
                                .background(Color.LightGray)
                        )
                    }

                    is AsyncImagePainter.State.Error -> {
                        Box(
                            Modifier
                                .matchParentSize()
                                .background(Color.Red.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }

                    else -> {
                        SubcomposeAsyncImageContent()
                    }
                }
            }
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = article.summary,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = article.publishedAt,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}