package com.example.minuta_nutricional.data

import android.content.Context

/** Consulta las recetas a través del ContentResolver de Android. */
fun Context.consultarRecetas(): List<Receta> {
    val recetas = mutableListOf<Receta>()

    contentResolver.query(
        MinutaContentProvider.CONTENT_URI,
        MinutaContentProvider.COLUMNAS,
        null,
        null,
        null
    )?.use { cursor ->
        val dia = cursor.getColumnIndexOrThrow("dia")
        val nombre = cursor.getColumnIndexOrThrow("nombre")
        val ingredientes = cursor.getColumnIndexOrThrow("ingredientes")
        val preparacion = cursor.getColumnIndexOrThrow("preparacion")
        val calorias = cursor.getColumnIndexOrThrow("calorias")
        val proteinas = cursor.getColumnIndexOrThrow("proteinas")
        val carbohidratos = cursor.getColumnIndexOrThrow("carbohidratos")
        val recomendacion = cursor.getColumnIndexOrThrow("recomendacion")

        while (cursor.moveToNext()) {
            recetas += Receta(
                dia = cursor.getString(dia),
                nombre = cursor.getString(nombre),
                ingredientes = cursor.getString(ingredientes),
                preparacion = cursor.getString(preparacion),
                calorias = cursor.getInt(calorias),
                proteinas = cursor.getInt(proteinas),
                carbohidratos = cursor.getInt(carbohidratos),
                recomendacion = cursor.getString(recomendacion)
            )
        }
    }

    return recetas
}
