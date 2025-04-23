package com.onursahin.ui.component

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri

@Composable
fun SocialMediaIcon(url: String = "", iconResId: Int) {
    val context = LocalContext.current
    AnimatedVisibility(visible = url.isNotEmpty()) {
        IconButton(onClick = {
            val intent = Intent(Intent.ACTION_VIEW, url?.toUri())
            context.startActivity(intent)
        }) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(32.dp)
            )
        }
    }

}