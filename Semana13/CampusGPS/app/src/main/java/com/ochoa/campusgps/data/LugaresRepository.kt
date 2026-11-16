package com.ochoa.campusgps.data

import com.ochoa.campusgps.model.Lugar

interface LugaresRepository {
    fun obtenerLugares(): List<Lugar>
}
