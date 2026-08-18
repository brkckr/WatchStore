package com.brkckr.watchstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.brkckr.watchstore.ui.WatchStoreScreen
import com.brkckr.watchstore.ui.theme.WatchStoreTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialize edge-to-edge display and set main content
        enableEdgeToEdge()
        setContent {
            WatchStoreTheme(dynamicColor = false) {
                WatchStoreScreen()
            }
        }
    }
}
