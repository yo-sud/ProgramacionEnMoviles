# AvisosCampus

Aplicación Android de laboratorio para gestionar avisos locales del campus. Está desarrollada con Kotlin, Jetpack Compose y Material 3, siguiendo una separación MVVM con Repository.

## Funciones

- Solicita el permiso de notificaciones en Android 13 o posterior y muestra su estado.
- Crea el canal de notificaciones **Avisos** al iniciar la aplicación.
- Muestra avisos locales y abre la aplicación al tocarlos.
- Programa un recordatorio de 10 segundos con opción para cancelarlo.
- Puede escuchar cambios del modo avión mientras el interruptor está activado.
- Presenta un historial en memoria con el título, texto, origen y hora de cada aviso.
- Informa cuando falta el permiso y permite abrir los ajustes de la aplicación.

## Estructura del proyecto

```text
app/src/main/java/com/tecsup/avisoscampus/
├── data/          # Repository en memoria y flujo de avisos
├── model/         # Aviso, origen y estado del permiso
├── notificacion/  # Canal, comprobación del permiso y publicación
├── receiver/      # Receptor dinámico del modo avión
├── ui/            # Pantalla principal e historial Compose
├── viewmodel/     # Estado de pantalla, recordatorio y eventos
├── AvisosApp.kt   # Inicialización del canal
└── MainActivity.kt
```

## Arquitectura

El `AvisosViewModel` decide qué aviso registrar en el Repository y emite un evento mediante `SharedFlow`. La pantalla observa ese evento y delega la publicación a `Notificador`, que recibe el `Context`. El ViewModel no utiliza `Context`, `NotificationManager` ni `BroadcastReceiver`.

Los avisos se guardan en memoria; se reinicia el historial al cerrar el proceso.

## Capturas

Las capturas de la aplicación aún están pendientes. Cuando estén disponibles, guárdalas en `docs/capturas/` y enlázalas aquí.

## Requisitos

- Android Studio con Android SDK 35.
- JDK 11.
- Android 7.0 (API 24) o posterior.

Abre el proyecto `AvisosCampus` en Android Studio y ejecuta la configuración `app` en un emulador o dispositivo.
