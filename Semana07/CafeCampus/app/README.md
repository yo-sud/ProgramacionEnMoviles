# CafeCampus

App de pedidos para la cafetería del campus (TECSUP · Programación en Móviles · Semana 8).
Dos perfiles en el mismo dispositivo: el **Estudiante** pide y sigue su pedido, y el
**Administrador** gestiona pedidos y menú. Ambos comparten la misma base de datos Room, así que
cuando el administrador cambia el estado de un pedido, el estudiante lo ve reflejado al instante.

**Tecnología:** Kotlin · Jetpack Compose · Material 3 (sin XML) · Room 2.6.1 · DataStore · Navigation Compose
**Patrón:** MVVM + Repository (interface)

**Datos de prueba (ficticios):** `juan@campus.test` / `1234` (Estudiante) · `admin@cafe.test` / `admin` (Administrador)

## Estructura de paquetes

```
com.tecsup.cafecampus
├── MainActivity.kt
├── CafeApp.kt                  crea la base, el repository y las preferencias
├── data
│   ├── local
│   │   ├── entity              Producto, Usuario, Pedido, DetallePedido
│   │   ├── dao                 ProductoDao, UsuarioDao, PedidoDao
│   │   ├── relation            PedidoConDetalle
│   │   ├── CafeDatabase.kt
│   │   └── DatosIniciales.kt
│   ├── repository              CafeRepository (interface), CafeRepositoryRoom
│   └── prefs                   PreferenciasStore (DataStore)
├── model                       Rol, EstadoPedido, ItemCarrito
├── viewmodel                   Menu, Sesion, PedidosCliente, PanelAdmin, PedidosAdmin, MenuAdmin
└── ui
    ├── theme                   Color, Type, Theme
    ├── components              AppTopBar, CafeButton, CafeTextField, CafeCard, EstadoChip, ...
    ├── navigation              Rutas, NavCafe
    ├── cliente                 Splash, Login, Menu, DetalleProducto, ConfirmarPedido, Seguimiento, MisPedidos
    └── admin                   Panel, PedidosAdmin, DetallePedidoAdmin, MenuAdmin, FormularioProducto, Ajustes
```

## Arquitectura MVVM

```
VIEW (Composable)
  │ eventos (clics) ▲ collectAsState()
  ▼                 │
VIEWMODEL (StateFlow)
  │ llama           ▲ Flow / resultados
  ▼                 │
CafeRepository (interface)
  │ la implementa
  ▼
CafeRepositoryRoom ──► DAO ──► SQLite (cafecampus.db)
DataStore (solo preferencias) ──► ViewModel
```

## Capturas

| Estudiante | | |
|---|---|---|
| ![Menú](![Screenshot_20261018_010527.png](../../../../../../../Pictures/img/Screenshot_20261018_010527.png)) | ![Seguimiento](docs/capturas/cliente-seguimiento.png![Screenshot_20261018_010634.png](../../../../../../../Pictures/img/Screenshot_20261018_010634.png)) | ![Mis pedidos](![Screenshot_20261018_010702.png](../../../../../../../Pictures/img/Screenshot_20261018_010702.png)) |

| Administrador | | |
|---|---|---|
| ![Panel](![Screenshot_20261018_010724.png](../../../../../../../Pictures/img/Screenshot_20261018_010724.png)) | ![Pedidos](![Screenshot_20261018_010750.png](../../../../../../../Pictures/img/Screenshot_20261018_010750.png)) | ![Menú](![Screenshot_20261018_010800.png](../../../../../../../Pictures/img/Screenshot_20261018_010800.png)) |

## Para pensar

1. **¿Por qué el Repository es una interface?**
Porque se puede cambiar la implementacion sin tocar viewModel ni Views
2. **¿Por qué `crearPedido` es `suspend` y `observarPedidosCliente` es `Flow`?**
crearPedido es una accion que culmina y da un resultado, mientras que observarPedidosCLiente solo muestra datos actualizados.
3. **¿Qué pasaría si dos perfiles usaran bases de datos distintas?**
Los datos no se comparten y tanto el admin como el estudiante no verían la misma informacion.
4. **¿Qué debería cambiar si mañana los datos viven en un servidor?**
Se crea otra implementacion del Repository para que pueda llamar al servidor, y ViewModels quedan intactos.