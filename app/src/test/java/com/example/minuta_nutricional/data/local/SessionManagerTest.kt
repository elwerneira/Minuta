package com.example.minuta_nutricional.data.local

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SessionManagerTest {
    private val datos = DatosSesion("uid-123", "persona@ejemplo.cl")

    @Test
    fun entregaDatosSoloConUidAutenticado() {
        assertEquals(datos, sesionCoincidente(datos, "uid-123"))
    }

    @Test
    fun noAceptaOtraCuenta() {
        assertNull(sesionCoincidente(datos, "uid-456"))
    }

    @Test
    fun noAceptaSesionFirebaseAusente() {
        assertNull(sesionCoincidente(datos, null))
    }
}
