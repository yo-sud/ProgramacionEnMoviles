package com.ochoa.Tarea03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.ochoa.Tarea03.ui.theme.Tarea03Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tarea03Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Tarea03(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}