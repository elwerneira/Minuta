package com.example.minuta_nutricional

import com.example.minuta_nutricional.utils.esCorreoValido
import com.example.minuta_nutricional.utils.validar
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun validar_aplicaLaReglaRecibida() {
        val correoCorrecto = validar("usuario@duocuc.cl") { it.esCorreoValido() }

        assertTrue(correoCorrecto)
    }

    @Test
    fun esCorreoValido_rechazaFormatoInvalido() {
        assertFalse("correo-sin-dominio".esCorreoValido())
    }
}
