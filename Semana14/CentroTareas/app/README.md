# CentroTareas

App Android con Kotlin, Jetpack Compose y Material 3 (MVVM + Repository) que demuestra cómo mantener la pantalla fluida usando corrutinas. Tiene tres tarjetas: tarea con progreso y cancelación, conteo de números primos y dos cargas simuladas (secuencial y en paralelo con `async`), con manejo de errores y reintento.

## Árbol de paquetes

```
com.ochoa.centrotareas/
├── MainActivity.kt
├── model/
│   └── Estados.kt              (PrimosUi, CargaUi)
├── data/
│   ├── TareasRepository.kt     (interface)
│   └── TareasRepositoryLocal.kt
├── viewmodel/
│   ├── TareasViewModel.kt
│   └── TareasViewModelFactory.kt
└── ui/
    ├── PantallaTareas.kt
    └── theme/
```

## Arquitectura MVVM + Repository

```
View (PantallaTareas)
   |  eventos (botones)          ^  collectAsState()
   v                             |
ViewModel (TareasViewModel) --- StateFlow --->
   |  viewModelScope.launch { ... }
   v
TareasRepository (interface) ---> TareasRepositoryLocal
   (cargas simuladas y contar primos)
```

## Observación del Paso 1

(Escribe aquí tu observación: qué pasó con la pantalla, por qué no se veía "Trabajando..." y qué crees que mostraría Android con una espera de 10 segundos.)

## Capturas

| Tarea con progreso | Cancelada |
|---|---|
| ![Progreso](capturas/1-progreso.png) | ![Cancelada](capturas/2-cancelada.png) |

| Primos | Una por una vs. a la vez |
|---|---|
| ![Primos](capturas/3-primos.png) | ![Cargas](capturas/4-cargas.png) |

| Error simulado |
|---|
| ![Error](capturas/5-error.png) |

## Para pensar

1. **¿Por qué no se veía "Trabajando..." en el Paso 1?** Porque `Thread.sleep` bloqueaba el hilo principal, que es el que dibuja la pantalla. El cambio de texto se hizo, pero no hubo ningún fotograma para mostrarlo hasta que terminó la espera.
2. **¿Diferencia entre `Thread.sleep` y `delay`?** `Thread.sleep` bloquea el hilo completo. `delay` es una función `suspend`: suspende solo la corrutina y libera el hilo para otro trabajo, además de ser cancelable.
3. **¿Por qué `Dispatchers.Default` para los primos y `Dispatchers.IO` para la red?** Default tiene tantos hilos como núcleos de CPU y sirve para cálculo intensivo. IO tiene un grupo grande de hilos pensado para tareas que esperan (red, disco), donde muchos hilos pasan el tiempo bloqueados.
4. **¿Qué pasa con la corrutina si el usuario cierra la pantalla? ¿Qué papel cumple `viewModelScope`?** `viewModelScope` está atado al ciclo de vida del ViewModel: sobrevive a las rotaciones y se cancela solo cuando el ViewModel se destruye (al salir definitivamente de la pantalla). Así no quedan corrutinas huérfanas ni fugas de memoria.
5. **¿Por qué `async` tarda 3 s y no 5 s? ¿Qué pasa si una falla?** Porque las dos llamadas corren al mismo tiempo y el total es el de la más lenta (máx(2 s, 3 s) = 3 s), no la suma. Dentro de `coroutineScope`, si una falla se cancela la otra y la excepción sube al `try/catch`, que muestra el estado de error.