package com.tecsup.pokemon

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.tecsup.pokemon.ui.navigation.AppNav
import com.tecsup.pokemon.ui.theme.PokemonTheme

class MainActivity : ComponentActivity() {
    private val pedirPermiso =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* si se niega, la app sigue normal */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 33) pedirPermiso.launch(Manifest.permission.POST_NOTIFICATIONS)
        val container = (application as PokemonApp).container
        setContent { PokemonTheme { AppNav(container) } }
    }
}
