package com.onursahin.spaceflightnews

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.onursahin.data.network.ApiService
import com.onursahin.feature.list.ui.ListScreen
import com.onursahin.feature.list.viewmodel.NewsArticlesViewModel
import com.onursahin.spaceflightnews.ui.theme.SpaceFlightNewsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpaceFlightNewsTheme {
                Scaffold { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    ) {
                        ListScreen(vm = hiltViewModel()){}
                    }
                }
            }
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    fun Greeting(apiService: ApiService) {

        val title = remember { mutableStateOf("") }
        rememberCoroutineScope().launch {
            title.value =
                apiService.getArticles(
                    limit = 20,
                    offset = 1
                ).results.firstOrNull()?.title.orEmpty()
        }
        Text(
            text = title.value,
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        SpaceFlightNewsTheme {

        }
    }
}