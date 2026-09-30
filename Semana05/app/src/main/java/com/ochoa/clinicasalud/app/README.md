
 Contexto de la mejora
Se agregó la funcionalidad de cancelar una cita médica desde la pantalla "Mis citas",
con un AlertDialog de confirmación antes de eliminarla, tal como sugiere el enunciado
de la tarea (ejemplo: "cancelar una cita/reserva con un AlertDialog de confirmación"). 
 
Prompt 1
Qué le pedí:

"Necesito agregar un botón de 'Cancelar cita' en cada tarjeta de mi pantalla de
Mis Citas en Jetpack Compose, que muestre un AlertDialog pidiendo confirmación antes
de eliminar la cita de la lista. Uso remember/mutableStateOf, sin ViewModel."

Lo que generó:

Una primera versión del AlertDialog con confirmButton y dismissButton, y una
variable de estado para controlar qué cita se quiere cancelar.

Tuve que corregir:
La IA proponía guardar el índice (Int) de la cita en la lista en vez del objeto Cita completo. Lo cambié para guardar la Cita completa (var citaACancelar by remember { mutableStateOf<Cita?>(null) }), evitando cancelar la cita equivocada si la lista cambia de orden.
Agregué la condición para que el botón "Cancelar cita" solo aparezca si el estado es "Confirmada" (no tiene sentido cancelar una cita ya "Completada").

---

Prompt 2
Qué le pedí:

"¿Cómo conecto ese AlertDialog con mi lista de citas que vive en MainActivity,
para que al confirmar la cancelación se elimine realmente de la lista, si estoy
usando SnapshotStateList y NavHost con Navigation Compose?"

Lo que generó la IA:

Sugirió pasar una función onCancelarCita: (Cita) -> Unit como parámetro
de MisCitasScreen, y en el NavGraph, implementarla como listaCitas.remove(cita).

Tuve que corregir:

Nada mayor; el patrón de "state hoisting" coincidía con el que ya usábamos para agregar citas desde Confirmación, así que solo adapté nombres de variables para mantener consistencia con el resto del proyecto.

---

Prompt 3
Qué le pedí:

"Estoy usando ModalNavigationDrawer envolviendo un Scaffold con NavHost plano
 en Jetpack Compose. ¿Qué patrón de popUpTo debo usar al
navegar desde los items del drawer para evitar que el back stack quede inconsistente?"

Lo que generó la IA:

Recomendó evitar saveState = true / restoreState = true en un NavHost plano, y usar
en su lugar popUpTo(ruta){ inclusive = false } + launchSingleTop = true.

Tuve que corregir:

Apliqué este patrón desde el principio, tanto en la navegación del drawer (MainActivity) como en la navegación desde Confirmación hacia Inicio (NavGraph), para evitar desde el inicio un bug de navegación que ya había experimentado en un proyecto anterior con un patrón de bottomBar similar.

## Preguntas
¿Cómo llega el médico elegido hasta Confirmación?
la id del medico va en la ruta "perfil_medico/{medicoId}" y cada apntalla lo recupera buscando en medicosDeEjemplo

¿Por qué el drawer se declara envolviendo el Scaffold?
Porque necesita deslizarse por encima de la pantalla incluyendo al topBar, no solo del área de contenido.

