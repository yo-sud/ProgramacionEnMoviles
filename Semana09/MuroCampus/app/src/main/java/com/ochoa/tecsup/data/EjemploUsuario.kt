package com.ochoa.tecsup.data



// Modelo (en model/Modelos.kt):
//   @Serializable
//   data class Usuario(val id: Int, val name: String, val email: String)
//
// Interface de Retrofit (iría en data/remote/):
//   interface UsuarioApi {
//       @GET("users")
//       suspend fun listar(): List<Usuario>
//   }
//
// Contrato y Repository (la excepción NUNCA escapa: se convierte en Result.failure):
//   interface UsuarioRepository {
//       suspend fun listar(): Result<List<Usuario>>
//   }
//
//   class UsuarioRepositoryRemote(private val api: UsuarioApi) : UsuarioRepository {
//       override suspend fun listar(): Result<List<Usuario>> =
//           try {
//               Result.success(api.listar())
//           } catch (e: CancellationException) {
//               throw e                      // nunca se traga la cancelación
//           } catch (e: Exception) {
//               Result.failure(e)
//           }
//   }

