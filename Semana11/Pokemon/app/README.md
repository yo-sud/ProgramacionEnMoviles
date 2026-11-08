Pokédex – Laboratorio 11-12 (Programación en Móviles)

App Android en Kotlin con Jetpack Compose y Material 3. Consulta Pokémon desde PokeAPI, guarda los datos en el celular, permite marcar favoritos y tiene login con dos roles (admin y usuario).

Autor: YAMIL AARON OCHOA QUISPE
Curso: Programación en Móviles
Tema: Pokémon 
Variante: tipo fire, water y grass
Paquete: com.tecsup.pokemon

Funciones	Usuario	Admin
Ingresar con correo y contraseña	Sí	Sí
Ver lista y detalle	Sí	Sí
Marcar favoritos y ver la pantalla Favoritos	Sí	Sí
Cerrar sesión	Sí	Sí
Panel Admin: totales, sincronizar y limpiar caché	No	Sí

Cómo compilar
Clona el repositorio y abre la carpeta del proyecto con Android Studio.
Coloca tu google-services.json en app/ (ver la sección de Firebase; el archivo no está en el repositorio).
Haz Sync de Gradle y ejecuta la app en un emulador con Google Play o en un dispositivo.

Para cambiar la variante, edita TIPO_POKEMON en app/src/main/java/com/tecsup/pokemon/Config.kt 

APIs usadas (públicas, sin clave):

Lista: https://pokeapi.co/api/v2/type/{tipo}
Detalle: https://pokeapi.co/api/v2/pokemon/{nombre}