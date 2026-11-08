package com.tecsup.pokemon.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.pokemon.AppContainer
import com.tecsup.pokemon.ui.admin.AdminViewModel
import com.tecsup.pokemon.ui.detalle.DetalleViewModel
import com.tecsup.pokemon.ui.favoritos.FavoritosViewModel
import com.tecsup.pokemon.ui.lista.ListaViewModel
import com.tecsup.pokemon.ui.login.LoginViewModel

class AppViewModelFactory(private val c: AppContainer) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        ListaViewModel::class.java -> ListaViewModel(c.itemRepository, c.notificador)
        DetalleViewModel::class.java -> DetalleViewModel(c.itemRepository)
        FavoritosViewModel::class.java -> FavoritosViewModel(c.itemRepository)
        AdminViewModel::class.java -> AdminViewModel(c.itemRepository, c.notificador)
        LoginViewModel::class.java -> LoginViewModel(c.authRepository)
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    } as T
}
