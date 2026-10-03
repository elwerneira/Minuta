package com.example.minuta_nutricional.data.local

import android.content.Context

data class DatosSesion(val uid: String, val correo: String)

/** Copia local de datos básicos; nunca sustituye la autenticación de Firebase. */
class SessionManager(context: Context, nombreArchivo: String = "user_session") {
    private val prefs = context.getSharedPreferences(nombreArchivo, Context.MODE_PRIVATE)

    fun guardarSesion(uid: String, correo: String) {
        require(uid.isNotBlank() && correo.isNotBlank())
        prefs.edit()
            .putString("uid", uid)
            .putString("correo", correo)
            .apply()
    }

    fun obtenerSesionSiCoincide(uidFirebase: String?): DatosSesion? {
        val uid = prefs.getString("uid", null)?.takeIf { it.isNotBlank() }
        val correo = prefs.getString("correo", null)?.takeIf { it.isNotBlank() }
        val datos = if (uid != null && correo != null) DatosSesion(uid, correo) else null
        return sesionCoincidente(datos, uidFirebase)
    }

    fun limpiarSesion() {
        prefs.edit().clear().apply()
    }
}

/** El UID local solo se usa si coincide con la identidad vigente de Firebase Auth. */
internal fun sesionCoincidente(datos: DatosSesion?, uidFirebase: String?): DatosSesion? =
    datos?.takeIf { uidFirebase != null && it.uid == uidFirebase }
