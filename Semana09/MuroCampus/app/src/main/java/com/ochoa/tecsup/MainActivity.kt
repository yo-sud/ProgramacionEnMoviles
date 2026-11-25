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

class MainActivity : ComponentActivity() {

    private val vm: PostsViewModel by viewModels {
        PostsViewModelFactory(PostRepositoryRemote(RetrofitClient.api))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MuroCampusTheme {
                Scaffold(topBar = { BarraSuperior() }) { padding ->
                    Box(Modifier.fillMaxSize().padding(padding)) {
                        PantallaLista(vm)
                    }
                }
            }
        }
    }
}