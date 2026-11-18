package com.ochoa.centrotareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ochoa.centrotareas.data.TareasRepositoryLocal
import com.ochoa.centrotareas.ui.PantallaTareas
import com.ochoa.centrotareas.ui.theme.CentroTareasTheme
import com.ochoa.centrotareas.viewmodel.TareasViewModel
import com.ochoa.centrotareas.viewmodel.TareasViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CentroTareasTheme {
                val vm: TareasViewModel = viewModel(
                    factory = TareasViewModelFactory(TareasRepositoryLocal())
                )
                PantallaTareas(vm)
            }
        }
    }
}
