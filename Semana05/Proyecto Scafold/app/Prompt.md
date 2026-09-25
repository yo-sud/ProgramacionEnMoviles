PROMPTS.md — Documentación de prompts usados (Fase 2: mejora-ia)

Contexto de la mejora
Se agregó la funcionalidad de cancelar una reserva desde la pantalla "Reservas",
con un `AlertDialog` de confirmación antes de eliminarla, tal como sugiere el enunciado
de la tarea (ejemplo: "cancelar una cita/reserva con un AlertDialog de confirmación").

---

Prompt 1
le pedí:

"Necesito agregar un botón de 'Cancelar reserva' en cada tarjeta de mi pantalla de
Reservas en Jetpack Compose, que muestre un AlertDialog pidiendo confirmación antes
de eliminar la reserva de la lista. Uso remember/mutableStateOf, sin ViewModel."

Lo que generó la IA fue:

Una primera versión del `AlertDialog` con `confirmButton` y `dismissButton`, y una
variable de estado para controlar qué reserva se quiere cancelar.

tuve que corregir: 

- La IA proponía guardar el índice (`Int`) de la reserva en la lista en vez del
  objeto `Reserva` completo, lo cual era frágil (si la lista cambiaba de orden,
  se podía cancelar la reserva equivocada). Lo cambié para guardar la `Reserva`
  completa (`var reservaACancelar by remember { mutableStateOf<Reserva?>(null) }`).
- Agregué la condición para que el botón "Cancelar reserva" solo aparezca si el
  estado es "Confirmada" (no tiene sentido cancelar una clase ya "Completada").

---

Prompt 2
le pedí:

"¿Cómo conecto ese AlertDialog con mi lista de reservas que vive en MainActivity,
para que al confirmar la cancelación se elimine realmente de la lista, si estoy
usando SnapshotStateList y NavHost con Navigation Compose?"

Lo que generó la IA fue:

Sugirió pasar una función 'onCancelarReserva: (Reserva) -> Unit' como parámetro
de 'ReservasScreen', y en el 'NavGraph', implementarla como 'listaReservas.remove(reserva)'.

tuve que corregir: 

- Nada mayor; el patrón de "state hoisting" que propuso coincidía con el que ya
  usábamos para agregar reservas desde Confirmación, así que solo adapté nombres
  de variables para mantener consistencia con el resto del proyecto.

---

 Prompt 3 
le pedí:
"Después de reservar o cancelar una reserva, el botón 'Inicio' de mi bottomBar
deja de funcionar (no navega, no hay crash). Uso NavHost plano con Navigation
Compose, sin grafos anidados. ¿Por qué pasa esto?"

Lo que generó la IA fue:
Explicó que el patrón 'popUpTo(startDestination.id){saveState=true} + restoreState=true'
está pensado para grafos anidados por pestaña, y que en un 'NavHost' plano puede
dejar el back stack en un estado inconsistente. Sugirió reemplazarlo por
'popUpTo(ruta){inclusive=false} + launchSingleTop=true', sin 'saveState'/'restoreState'.

tuve que corregir: 
- Apliqué el cambio tanto en el 'NavGraph' (botón "Ver mis reservas") como en el
  'bottomBar' de 'MainActivity', ya que ambos usaban el patrón problemático —
  la IA solo había señalado uno de los dos lugares.
- Agregué una validación extra ('if (currentDestination?.route != pantalla.ruta)')
  para evitar navegaciones redundantes al tocar una pestaña ya activa.