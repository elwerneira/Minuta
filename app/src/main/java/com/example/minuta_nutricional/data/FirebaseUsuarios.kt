package com.example.minuta_nutricional.data

import com.example.minuta_nutricional.data.local.SessionManager
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await

// Perfil Firebase.
data class PerfilUsuario(
    val nombre: String = "",
    val correo: String = "",
    val tipoAlimentacion: String = "Sin preferencia",
    val objetivo: String = "Mantener peso",
    val aceptaRecomendaciones: Boolean = false
)

object FirebaseUsuarios {
    private fun verificarConfiguracion() {
        check(FirebaseApp.getApps(androidContext()).isNotEmpty()) {
            "Falta configurar Firebase. Agrega google-services.json y sincroniza el proyecto."
        }
    }

    // FirebaseApp puede consultarse sin guardar una Activity ni un Context en este objeto.
    private fun androidContext() = com.example.minuta_nutricional.MinutaApplication.context

    private val auth: FirebaseAuth
        get() {
            verificarConfiguracion()
            return FirebaseAuth.getInstance()
        }

    private fun perfiles(): com.google.firebase.database.DatabaseReference {
        verificarConfiguracion()
        check(!FirebaseApp.getInstance().options.databaseUrl.isNullOrBlank()) {
            "Falta configurar Realtime Database. Crea la base y descarga google-services.json actualizado."
        }
        return FirebaseDatabase.getInstance().getReference("usuarios")
    }

    suspend fun registrar(correo: String, clave: String, perfil: PerfilUsuario): Result<PerfilUsuario> =
        ejecutar {
            val referenciaPerfiles = perfiles()
            val usuario = checkNotNull(auth.createUserWithEmailAndPassword(correo.trim(), clave).await().user)
            val perfilConfirmado = perfil.copy(correo = checkNotNull(usuario.email))
            try {
                referenciaPerfiles.child(usuario.uid).setValue(perfilConfirmado).await()
            } catch (error: Exception) {
                if (error is CancellationException) throw error
                // Evita dejar una cuenta recién creada sin su perfil si falla la escritura.
                try {
                    usuario.delete().await()
                } catch (rollback: Exception) {
                    if (rollback is CancellationException) throw rollback
                    throw IllegalStateException("La cuenta se creó, pero no se guardó el perfil. Revisa la conexión y las reglas de Firebase antes de volver a ingresar.", error)
                }
                throw IllegalStateException("No se guardó el perfil. Revisa la conexión y las reglas de Firebase e intenta registrarte nuevamente.", error)
            }
            SessionManager(androidContext()).guardarSesion(usuario.uid, perfilConfirmado.correo)
            perfilConfirmado
        }

    suspend fun ingresar(correo: String, clave: String): Result<PerfilUsuario> = ejecutar {
        val referenciaPerfiles = perfiles()
        val usuario = checkNotNull(auth.signInWithEmailAndPassword(correo.trim(), clave).await().user)
        try {
            val perfil = checkNotNull(referenciaPerfiles.child(usuario.uid).get().await().getValue(PerfilUsuario::class.java)) {
                "No se encontró el perfil de esta cuenta."
            }
            SessionManager(androidContext()).guardarSesion(usuario.uid, checkNotNull(usuario.email))
            perfil
        } catch (error: Exception) {
            auth.signOut()
            SessionManager(androidContext()).limpiarSesion()
            throw error
        }
    }

    /** Solo prellena el correo cuando la cuenta local coincide con Firebase Auth. */
    fun correoSesionRecordada(): String {
        if (FirebaseApp.getApps(androidContext()).isEmpty()) return ""
        val uidFirebase = FirebaseAuth.getInstance().currentUser?.uid
        return SessionManager(androidContext()).obtenerSesionSiCoincide(uidFirebase)?.correo.orEmpty()
    }

    suspend fun recuperar(correo: String): Result<Unit> = ejecutar {
        auth.sendPasswordResetEmail(correo.trim()).await()
        Unit
    }

    suspend fun consultarPerfil(): Result<PerfilUsuario> = ejecutar {
        val usuario = checkNotNull(auth.currentUser) { "Inicia sesión para consultar tu perfil." }
        checkNotNull(perfiles().child(usuario.uid).get().await().getValue(PerfilUsuario::class.java)) {
            "No se encontró el perfil de esta cuenta."
        }
    }

    suspend fun guardarPerfil(nombre: String, tipoAlimentacion: String, objetivo: String): Result<Unit> = ejecutar {
        val usuario = checkNotNull(auth.currentUser) { "Inicia sesión para actualizar tu perfil." }
        check(nombre.trim().isNotEmpty() && nombre.trim().length <= 120) { "Ingresa un nombre de entre 1 y 120 caracteres." }
        check(tipoAlimentacion in listOf("Sin preferencia", "Vegetariana", "Vegana")) { "Selecciona un tipo de alimentación válido." }
        check(objetivo in listOf("Bajar de peso", "Mantener peso", "Aumentar masa muscular")) { "Selecciona un objetivo válido." }
        // Modifica únicamente los campos editables: conserva el correo y la aceptación.
        perfiles().child(usuario.uid).updateChildren(
            mapOf("nombre" to nombre.trim(), "tipoAlimentacion" to tipoAlimentacion, "objetivo" to objetivo)
        ).await()
        Unit
    }

    suspend fun eliminarCuenta(clave: String): Result<Unit> = ejecutar {
        check(clave.isNotEmpty()) { "Ingresa tu contraseña para confirmar." }
        val usuario = checkNotNull(auth.currentUser) { "Inicia sesión para eliminar tu cuenta." }
        val correo = checkNotNull(usuario.email) { "No se encontró el correo de esta cuenta." }
        // Comprueba la identidad antes de borrar cualquier dato.
        usuario.reauthenticate(EmailAuthProvider.getCredential(correo, clave)).await()
        val referencia = perfiles().child(usuario.uid)
        val respaldo = referencia.get().await().value
        // Completa o compensa la operación aunque se cierre la pantalla.
        withContext(NonCancellable) {
            referencia.removeValue().await()
            try {
                usuario.delete().await()
            } catch (error: Exception) {
                try {
                    if (respaldo != null) referencia.setValue(respaldo).await()
                } catch (restauracion: Exception) {
                    throw IllegalStateException("La cuenta no se eliminó, pero no se pudo restaurar el perfil. Revisa la conexión y contacta al responsable del proyecto.", restauracion)
                }
                throw error
            }
            auth.signOut()
            SessionManager(androidContext()).limpiarSesion()
        }
        Unit
    }

    fun cerrarSesion() {
        if (FirebaseApp.getApps(androidContext()).isNotEmpty()) FirebaseAuth.getInstance().signOut()
        SessionManager(androidContext()).limpiarSesion()
    }

    private suspend fun <T> ejecutar(operacion: suspend () -> T): Result<T> = try {
        Result.success(operacion())
    } catch (error: CancellationException) {
        throw error
    } catch (error: Exception) {
        Result.failure(error)
    }
}

fun Throwable.mensajeFirebase(): String = when (this) {
    is FirebaseNetworkException -> "No hay conexión con Firebase. Revisa internet e intenta nuevamente."
    is FirebaseAuthException -> when (errorCode) {
        "ERROR_EMAIL_ALREADY_IN_USE" -> "Este correo ya está registrado."
        "ERROR_WEAK_PASSWORD" -> "La contraseña debe tener al menos 6 caracteres."
        "ERROR_INVALID_EMAIL" -> "Ingresa un correo electrónico válido."
        "ERROR_USER_DISABLED" -> "Esta cuenta está deshabilitada."
        "ERROR_OPERATION_NOT_ALLOWED" -> "Habilita el acceso por correo y contraseña en Firebase."
        "ERROR_TOO_MANY_REQUESTS" -> "Demasiados intentos. Espera un momento e intenta nuevamente."
        else -> "No fue posible autenticar la cuenta. Revisa el correo y la contraseña."
    }
    is IllegalStateException -> message ?: "Revisa la configuración de Firebase."
    else -> "No fue posible completar la operación. Revisa tu conexión e intenta nuevamente."
}
