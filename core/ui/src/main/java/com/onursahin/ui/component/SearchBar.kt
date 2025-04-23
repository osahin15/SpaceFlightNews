import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun AnimatedSearchBar(
    isSearchExpanded: Boolean,
    searchExpanded: (Boolean) -> Unit,
    searchWidth: Dp,
    query: String,
    setSearchQuery: (String) -> Unit,
    modifier: Modifier = Modifier,
    searchBgColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    iconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    unfocusedTextColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    cursorColor: Color = MaterialTheme.colorScheme.primary,
    focusedBorderColor: Color = Color.Transparent,
    animationDurationMillis: Int = 300,
    searchHeight: Dp = 56.dp,
    cornerRadiusPercent: Int = 50
) {
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(isSearchExpanded) {
        if (isSearchExpanded) {
            delay(animationDurationMillis / 2L)
            try {
                focusRequester.requestFocus()
            } catch (e: Exception) {
                println("Focus request failed: ${e.message}")
            }
        }
    }

    Surface(
        modifier = modifier
            .height(searchHeight)
            .width(searchWidth)
            .background(searchBgColor, RoundedCornerShape(percent = cornerRadiusPercent))
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                if (!isSearchExpanded) {
                    searchExpanded(true)
                }
            },
        shape = RoundedCornerShape(percent = cornerRadiusPercent),
        color = searchBgColor.copy(0.3f),
        contentColor = textColor
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = isSearchExpanded,
                enter = fadeIn(animationSpec = tween(delayMillis = animationDurationMillis / 2)),
                exit = fadeOut(animationSpec = tween(durationMillis = animationDurationMillis / 2))
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = setSearchQuery,
                    modifier = Modifier
                        .fillMaxSize()
                        .focusRequester(focusRequester),
                    textStyle = TextStyle(color = textColor),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { searchExpanded(false) }) {
                            Icon(Icons.Default.Close, contentDescription = "Close search")
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(percent = cornerRadiusPercent),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        cursorColor = cursorColor,
                        focusedIndicatorColor = focusedBorderColor,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedTextColor = textColor,
                        unfocusedTextColor = unfocusedTextColor
                    ),
                    placeholder = {
                        Text(
                            text = "Search in the title or summary",
                        )
                    },
                    label = null
                )
            }

            AnimatedVisibility(
                visible = !isSearchExpanded,
                enter = fadeIn(),
                exit = fadeOut(animationSpec = tween(durationMillis = animationDurationMillis / 3))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = iconColor,
                        modifier = Modifier.size(searchHeight * 0.5f)
                    )
                }
            }
        }
    }
}