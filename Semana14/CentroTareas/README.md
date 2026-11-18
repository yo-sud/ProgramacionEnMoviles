# CentroTareas

Descripción: app que ejecuta tareas largas sin bloquear el hilo principal (corrutinas, ViewModel, Repository).

## Observación del Paso 1
(qué pasa con la pantalla, por qué no se ve "Trabajando...", qué mostraría Android con 10 s)

## Estructura de paquetes
```
com.ochoa.centrotareas/
├── MainActivity.kt
├── model/      Estados.kt
├── data/       TareasRepository.kt, TareasRepositoryLocal.kt
├── viewmodel/  TareasViewModel.kt, TareasViewModelFactory.kt
└── ui/         PantallaTareas.kt, theme/
```

## Arquitectura MVVM
```
View (PantallaTareas) --eventos--> ViewModel (TareasViewModel) --StateFlow--> View
ViewModel --viewModelScope.launch--> TareasRepository --> TareasRepositoryLocal
```

## Capturas
1. Congelada o tarea con progreso
2. Cancelada
3. Primos
4. Una por una vs A la vez
5. Error

## Para pensar
1. 
2. 
3. 
4. 
5. 
