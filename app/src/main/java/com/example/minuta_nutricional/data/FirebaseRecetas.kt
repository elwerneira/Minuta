package com.example.minuta_nutricional.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object FirebaseRecetas {
    suspend fun consultar(permitirVacio: Boolean = false): Result<List<Receta>> = ejecutar {
        check(FirebaseAuth.getInstance().currentUser != null) { "Inicia sesión para consultar las recetas." }
        val datos = FirebaseDatabase.getInstance().getReference("recetas").get().await()
        val recetas = datos.children.map { registro ->
            val receta = checkNotNull(registro.getValue(Receta::class.java)) { "Hay una receta sin información." }
            check(registro.key == claveDia(receta.dia)) { "La clave de una receta no coincide con su día. Revisa el catálogo de Firebase." }
            receta
        }
        if (permitirVacio && recetas.isEmpty()) emptyList() else validarYOrdenarRecetas(recetas)
    }

    suspend fun esAdministrador(): Result<Boolean> = ejecutar {
        val uid = checkNotNull(FirebaseAuth.getInstance().currentUser?.uid) { "Inicia sesión para continuar." }
        FirebaseDatabase.getInstance().getReference("administradores").child(uid).get().await().value == true
    }

    suspend fun guardar(receta: Receta, crear: Boolean): Result<Unit> = ejecutar {
        validarYOrdenarRecetas(listOf(receta))
        check(esAdministrador().getOrThrow()) { "Solo una cuenta administradora puede modificar el catálogo." }
        val clave = claveDia(receta.dia)
        val referencia = FirebaseDatabase.getInstance().getReference("recetas").child(clave)
        if (!crear) {
            check(referencia.get().await().exists()) { "La receta ya no existe. Actualiza la lista." }
            referencia.setValue(receta).await()
            return@ejecutar Unit
        }
        // La transacción evita sobrescribir otra receta si dos personas crean el mismo día.
        suspendCancellableCoroutine<Unit> { continuacion ->
            referencia.runTransaction(object : Transaction.Handler {
                override fun doTransaction(datos: MutableData): Transaction.Result {
                    if (datos.value != null) return Transaction.abort()
                    datos.value = receta
                    return Transaction.success(datos)
                }

                override fun onComplete(error: DatabaseError?, confirmado: Boolean, datos: DataSnapshot?) {
                    if (!continuacion.isActive) return
                    when {
                        error != null -> continuacion.resumeWithException(error.toException())
                        !confirmado -> continuacion.resumeWithException(IllegalStateException(
                            "Este día ya tiene una receta. Actualiza la lista y edítala."
                        ))
                        else -> continuacion.resume(Unit)
                    }
                }
            }, false)
        }
    }

    suspend fun eliminar(dia: String): Result<Unit> = ejecutar {
        check(esAdministrador().getOrThrow()) { "Solo una cuenta administradora puede eliminar recetas." }
        FirebaseDatabase.getInstance().getReference("recetas").child(claveDia(dia)).removeValue().await()
        Unit
    }

    private suspend fun <T> ejecutar(operacion: suspend () -> T): Result<T> = try {
        Result.success(operacion())
    } catch (error: CancellationException) {
        throw error
    } catch (error: Exception) {
        Result.failure(error)
    }
}

internal val diasSemana = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")

internal fun claveDia(dia: String): String = when (dia) {
    "Lunes" -> "lunes"
    "Martes" -> "martes"
    "Miércoles" -> "miercoles"
    "Jueves" -> "jueves"
    "Viernes" -> "viernes"
    "Sábado" -> "sabado"
    "Domingo" -> "domingo"
    else -> error("Selecciona un día válido.")
}

// El orden en Firebase depende de las claves, no del orden de la semana.
internal fun validarYOrdenarRecetas(recetas: List<Receta>): List<Receta> {
    check(recetas.isNotEmpty()) { "No hay recetas disponibles en el catálogo de Firebase." }
    check(recetas.map { it.dia }.toSet().size == recetas.size) { "Hay más de una receta para el mismo día." }
    check(recetas.all {
        it.dia in diasSemana && it.nombre.isNotBlank() && it.nombre.length <= 120 &&
            it.ingredientes.length <= 2000 && it.preparacion.length <= 4000 && it.recomendacion.length <= 500 && it.ingredientes.isNotBlank() &&
            it.preparacion.isNotBlank() && it.recomendacion.isNotBlank() &&
            it.calorias in 1..10000 && it.proteinas in 0..1000 && it.carbohidratos in 0..1000
    }) { "Revisa los días, textos y nutrientes del catálogo de Firebase." }
    return recetas.sortedBy { diasSemana.indexOf(it.dia) }
}
