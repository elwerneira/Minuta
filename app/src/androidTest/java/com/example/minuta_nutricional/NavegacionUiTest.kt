package com.example.minuta_nutricional

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavegacionUiTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun loginSinDatosMuestraErrorYPermaneceEnLaPantalla() {
        composeRule.onNodeWithText("Iniciar Sesión").performClick()

        composeRule.onNodeWithText("Todos los campos son obligatorios").assertIsDisplayed()
        composeRule.onNodeWithText("¿No tienes cuenta? Regístrate").assertIsDisplayed()
    }

    @Test
    fun registroYRecuperacionRegresanAlLogin() {
        composeRule.onNodeWithText("¿No tienes cuenta? Regístrate").performClick()
        composeRule.onNodeWithText("Registro").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Volver").performClick()

        composeRule.onNodeWithText("Olvidé mi contraseña").performClick()
        composeRule.onNodeWithText("Recuperar").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Volver").performClick()

        composeRule.onNodeWithText("Iniciar Sesión").assertIsDisplayed()
    }
}
