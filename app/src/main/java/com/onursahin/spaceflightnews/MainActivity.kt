package com.onursahin.spaceflightnews

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.onursahin.data.base.GENERIC_ERROR_MESSAGE
import com.onursahin.spaceflightnews.navigation.NewsNavHost
import com.onursahin.spaceflightnews.ui.theme.SpaceFlightNewsTheme
import com.onursahin.ui.base.ErrorManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handleError()
        setContent {
            SpaceFlightNewsTheme {
                Scaffold{ innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    ) {
                        NewsNavHost()
                    }
                }
            }
        }
    }

    fun handleError() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                ErrorManager.errorFlow.collect {
                    val dialog = AlertDialog.Builder(this@MainActivity)
                        .setTitle("Error")
                        .setMessage(it.message ?: GENERIC_ERROR_MESSAGE)
                        .setPositiveButton("OK") { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }
                        .setCancelable(true)
                        .create()
                    dialog.show()
                }
            }
        }
    }
}