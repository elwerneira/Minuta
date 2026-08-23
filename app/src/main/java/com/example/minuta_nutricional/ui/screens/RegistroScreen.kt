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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Registro(modifier: Modifier, volver: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var objetivo by remember { mutableStateOf("Mantener peso") }
    var acepta by remember { mutableStateOf(false) }
    var expandido by remember { mutableStateOf(false) }
    var tipoAlimentacion by remember { mutableStateOf("Sin preferencia") }

    FormularioBase(
        modifier = modifier,
        titulo = "Registro",
        subtitulo = "Perfil nutricional",
        onBack = volver
    ) {
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre Completo") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(Modifier.height(8.dp))
        
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo Electrónico") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(Modifier.height(8.dp))
        
        OutlinedTextField(
            value = clave,
            onValueChange = { clave = it },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

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
                if (nombre.isNotBlank() && correo.isNotBlank() && clave.isNotBlank()) {
                    usuariosPrueba.add(Usuario(correo, clave, nombre))
                    volver()
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Crear Cuenta", style = MaterialTheme.typography.titleMedium)
        }
        
        Spacer(Modifier.height(16.dp))
    }
}
