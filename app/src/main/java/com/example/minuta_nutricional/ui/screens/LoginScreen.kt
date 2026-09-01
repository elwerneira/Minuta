package com.example.minuta_nutricional.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.minuta_nutricional.data.usuariosPrueba
import com.example.minuta_nutricional.ui.components.MensajeVisual

@Composable
fun Login(modifier: Modifier, ingresar: () -> Unit, registrar: () -> Unit, recuperar: () -> Unit) {
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    FormularioBase(modifier, "Minuta Nutricional", "Bienvenido") {
        OutlinedTextField(
            value = correo,
            onValueChange = { 
                correo = it
                mensajeError = "" 
            },
            label = { Text("Correo Electrónico") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = mensajeError.isNotEmpty()
        )
        
        Spacer(Modifier.height(8.dp))
        
        OutlinedTextField(
            value = clave,
            onValueChange = { 
                clave = it
                mensajeError = ""
            },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = mensajeError.isNotEmpty()
        )

        if (mensajeError.isNotEmpty()) {
            MensajeVisual(mensaje = mensajeError, esError = true)
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]+$".toRegex()
                
                when {
                    correo.isBlank() || clave.isBlank() -> {
                        mensajeError = "Todos los campos son obligatorios"
                    }
                    !correo.matches(emailRegex) -> {
                        mensajeError = "Formato de correo inválido"
                    }
                    else -> {
                        val usuarioEncontrado = usuariosPrueba.find { it.correo == correo && it.clave == clave }
                        if (usuarioEncontrado != null) {
                            ingresar()
                        } else {
                            mensajeError = "Credenciales incorrectas"
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Iniciar Sesión")
        }
        
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            TextButton(onClick = registrar) { Text("¿No tienes cuenta? Regístrate") }
            TextButton(onClick = recuperar) { Text("Olvidé mi contraseña") }
        }
    }
}

@Composable
fun FormularioBase(
    modifier: Modifier,
    titulo: String,
    subtitulo: String,
    onBack: (() -> Unit)? = null,
    contenido: @Composable () -> Unit
) {
    val scrollState = rememberScrollState()
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(scrollState), // Habilitamos el scroll para accesibilidad
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (onBack != null) {
                        IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                modifier = Modifier.size(32.dp),
                                tint = Color(0xFF1A237E)
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                    }
                    Column {
                        Text(
                            text = titulo,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A237E)
                        )
                        Text(
                            text = subtitulo,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
                
                Spacer(Modifier.height(16.dp))
                
                contenido()
            }
        }
    }
}
