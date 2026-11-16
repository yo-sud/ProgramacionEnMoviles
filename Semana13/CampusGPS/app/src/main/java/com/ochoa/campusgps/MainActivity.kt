package com.ochoa.campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ochoa.campusgps.ui.PantallaMapa
import com.ochoa.campusgps.ui.theme.CampusGPSTheme
import com.ochoa.campusgps.viewmodel.CampusViewModelFactory
import com.ochoa.campusgps.viewmodel.MapaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CampusGPSTheme {
                val vm: MapaViewModel = viewModel(factory = CampusViewModelFactory(applicationContext))
                PantallaMapa(vm)
            }
        }
    }
}
