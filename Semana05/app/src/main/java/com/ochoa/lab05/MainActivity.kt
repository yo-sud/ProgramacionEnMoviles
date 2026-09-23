package com.ochoa.lab05

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.Modifier
import com.ochoa.lab05.navigation.AppNavigation
import com.ochoa.lab05.ui.theme.AppColors

private val Lab05ColorScheme = lightColorScheme(
    primary = AppColors.Primary,
    onPrimary = AppColors.OnPrimary,
    secondary = AppColors.PrimaryLight,
    background = AppColors.Background,
    surface = AppColors.CardBackground,
    surfaceVariant = AppColors.CardBackground
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme(colorScheme = Lab05ColorScheme) {
                Surface(modifier = Modifier) {
                    AppNavigation()
                }
            }
        }
    }
}