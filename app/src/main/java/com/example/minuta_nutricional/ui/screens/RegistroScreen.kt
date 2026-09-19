package com.example.minuta_nutricional.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.minuta_nutricional.data.Usuario
import com.example.minuta_nutricional.data.usuariosPrueba
import com.example.minuta_nutricional.ui.components.MensajeVisual
import com.example.minuta_nutricional.utils.esCorreoValido
import com.example.minuta_nutricional.utils.validar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Registro(
    modifier: Modifier,
    volver: () -> Unit,
    registroExitoso: (String) -> Unit
) {
    // Cambiar cuando el usuario completa el formulario.
    var nombre: String by remember { mutableStateOf("") }
    var correo: String by remember { mutableStateOf("") }
    var clave: String by remember { mutableStateOf("") }
    var objetivo: String by remember { mutableStateOf("Mantener peso") }
    var acepta: Boolean by remember { mutableStateOf(false) }
    var expandido: Boolean by remember { mutableStateOf(false) }
    var tipoAlimentacion: String by remember { mutableStateOf("Sin preferencia") }
    var mensaje: String by remember { mutableStateOf("") }
    var esError: Boolean by remember { mutableStateOf(false) }
    FormularioBase(
        modifier = modifier,
        titulo = "Registro",
        subtitulo = "Perfil nutricional",
        onBack = volver
    ) {
        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                mensaje = ""
            },
            label = { Text("Nombre Completo") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = mensaje.isNotEmpty() && esError
        )
        
        Spacer(Modifier.height(8.dp))
        
        OutlinedTextField(
            value = correo,
            onValueChange = {
                correo = it
                mensaje = ""
            },
            label = { Text("Correo Electrónico") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = mensaje.isNotEmpty() && esError
        )
        
        Spacer(Modifier.height(8.dp))
        
        OutlinedTextField(
            value = clave,
            onValueChange = {
                clave = it
                mensaje = ""
            },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = mensaje.isNotEmpty() && esError
        )

        if (mensaje.isNotEmpty()) {
            MensajeVisual(mensaje = mensaje, esError = esError)
        }

        Spacer(Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expandido,
            onExpandedChange = { expandido = !expandido }
        ) {
            OutlinedTextField(
                value = tipoAlimentacion,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo de alimentación") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false }
            ) {
                listOf("Sin preferencia", "Vegetariana", "Vegana").forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            tipoAlimentacion = opcion
                            expandido = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        Text("Objetivo nutricional", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Bajar de peso", "Mantener peso", "Aumentar masa muscular").forEach { opcion ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = objetivo == opcion, 
                        onClick = { objetivo = opcion }
                    )
                    Text(opcion, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = acepta, onCheckedChange = { acepta = it })
            Text("Acepto recomendaciones", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(Modifier.height(24.dp))
        
        Button(
            onClick = {
                val correoLimpio = correo.trim()
                when {
                    nombre.isBlank() || correoLimpio.isBlank() || clave.isBlank() -> {
                        mensaje = "Completa nombre, correo y contraseña para continuar."
                        esError = true
                    }
                    !validar(correoLimpio) { it.esCorreoValido() } -> {
                        mensaje = "Ingresa un correo electrónico válido."
                        esError = true
                    }
                    clave.length < 6 -> {
                        mensaje = "La contraseña debe tener al menos 6 caracteres."
                        esError = true
                    }
                    usuariosPrueba.any { it.correo.equals(correoLimpio, ignoreCase = true) } -> {
                        mensaje = "Este correo ya está registrado."
                        esError = true
                    }
                    !acepta -> {
                        mensaje = "Debes aceptar las recomendaciones para crear tu cuenta."
                        esError = true
                    }
                    else -> {
                        val nuevoUsuario = Usuario(correoLimpio, clave, nombre.trim())
                        try {
                            usuariosPrueba.add(nuevoUsuario)
                            registroExitoso(nuevoUsuario.nombre)
                        } catch (e: Exception) {
                            mensaje = "No fue posible crear la cuenta. Intenta nuevamente."
                            esError = true
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Crear cuenta", style = MaterialTheme.typography.titleMedium)
        }
        
        Spacer(Modifier.height(16.dp))
    }
}
