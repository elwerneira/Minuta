package com.example.minuta_nutricional.data

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri

/**Expone la minuta mediante una URI estándar.*/
class MinutaContentProvider : ContentProvider() {

    override fun onCreate(): Boolean = true

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        require(uri == CONTENT_URI) { "URI de recetas no reconocida: $uri" }

        return MatrixCursor(COLUMNAS).apply {
            recetasSemanales.forEachIndexed { indice, receta ->
                addRow(
                    arrayOf<Any?>(
                        indice,
                        receta.dia,
                        receta.nombre,
                        receta.ingredientes,
                        receta.preparacion,
                        receta.calorias,
                        receta.proteinas,
                        receta.carbohidratos,
                        receta.recomendacion
                    )
                )
            }
        }
    }

    override fun getType(uri: Uri): String {
        require(uri == CONTENT_URI) { "URI de recetas no reconocida: $uri" }
        return "vnd.android.cursor.dir/vnd.$AUTHORITY.receta"
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int = 0

    companion object {
        const val AUTHORITY = "com.example.minuta_nutricional.recetas"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/recetas")

        val COLUMNAS = arrayOf(
            "_id",
            "dia",
            "nombre",
            "ingredientes",
            "preparacion",
            "calorias",
            "proteinas",
            "carbohidratos",
            "recomendacion"
        )
    }
}
