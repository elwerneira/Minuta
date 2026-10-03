package com.example.minuta_nutricional

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.minuta_nutricional.data.local.DatosSesion
import com.example.minuta_nutricional.data.local.SessionManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SessionManagerInstrumentedTest {
    @Test
    fun guardaRecuperaYLimpiaDatosBasicos() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val session = SessionManager(context, "user_session_instrumented_test")
        session.limpiarSesion()
        try {
            session.guardarSesion("uid-test", "persona@ejemplo.cl")
            assertEquals(
                DatosSesion("uid-test", "persona@ejemplo.cl"),
                session.obtenerSesionSiCoincide("uid-test")
            )
            assertNull(session.obtenerSesionSiCoincide("otro-uid"))
        } finally {
            session.limpiarSesion()
        }
        assertNull(session.obtenerSesionSiCoincide("uid-test"))
    }
}
