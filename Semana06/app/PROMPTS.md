
### Prompt 1: 

Lo que pedí:
Quiero que la opción "Favoritos" del DropdownMenu de cada producto marque o desmarque el producto, y que ese estado esté disponible para el NavigationDrawer. Dame el código completo, un commit a la vez.

lo que generó la IA:
- Elevó el estado a AppNavegacion con mutableStateListOf<Int>() (ids de productos favoritos).
- Cambió TarjetaProducto para recibir esFavorito y onToggleFavorito.
- Cambió PantallaInicio para recibir favoritos y onToggleFavorito, y pasarlos a cada tarjeta.
- El texto del ítem cambia entre "Favoritos" y "Quitar de favoritos", y el ícono entre Favorite y FavoriteBorder.

lo que tuve que corregir:
Se agrega dependencias faltantes y errores de identacion dentro del codigo
### Prompt 2: 

Lo que pedí:
Agrega un badge con el contador de favoritos en el ítem "Favoritos" del drawer.

Que generó la IA
- Agregó el parámetro cantidadFavoritos: Int a AppDrawer.
- Usó el parámetro badge de NavigationDrawerItem con un Badge { Text(...) }, visible solo si el contador es mayor que 0.
- En AppNavegacion pasó cantidadFavoritos = favoritos.size.
- Reescribió AppDrawer.kt completo, con el encabezado de usuario y el color onPrimary para las iniciales.

Qué tuve que corregir:
Errores de identacion en el codigo, e imports desactualizados

### Prompt 3: 

lo que pedí:
Arma el PROMPTS.md, las reflexiones y el resumen final.

Qué generó la IA:
Plantilla de este archivo y borrador de las reflexiones.

Que tuve que corregir:
Errores de identacion dentro del codigo y borrar imports innecesarios

--

### Preguntas de reflexión

1. ¿Por qué el DropdownMenu se declara dentro de un Box junto al ícono que lo activa?
El DropdownMenu es un popup que se posiciona en relación con su contenedor padre. Al ponerlo en el mismo Box que el IconButton, el menú se ancla justo debajo del ícono de esa tarjeta. Si estuviera en otro lugar, se abriría en una posición que no corresponde al producto tocado.

2. ¿Qué diferencia de alcance hay entre las opciones del DropdownMenu y las del NavigationDrawer?
Las opciones del DropdownMenu actúan sobre un solo producto (marcarlo como favorito, compartirlo, reportarlo) y su estado expanded vive en cada tarjeta. Las del NavigationDrawer cambian de destino y afectan a toda la app: su estado (drawerState) y la ruta actual viven en AppNavegacion.

3. ¿Cómo estructuré el código para que el contador del drawer se entere de lo que pasa en el DropdownMenu?
Apliqué state hoisting. La lista de ids favoritos (mutableStateListOf<Int>()) vive en AppNavegacion, el ancestro común de la lista y del drawer. La tarjeta recibe esFavorito y un callback onToggleFavorito, y el drawer recibe favoritos.size. Como la lista es un estado observable, cuando cambia, Compose recompone el badge solo.


