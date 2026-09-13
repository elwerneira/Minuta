package com.example.minuta_nutricional.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.minuta_nutricional.data.usuariosPrueba
import com.example.minuta_nutricional.ui.components.MensajeVisual
import com.example.minuta_nutricional.utils.esCorreoValido

@Composable
fun RecuperarClave(modifier: Modifier, volver: () -> Unit) {
    var correo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var esError by remember { mutableStateOf(false) }
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
                val correoLimpio = correo.trim()
                when {
                    correoLimpio.isBlank() -> {
                        mensaje = "Ingresa tu correo electrónico para continuar."
                        esError = true
                    }
                    !correoLimpio.esCorreoValido() -> {
                        mensaje = "El formato del correo electrónico no es válido."
                        esError = true
                    }
                    !usuariosPrueba.any { usuario ->
                        usuario.correo.equals(correoLimpio, ignoreCase = true)
                    } -> {
                        mensaje = "No existe una cuenta registrada con este correo."
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
