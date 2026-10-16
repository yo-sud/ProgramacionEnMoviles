package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.cafecampus.CafeApp

/** Crea los ViewModels inyectándoles el Repository (y las preferencias cuando las necesitan). */
class CafeViewModelFactory(private val app: CafeApp) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val vm: ViewModel = when {
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> MenuViewModel(app.repo)
            modelClass.isAssignableFrom(SesionViewModel::class.java) -> SesionViewModel(app.repo, app.prefs)
            modelClass.isAssignableFrom(PedidosClienteViewModel::class.java) -> PedidosClienteViewModel(app.repo)
            modelClass.isAssignableFrom(PanelAdminViewModel::class.java) -> PanelAdminViewModel(app.repo)
            modelClass.isAssignableFrom(PedidosAdminViewModel::class.java) -> PedidosAdminViewModel(app.repo)
            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
        return vm as T
    }
}