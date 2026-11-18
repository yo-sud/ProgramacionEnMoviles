# CampusGPS

App Android que obtiene tu ubicación, la muestra en un mapa y te dice qué lugar del campus tienes más cerca. Kotlin + Jetpack Compose + Material 3, MVVM + Repository, Fused Location y Maps Compose.

## Estructura de paquetes
```
com.ochoa.campusgps/
├── MainActivity.kt
├── model/        Modelos.kt
├── data/         UbicacionRepository(+Google), LugaresRepository(+Local)
├── util/         Distancia.kt, Formato.kt
├── viewmodel/    MapaViewModel.kt, CampusViewModelFactory.kt
└── ui/           PantallaMapa, MapaUbicacion, ListaLugares, MensajeError, theme/
```

## Arquitectura MVVM
```
View (PantallaMapa) --evento--> ViewModel (MapaViewModel) --StateFlow--> View
ViewModel --> UbicacionRepository --> UbicacionRepositoryGoogle (Fused)
ViewModel --> LugaresRepository   --> LugaresRepositoryLocal
```

## Capturas
| Permiso | Mapa con ubicación | Marcadores |
|---|---|---|
| ![](capturas/1-permiso.png) | ![](capturas/2-mapa.png) | ![](capturas/3-marcadores.png) |

| Lista ordenada | Error |
|---|---|
| ![](capturas/4-lista.png) | ![](capturas/5-error.png) |

## Para pensar
1. **¿Por qué Fused está en el Repository?** Para que el ViewModel no dependa de Google ni de un `Context`; se puede cambiar o probar con otra implementación.
2. **Permiso vs. clave de API:** el permiso lo da el usuario para leer el GPS; la clave autoriza a la app a usar Maps. Sin permiso no hay ubicación; sin clave el mapa sale en blanco.
3. **Clave en `local.properties`:** ese archivo no se sube a git. Si se sube por error, se borra la clave y se crea otra en Google Cloud.
4. **Distancia en el ViewModel (`util/`):** el Composable solo dibuja; calcular ahí rompe MVVM y se repite en cada recomposición.
5. **Permiso negado dos veces:** Android deja de mostrar el cuadro. La app muestra el error con **Abrir ajustes** para activarlo manualmente.