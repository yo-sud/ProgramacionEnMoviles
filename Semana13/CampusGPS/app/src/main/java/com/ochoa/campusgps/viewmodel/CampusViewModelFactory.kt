package com.ochoa.campusgps.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ochoa.campusgps.data.LugaresRepositoryLocal
import com.ochoa.campusgps.data.UbicacionRepositoryGoogle

class CampusViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MapaViewModel(
            UbicacionRepositoryGoogle(context.applicationContext),
            LugaresRepositoryLocal()
        ) as T
}
