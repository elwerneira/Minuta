package com.example.minuta_nutricional.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.minuta_nutricional.ui.components.MensajeVisual

@Composable
fun RecuperarClave(modifier: Modifier, volver: () -> Unit) {
    var correo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var esError by remember { mutableStateOf(false) }
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]+$".toRegex()

    FormularioBase(
        modifier = modifier,
        titulo = "Recuperar",
        subtitulo = "Enviaremos instrucciones",
        onBack = volver
    ) {
        OutlinedTextField(
            value = correo,
            onValueChange = { 
                correo = it
                mensaje = ""
                esError = false
            },
            label = { Text("Correo Electrónico") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = mensaje.isNotEmpty() && esError
        )

        if (mensaje.isNotEmpty()) {
            MensajeVisual(mensaje = mensaje, esError = esError)
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                when {
                    correo.isBlank() -> {
                        mensaje = "Ingresa tu correo electrónico para continuar."
                        esError = true
                    }
                    !correo.matches(emailRegex) -> {
                        mensaje = "El formato del correo electrónico no es válido."
                        esError = true
                    }
                    else -> {
                        mensaje = "Instrucciones enviadas. Revisa tu correo electrónico."
                        esError = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Enviar instrucciones")
        }
    }
}
