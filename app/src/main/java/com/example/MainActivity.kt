package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.DeekshiiViewModel
import com.example.ui.MainScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: DeekshiiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val systemIsDark = isSystemInDarkTheme()
            val userDarkPref by viewModel.isDarkTheme.collectAsStateWithLifecycle()
            val isDarkTheme = userDarkPref ?: systemIsDark

            MyApplicationTheme(darkTheme = isDarkTheme) {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}
