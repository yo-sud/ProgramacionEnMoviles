package com.tecsup.pokemon.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import com.tecsup.pokemon.domain.model.Rol
import com.tecsup.pokemon.domain.model.Sesion
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {

    override suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion> = try {
        val uid = auth.signInWithEmailAndPassword(correo, clave).await().user!!.uid
        Result.success(leerSesion(uid, correo))
    } catch (e: Exception) {
        Result.failure(traducir(e))
    }

    private fun traducir(e: Exception): Exception = when (e) {
        is FirebaseAuthInvalidCredentialsException,
        is FirebaseAuthInvalidUserException -> AuthException("Correo o contraseña incorrectos")
        is FirebaseNetworkException -> AuthException("Sin conexión a internet")
        else -> AuthException("No se pudo iniciar sesión")
    }

    override suspend fun sesionActual(): Sesion? {
        val u = auth.currentUser ?: return null
        return try { leerSesion(u.uid, u.email ?: "") } catch (e: Exception) { null }
    }

    override fun cerrarSesion() = auth.signOut()

    private suspend fun leerSesion(uid: String, correo: String): Sesion {
        val doc = db.collection("usuarios").document(uid).get().await()
        val rol = if (doc.getString("rol") == "admin") Rol.ADMIN else Rol.USUARIO
        return Sesion(uid, correo, rol)
    }
}
