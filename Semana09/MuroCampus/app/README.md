# MuroCampus

Descripción: app Android (Jetpack Compose) que consume la API REST de JSONPlaceholder: lista publicaciones, muestra el detalle con comentarios, filtra por usuario y crea publicaciones.

## Estructura de paquetes
```
com.ochoa.tecsup/
├── MainActivity.kt
├── model/Modelos.kt
├── data/
│   ├── PostRepository.kt
│   └── remote/ (PostApi.kt, RetrofitClient.kt)
├── viewmodel/ (PostsViewModel.kt, PostsViewModelFactory.kt, UiState.kt)
└── ui/ (PantallaLista.kt, PantallaDetalle.kt, PantallaCrear.kt, Componentes.kt)
```

## Arquitectura MVVM + Repository
```
View (Pantallas) -> ViewModel (StateFlow<UiState>) -> PostRepository (Result) -> PostApi (Retrofit/OkHttp) -> JSONPlaceholder
```

## Capturas
| Lista | Detalle | Filtro |
|---|---|---|
| ![](capturas/lista.png) | ![](capturas/detalle.png) | ![](capturas/filtro.png) |

| Crear | Error de conexión |
|---|---|
| ![](capturas/crear.png) | ![](capturas/error.png) |

## Para pensar
1. ...
2. ...
   (las 6 preguntas del PDF con tus respuestas)