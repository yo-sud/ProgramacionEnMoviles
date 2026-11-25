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

class MainActivity : ComponentActivity() {

    private val vm: PostsViewModel by viewModels {
        PostsViewModelFactory(PostRepositoryRemote(RetrofitClient.api))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MuroCampusTheme {
                var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
                BackHandler(enabled = idSeleccionado != null) { idSeleccionado = null }

                Scaffold(topBar = {
                    BarraSuperior(
                        titulo = if (idSeleccionado == null) "MuroCampus" else "Publicación",
                        onAtras = if (idSeleccionado != null) ({ idSeleccionado = null }) else null
                    )
                }) { padding ->
                    Box(Modifier.fillMaxSize().padding(padding)) {
                        val id = idSeleccionado
                        if (id == null) {
                            PantallaLista(vm, onPostClick = { vm.abrir(it); idSeleccionado = it })
                        } else {
                            PantallaDetalle(vm, onReintentar = { vm.abrir(id) })
                        }
                    }
                }
            }
        }
    }
}