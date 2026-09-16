
val productos = remember { mutableStateListOf<Producto>() }

¿Por qué la lista se declara con val y aún así podemos agregarle elementos?

Porque val afecta a la referencia de la variable, no a su contenido ya que es una "mutableStateListaOf"