package com.example.minuta_nutricional.data.local

import android.content.Context
import com.example.minuta_nutricional.data.Receta

/** Guarda solo una preferencia de interfaz; Firebase Auth administra la sesión. */
class PreferenciasMinuta(context: Context) {
    private val prefs = context.getSharedPreferences("minuta_preferencias", Context.MODE_PRIVATE)

    fun guardarDiaSeleccionado(dia: String) {
        prefs.edit().putString(CLAVE_DIA_SELECCIONADO, dia).apply()
    }

    fun obtenerDiaSeleccionado(recetas: List<Receta>): String =
        resolverDiaInicial(recetas.map { it.dia }, prefs.getString(CLAVE_DIA_SELECCIONADO, null))

    private companion object {
        const val CLAVE_DIA_SELECCIONADO = "dia_seleccionado"
    }
}

/** Ignora preferencias antiguas si el administrador eliminó la receta de ese día. */
internal fun resolverDiaInicial(diasDisponibles: List<String>, diaGuardado: String?): String =
    when {
        diaGuardado != null && (diaGuardado == DIA_LIBRE || diaGuardado in diasDisponibles) -> diaGuardado
        else -> diasDisponibles.firstOrNull() ?: DIA_LIBRE
    }

internal const val DIA_LIBRE = "Día libre"
