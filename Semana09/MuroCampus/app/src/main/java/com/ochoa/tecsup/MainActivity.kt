package com.ochoa.tecsup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.ochoa.tecsup.data.PostRepositoryRemote
import com.ochoa.tecsup.data.remote.RetrofitClient
import com.ochoa.tecsup.ui.BarraSuperior
import com.ochoa.tecsup.ui.PantallaLista
import com.ochoa.tecsup.ui.theme.MuroCampusTheme
import com.ochoa.tecsup.viewmodel.PostsViewModel
import com.ochoa.tecsup.viewmodel.PostsViewModelFactory
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.ochoa.tecsup.ui.PantallaDetalle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import com.ochoa.tecsup.ui.PantallaCrear

class MainActivity : ComponentActivity() {

    private val vm: PostsViewModel by viewModels {
        PostsViewModelFactory(PostRepositoryRemote(RetrofitClient.api))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MuroCampusTheme {
                var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
                var creando by rememberSaveable { mutableStateOf(false) }

                fun volver() {
                    idSeleccionado = null
                    if (creando) {
                        creando = false
                        vm.limpiarPublicacion()
                    }
                }

                BackHandler(enabled = idSeleccionado != null || creando) { volver() }

                Scaffold(
                    topBar = {
                        BarraSuperior(
                            titulo = when {
                                creando -> "Nueva publicación"
                                idSeleccionado != null -> "Publicación"
                                else -> "MuroCampus"
                            },
                            onAtras = if (idSeleccionado != null || creando) ({ volver() }) else null
                        )
                    },
                    floatingActionButton = {
                        if (idSeleccionado == null && !creando) {
                            FloatingActionButton(onClick = { creando = true }) {
                                Icon(Icons.Default.Add, contentDescription = "Crear publicación")
                            }
                        }
                    }
                ) { padding ->
                    Box(Modifier.fillMaxSize().padding(padding)) {
                        val id = idSeleccionado
                        when {
                            creando -> PantallaCrear(vm)
                            id != null -> PantallaDetalle(vm, onReintentar = { vm.abrir(id) })
                            else -> PantallaLista(vm, onPostClick = { vm.abrir(it); idSeleccionado = it })
                        }
                    }
                }
            }
        }
    }
}