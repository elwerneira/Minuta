package com.example.minuta_nutricional.data

import org.junit.Assert.assertEquals
import org.junit.Test

class FirebaseRecetasTest {
    @Test fun ordenaPorDiaSinDependerDeLasClaves() {
        val resultado = validarYOrdenarRecetas(recetasSemanales.reversed())
        assertEquals(listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes"), resultado.map { it.dia })
    }
    @Test(expected = IllegalStateException::class)
    fun rechazaCatalogoVacio() { validarYOrdenarRecetas(emptyList()) }

    @Test(expected = IllegalStateException::class)
    fun rechazaDiasDuplicados() { validarYOrdenarRecetas(listOf(recetasSemanales[0], recetasSemanales[0])) }

    @Test(expected = IllegalStateException::class)
    fun rechazaInformacionIncompleta() { validarYOrdenarRecetas(listOf(Receta(dia = "Lunes"))) }

    @Test(expected = IllegalStateException::class)
    fun rechazaNutrientesNegativos() { validarYOrdenarRecetas(listOf(recetasSemanales[0].copy(proteinas = -1))) }

    @Test(expected = IllegalStateException::class)
    fun rechazaDiaDesconocido() { validarYOrdenarRecetas(listOf(recetasSemanales[0].copy(dia = "Otro"))) }

    @Test fun clavesDeDiasSonEstablesSinTildes() {
        assertEquals(listOf("lunes", "martes", "miercoles", "jueves", "viernes", "sabado", "domingo"), diasSemana.map { claveDia(it) })
    }

    @Test(expected = IllegalStateException::class)
    fun rechazaClaveDeDiaInvalida() { claveDia("Día libre") }

    @Test(expected = IllegalStateException::class)
    fun rechazaNombreDemasiadoLargo() { validarYOrdenarRecetas(listOf(recetasSemanales[0].copy(nombre = "a".repeat(121)))) }

    @Test(expected = IllegalStateException::class)
    fun rechazaNutrientesFueraDeLimite() { validarYOrdenarRecetas(listOf(recetasSemanales[0].copy(calorias = 10001))) }
}
