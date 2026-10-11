package com.example.minuta_nutricional.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class FirebaseRecetasTest {
    @Test fun ordenaPorDiaSinDependerDeLasClaves() {
        val resultado = validarYOrdenarRecetas(recetasSemanales.reversed())
        assertEquals(listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes"), resultado.map { it.dia })
    }
    @Test fun rechazaCatalogoVacio() {
        val error = assertThrows(IllegalStateException::class.java) {
            validarYOrdenarRecetas(emptyList())
        }
        assertEquals("No hay recetas disponibles en el catálogo de Firebase.", error.message)
    }

    @Test fun rechazaDiasDuplicados() {
        val error = assertThrows(IllegalStateException::class.java) {
            validarYOrdenarRecetas(listOf(recetasSemanales[0], recetasSemanales[0]))
        }
        assertEquals("Hay más de una receta para el mismo día.", error.message)
    }

    @Test fun rechazaInformacionIncompleta() {
        val error = assertThrows(IllegalStateException::class.java) {
            validarYOrdenarRecetas(listOf(Receta(dia = "Lunes")))
        }
        assertEquals("Revisa los días, textos y nutrientes del catálogo de Firebase.", error.message)
    }

    @Test fun rechazaNutrientesNegativos() {
        val error = assertThrows(IllegalStateException::class.java) {
            validarYOrdenarRecetas(listOf(recetasSemanales[0].copy(proteinas = -1)))
        }
        assertEquals("Revisa los días, textos y nutrientes del catálogo de Firebase.", error.message)
    }

    @Test fun rechazaDiaDesconocido() {
        val error = assertThrows(IllegalStateException::class.java) {
            validarYOrdenarRecetas(listOf(recetasSemanales[0].copy(dia = "Otro")))
        }
        assertEquals("Revisa los días, textos y nutrientes del catálogo de Firebase.", error.message)
    }

    @Test fun clavesDeDiasSonEstablesSinTildes() {
        assertEquals(listOf("lunes", "martes", "miercoles", "jueves", "viernes", "sabado", "domingo"), diasSemana.map { claveDia(it) })
    }

    @Test fun rechazaClaveDeDiaInvalida() {
        val error = assertThrows(IllegalStateException::class.java) {
            claveDia("Día libre")
        }
        assertEquals("Selecciona un día válido.", error.message)
    }

    @Test fun rechazaNombreDemasiadoLargo() {
        val error = assertThrows(IllegalStateException::class.java) {
            validarYOrdenarRecetas(listOf(recetasSemanales[0].copy(nombre = "a".repeat(121))))
        }
        assertEquals("Revisa los días, textos y nutrientes del catálogo de Firebase.", error.message)
    }

    @Test fun rechazaNutrientesFueraDeLimite() {
        val error = assertThrows(IllegalStateException::class.java) {
            validarYOrdenarRecetas(listOf(recetasSemanales[0].copy(calorias = 10001)))
        }
        assertEquals("Revisa los días, textos y nutrientes del catálogo de Firebase.", error.message)
    }
}
