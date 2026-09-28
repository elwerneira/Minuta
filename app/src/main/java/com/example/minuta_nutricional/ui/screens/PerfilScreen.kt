package com.example.minuta_nutricional.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.window.DialogProperties
import com.example.minuta_nutricional.data.FirebaseUsuarios
import com.example.minuta_nutricional.ui.components.MensajeVisual
import com.example.minuta_nutricional.data.mensajeFirebase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Perfil(modifier: Modifier, volver: () -> Unit, perfilActualizado: (String) -> Unit, cuentaEliminada: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var alimentacion by remember { mutableStateOf("Sin preferencia") }
    var objetivo by remember { mutableStateOf("Mantener peso") }
    var expandido by remember { mutableStateOf(false) }
    var cargando by remember { mutableStateOf(true) }
    var guardando by remember { mutableStateOf(false) }
    var perfilDisponible by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("") }
    var esError by remember { mutableStateOf(false) }
    var intentoCarga by remember { mutableStateOf(0) }
    var confirmarEliminacion by remember { mutableStateOf(false) }
    var claveEliminacion by remember { mutableStateOf("") }
    var errorEliminacion by remember { mutableStateOf("") }
    var eliminando by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(intentoCarga) {
        cargando = true
        mensaje = ""
        FirebaseUsuarios.consultarPerfil().fold(
            onSuccess = { perfil ->
                nombre = perfil.nombre
                correo = perfil.correo
                alimentacion = perfil.tipoAlimentacion
                objetivo = perfil.objetivo
                perfilDisponible = true
            },
            onFailure = {
                mensaje = it.mensajeFirebase()
                esError = true
                perfilDisponible = false
            }
        )
        cargando = false
    }

    BackHandler(enabled = guardando || eliminando) { /* Esperar la confirmación de Firebase. */ }
    if (confirmarEliminacion) {
        AlertDialog(
            onDismissRequest = {
                if (!eliminando) { confirmarEliminacion = false; claveEliminacion = "" }
            },
            properties = DialogProperties(dismissOnBackPress = !eliminando, dismissOnClickOutside = !eliminando),
            title = { Text("¿Eliminar tu cuenta?") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Se eliminarán tu cuenta y tu perfil nutricional. Esta acción no se puede deshacer. Ingresa tu contraseña para confirmar.")
                    OutlinedTextField(
                        value = claveEliminacion,
                        onValueChange = { claveEliminacion = it; errorEliminacion = "" },
                        label = { Text("Contraseña actual") },
                        singleLine = true,
                        enabled = !eliminando,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (errorEliminacion.isNotEmpty()) MensajeVisual(errorEliminacion, esError = true)
                    if (eliminando) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        Text("Eliminando cuenta…")
                    }
                }
            },
            confirmButton = {
                TextButton(
                    enabled = !eliminando && claveEliminacion.isNotEmpty(),
                    onClick = {
                        eliminando = true
                        errorEliminacion = ""
                        val clave = claveEliminacion
                        claveEliminacion = ""
                        scope.launch {
                            try {
                                FirebaseUsuarios.eliminarCuenta(clave).fold(
                                    onSuccess = { confirmarEliminacion = false; cuentaEliminada() },
                                    onFailure = { errorEliminacion = it.mensajeFirebase() }
                                )
                            } finally { eliminando = false }
                        }
                    }
                ) { Text("Eliminar definitivamente", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(enabled = !eliminando, onClick = {
                    confirmarEliminacion = false
                    claveEliminacion = ""
                }) { Text("Cancelar") }
            }
        )
    }
    FormularioBase(
        modifier = modifier,
        titulo = "Mi perfil",
        subtitulo = "Consulta y actualiza tus datos",
        onBack = if (guardando || eliminando) null else volver
    ) {
        when {
            cargando -> {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Text("Cargando tu perfil…")
            }
            !perfilDisponible -> {
                MensajeVisual(mensaje = mensaje, esError = true)
                Button(onClick = { intentoCarga++ }, modifier = Modifier.fillMaxWidth()) {
                    Text("Reintentar")
                }
            }
            else -> {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it; mensaje = "" },
                    label = { Text("Nombre completo") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !guardando,
                    singleLine = true,
                    isError = esError && (nombre.isBlank() || nombre.trim().length > 120)
                )
                OutlinedTextField(
                    value = correo,
                    onValueChange = {},
                    label = { Text("Correo electrónico") },
                    supportingText = { Text("El correo de tu cuenta no se modifica aquí.") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    singleLine = true
                )
                ExposedDropdownMenuBox(
                    expanded = expandido,
                    onExpandedChange = { if (!guardando) expandido = !expandido }
                ) {
                    OutlinedTextField(
                        value = alimentacion,
                        onValueChange = {},
                        readOnly = true,
                        enabled = !guardando,
                        label = { Text("Tipo de alimentación") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(
                            type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                            enabled = !guardando
                        )
                    )
                    ExposedDropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
                        listOf("Sin preferencia", "Vegetariana", "Vegana").forEach { opcion ->
                            DropdownMenuItem(text = { Text(opcion) }, onClick = {
                                alimentacion = opcion
                                expandido = false
                                mensaje = ""
                            })
                        }
                    }
                }
                Text("Objetivo nutricional", style = MaterialTheme.typography.titleMedium)
                listOf("Bajar de peso", "Mantener peso", "Aumentar masa muscular").forEach { opcion ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = objetivo == opcion,
                            enabled = !guardando,
                            onClick = { objetivo = opcion; mensaje = "" }
                        )
                        Text(opcion)
                    }
                }
                if (mensaje.isNotEmpty()) MensajeVisual(mensaje = mensaje, esError = esError)
                Button(
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    enabled = !guardando,
                    onClick = {
                        if (nombre.isBlank() || nombre.trim().length > 120) {
                            mensaje = "Ingresa un nombre de entre 1 y 120 caracteres."
                            esError = true
                        } else {
                            val nombreGuardado = nombre.trim()
                            guardando = true
                            mensaje = ""
                            scope.launch {
                                try {
                                    FirebaseUsuarios.guardarPerfil(nombreGuardado, alimentacion, objetivo).fold(
                                        onSuccess = {
                                            nombre = nombreGuardado
                                            perfilActualizado(nombreGuardado)
                                            mensaje = "Tu perfil se actualizó correctamente."
                                            esError = false
                                        },
                                        onFailure = { mensaje = it.mensajeFirebase(); esError = true }
                                    )
                                } finally {
                                    guardando = false
                                }
                            }
                        }
                    }
                ) {
                    Text(if (guardando) "Guardando cambios…" else "Guardar cambios")
                }
                OutlinedButton(
                    enabled = !guardando && !eliminando,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    onClick = { errorEliminacion = ""; claveEliminacion = ""; confirmarEliminacion = true }
                ) {
                    Text("Eliminar cuenta", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
