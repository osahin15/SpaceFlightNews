package com.onursahin.feature.detail.ui

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.onursahin.domain.model.News
import com.onursahin.domain.model.Socials
import com.onursahin.domain.util.orZero
import com.onursahin.feature.R
import com.onursahin.feature.list.navigation.GoBack
import com.onursahin.ui.component.EmptyState
import com.onursahin.ui.component.NewsAsyncImage
import com.onursahin.ui.component.SocialMediaIcon
import com.onursahin.ui.utils.DevicePreviews
import com.onursahin.ui.utils.NavigateHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    state: DetailScreenContract.State,
    onEvent: (DetailScreenContract.Event) -> Unit,
    effect: Flow<DetailScreenContract.Effect>,
    navigate: NavigateHandler
) {
    val news = state.news
    val context = LocalContext.current
    val customTabsIntent = remember {
        CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setUrlBarHidingEnabled(true)
            .build()
    }

    val snackBarState = remember { SnackbarHostState() }
    var favoriteButtonVisible by remember { mutableStateOf(true) }

    BackHandler {
        navigate(GoBack)
    }

    LaunchedEffect(news) {
        onEvent(DetailScreenContract.Event.GetIsFavorite(news?.id.orZero()))
    }

    LaunchedEffect(effect) {
        effect.collect { effect ->
            when (effect) {
                is DetailScreenContract.Effect.ShowSnackBar -> {
                    snackBarState.showSnackbar(
                        effect.message,
                        actionLabel = effect.actionLabel,
                        withDismissAction = effect.isDismiss
                    )
                }
            }
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Space Flight News")
                },
                navigationIcon = {
                    IconButton(onClick = { navigate(GoBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (favoriteButtonVisible) {
                        IconButton(onClick = {
                            onEvent(DetailScreenContract.Event.OnAddFavorite(news))
                        }) {
                            Icon(
                                imageVector = if (state.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Favorite"
                            )
                        }
                    }
                    IconButton(onClick = {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, news?.url ?: "")
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(shareIntent, null))
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Share"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (state.isLoadings) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (news != null) {
            favoriteButtonVisible = true
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(state = ScrollState(0)),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    NewsAsyncImage(
                        imageUrl = news.imageUrl.toString(),
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomStart)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.8f)
                                    ),
                                    startY = 0f,
                                    endY = Float.POSITIVE_INFINITY
                                )
                            )
                    ) {
                        Text(
                            text = news.title,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        )
                    }
                }
                SocialMediaContent(news.authors.firstOrNull()?.socials)
                SummaryContent(news = news) {
                    customTabsIntent.launchUrl(context, it.toUri())
                }

            }
        } else {
            favoriteButtonVisible = false
            EmptyState()
        }
    }
}

@Composable
fun SocialMediaContent(socials: Socials?) {
    if (socials != null) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SocialMediaIcon(
                url = socials.youtube,
                iconResId = R.drawable.youtube_color_icon
            )
            SocialMediaIcon(
                url = socials.instagram,
                iconResId = R.drawable.black_instagram_icon
            )
            SocialMediaIcon(
                url = socials.linkedin,
                iconResId = R.drawable.linkedin_app_icon
            )
            SocialMediaIcon(
                url = socials.x,
                iconResId = R.drawable.x_social_media_logo_icon
            )
            SocialMediaIcon(
                url = socials.bluesky,
                iconResId = R.drawable.bluesky_icon
            )
            SocialMediaIcon(
                url = socials.mastodon,
                iconResId = R.drawable.mastodon_black_icon
            )

        }
    }
}

@Composable
fun SummaryContent(news: News, customTabsIntent: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        shape = CardDefaults.elevatedShape,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp)
    ) {
        Text(
            text = news.summary,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = news.newsSite,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Blue,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable {
                        if (news.url.isNotBlank()) {
                            customTabsIntent.invoke(news.url)
                        }

                    }
            )
            Text(
                text = news.publishedAt,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Authors: " + news.authors.joinToString(",") { it.name },
            style = MaterialTheme.typography.labelSmall,
            fontStyle = FontStyle.Italic,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .align(Alignment.End)

        )
    }

}


@DevicePreviews
@Composable
fun DetailScreenPreview() {
    DetailScreen(
        state = DetailScreenContract.State(
            news = News(
                id = 1,
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
            ), isLoadings = false
        ),
        onEvent = {},
        effect = flowOf()
    ) { }
}

@DevicePreviews
@Composable
fun DetailScreenEmptyPreview() {
    DetailScreen(
        state = DetailScreenContract.State(
            news = null, isLoadings = false
        ),
        onEvent = {},
        effect = flowOf()
    ) { }
}