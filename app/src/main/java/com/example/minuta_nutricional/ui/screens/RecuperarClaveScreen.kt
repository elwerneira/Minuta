package com.example.minuta_nutricional.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RecuperarClave(modifier: Modifier, volver: () -> Unit) {
    var correo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    FormularioBase(modifier, "Recuperar Acceso", "Enviaremos un enlace a su correo") {
        OutlinedTextField(
            value = correo,
            onValueChange = { 
                correo = it
                mensaje = ""
            },
            label = { Text("Correo Electrónico") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        if (mensaje.isNotEmpty()) {
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                if (correo.isNotBlank()) {
                    mensaje = "Enlace enviado exitosamente"
                } else {
                    mensaje = "Por favor ingrese su correo"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Enviar Instrucciones")
        }
        
        TextButton(
            onClick = volver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al inicio de sesión")
        }
    }
}
