package com.ochoa.tecsupstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ochoa.tecsupstore.ui.theme.TecsupstoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TecsupstoreTheme {
                AppNavegacion()
            }
        }
    }
}