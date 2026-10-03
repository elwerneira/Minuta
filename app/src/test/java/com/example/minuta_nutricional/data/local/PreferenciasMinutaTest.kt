package com.example.minuta_nutricional.data.local

import org.junit.Assert.assertEquals
import org.junit.Test

class PreferenciasMinutaTest {
    private val dias = listOf("Lunes", "Martes")

    @Test
    fun conservaDiaDisponible() {
        assertEquals("Martes", resolverDiaInicial(dias, "Martes"))
    }

    @Test
    fun conservaDiaLibre() {
        assertEquals(DIA_LIBRE, resolverDiaInicial(dias, DIA_LIBRE))
    }

    @Test
    fun descartaDiaEliminadoDelCatalogo() {
        assertEquals("Lunes", resolverDiaInicial(dias, "Viernes"))
    }

    @Test
    fun usaDiaLibreSiNoQuedanRecetas() {
        assertEquals(DIA_LIBRE, resolverDiaInicial(emptyList(), "Viernes"))
    }
}
