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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

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

    FormularioBase(modifier, "Registro", "Complete su perfil nutricional") {
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre Completo") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo Electrónico") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
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

        Spacer(Modifier.height(8.dp))
        Text("Objetivo nutricional", style = MaterialTheme.typography.titleSmall)
        
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            listOf("Bajar de peso", "Mantener peso", "Aumentar masa muscular").forEach { opcion ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = objetivo == opcion, onClick = { objetivo = opcion })
                    Text(opcion)
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = acepta, onCheckedChange = { acepta = it })
            Text("Acepto recomendaciones", style = MaterialTheme.typography.bodySmall)
        }

        Spacer(Modifier.height(16.dp))
        
        Button(
            onClick = volver,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Crear Cuenta")
        }
        
        TextButton(onClick = volver, modifier = Modifier.fillMaxWidth()) {
            Text("¿Ya tienes cuenta? Ingresa")
        }
    }
}
