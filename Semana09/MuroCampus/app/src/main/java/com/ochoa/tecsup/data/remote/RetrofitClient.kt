package com.ochoa.tecsup.data.remote

// ------------------------------------------------------------------------------------------
// Archivo GUIADO (Paso 4). Está comentado porque Retrofit y OkHttp llegan con el Paso 1,
// y PostApi la creas tú en el Paso 3.
//
// En el Paso 4: descomenta el archivo (Ctrl + /), agrega los imports que pida el IDE y
// completa los 2 TODO. El TODO 3 va en MainActivity (prueba temporal).
// ------------------------------------------------------------------------------------------

// object RetrofitClient {
//     private val json = Json { ignoreUnknownKeys = true }
//
//     private val cliente = OkHttpClient.Builder()
//         // TODO 1: addInterceptor(HttpLoggingInterceptor().apply { level = ... BODY })
//         .build()
//
//     val api: PostApi by lazy {
//         Retrofit.Builder()
//             .baseUrl("https://jsonplaceholder.typicode.com/")   // termina con /
//             .client(cliente)
//             // TODO 2: addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
//             .build()
//             .create(PostApi::class.java)
//     }
// }
