package com.ochoa.tecsup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.ochoa.tecsup.ui.BarraSuperior
import com.ochoa.tecsup.ui.theme.MuroCampusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO (Paso 4): prueba temporal con lifecycleScope.launch y RetrofitClient.api.listar() (se borra antes del commit)

        setContent {
            MuroCampusTheme {
                Scaffold(topBar = { BarraSuperior() }) { padding ->
                    Box(Modifier.fillMaxSize().padding(padding)) {
                        // TODO (Pasos 6-9): aquí van PantallaLista, PantallaDetalle y PantallaCrear
                    }
                }
            }
        }
    }
}
