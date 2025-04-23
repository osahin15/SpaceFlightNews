package com.onursahin.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest

@Composable
fun NewsAsyncImage(modifier: Modifier = Modifier, imageUrl: String) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
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
}