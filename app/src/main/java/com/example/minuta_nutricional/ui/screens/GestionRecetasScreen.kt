package com.example.minuta_nutricional.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.minuta_nutricional.data.*
import com.example.minuta_nutricional.ui.components.MensajeVisual
import kotlinx.coroutines.launch

@Composable
fun GestionRecetas(volver: () -> Unit) {
    var recetas by remember { mutableStateOf<List<Receta>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var ocupado by remember { mutableStateOf(false) }
    var autorizado by remember { mutableStateOf(false) }
    var cargaCorrecta by remember { mutableStateOf(false) }
    var intento by remember { mutableStateOf(0) }
    var formulario by remember { mutableStateOf(false) }
    var recetaEditada by remember { mutableStateOf<Receta?>(null) }
    var recetaEliminar by remember { mutableStateOf<Receta?>(null) }
    var mensaje by remember { mutableStateOf("") }
    var esError by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(intento) {
        cargando = true
        cargaCorrecta = false
        FirebaseRecetas.esAdministrador().fold(
            onSuccess = { autorizado = it },
            onFailure = { autorizado = false; mensaje = it.mensajeFirebase(); esError = true }
        )
        if (autorizado) {
            FirebaseRecetas.consultar(permitirVacio = true).fold(
                onSuccess = { recetas = it; cargaCorrecta = true },
                onFailure = { mensaje = it.mensajeFirebase(); esError = true }
            )
        }
        cargando = false
    }
    BackHandler(enabled = ocupado) { /* Evita abandonar una operación en curso. */ }
    Column(
        Modifier.fillMaxSize().imePadding().padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(enabled = !ocupado, onClick = {
            if (formulario) { formulario = false; mensaje = "" } else volver()
        }) { Text(if (formulario) "Volver a las recetas" else "Volver al menú") }
        Text("Gestionar recetas", style = MaterialTheme.typography.headlineSmall)
        Text("Los cambios se aplican al catálogo semanal compartido.")
        if (mensaje.isNotEmpty()) MensajeVisual(mensaje, esError)
        when {
            cargando -> { LinearProgressIndicator(Modifier.fillMaxWidth()); Text("Consultando permisos y recetas…") }
            !autorizado -> {
                Text("Esta pantalla requiere una cuenta administradora configurada en Firebase.")
                Button(onClick = { intento++ }) { Text("Revisar permisos nuevamente") }
            }
            !cargaCorrecta -> Button(onClick = { intento++ }) { Text("Reintentar carga") }
            formulario -> key(recetaEditada?.dia ?: "nuevo") {
                FormularioReceta(
                    receta = recetaEditada,
                    diasDisponibles = diasSemana.filter { dia -> recetas.none { it.dia == dia } },
                    ocupado = ocupado,
                    guardar = { receta ->
                        ocupado = true
                        mensaje = ""
                        val crear = recetaEditada == null
                        scope.launch {
                            try {
                                FirebaseRecetas.guardar(receta, crear).fold(
                                    onSuccess = {
                                        mensaje = if (crear) "Receta creada correctamente." else "Receta actualizada correctamente."
                                        esError = false
                                        formulario = false
                                        intento++
                                    },
                                    onFailure = { mensaje = it.mensajeFirebase(); esError = true }
                                )
                            } finally { ocupado = false }
                        }
                    }
                )
            }
            else -> {
                Button(enabled = !ocupado && recetas.size < diasSemana.size, modifier = Modifier.fillMaxWidth(), onClick = {
                    recetaEditada = null; formulario = true; mensaje = ""
                }) { Text("Agregar receta") }
                OutlinedButton(enabled = !ocupado, onClick = { mensaje = ""; intento++ }) { Text("Actualizar lista") }
                if (recetas.isEmpty()) Text("No hay recetas. Agrega la primera para mostrarla en la minuta.")
                if (recetas.size == diasSemana.size) Text("Todos los días ya tienen una receta. Puedes editar las existentes.")
                recetas.forEach { receta ->
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(receta.dia, style = MaterialTheme.typography.titleMedium)
                            Text(receta.nombre)
                            Text(receta.resumen())
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                OutlinedButton(enabled = !ocupado, onClick = {
                                    recetaEditada = receta; formulario = true; mensaje = ""
                                }) { Text("Editar") }
                                TextButton(enabled = !ocupado, onClick = { recetaEliminar = receta }) {
                                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    recetaEliminar?.let { receta ->
        AlertDialog(
            onDismissRequest = { if (!ocupado) recetaEliminar = null },
            title = { Text("¿Eliminar receta?") },
            text = { Text("Se eliminará ${receta.nombre} del ${receta.dia}. Dejará de aparecer en la minuta de todos los usuarios. Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(enabled = !ocupado, onClick = {
                    ocupado = true
                    mensaje = ""
                    scope.launch {
                        try {
                            FirebaseRecetas.eliminar(receta.dia).fold(
                                onSuccess = { mensaje = "Receta eliminada correctamente."; esError = false; intento++ },
                                onFailure = { mensaje = it.mensajeFirebase(); esError = true }
                            )
                            recetaEliminar = null
                        } finally { ocupado = false }
                    }
                }) { Text(if (ocupado) "Eliminando…" else "Eliminar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = { TextButton(enabled = !ocupado, onClick = { recetaEliminar = null }) { Text("Cancelar") } }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormularioReceta(receta: Receta?, diasDisponibles: List<String>, ocupado: Boolean, guardar: (Receta) -> Unit) {
    var dia by remember { mutableStateOf(receta?.dia ?: diasDisponibles.firstOrNull().orEmpty()) }
    var nombre by remember { mutableStateOf(receta?.nombre.orEmpty()) }
    var ingredientes by remember { mutableStateOf(receta?.ingredientes.orEmpty()) }
    var preparacion by remember { mutableStateOf(receta?.preparacion.orEmpty()) }
    var recomendacion by remember { mutableStateOf(receta?.recomendacion.orEmpty()) }
    var calorias by remember { mutableStateOf(receta?.calorias?.toString().orEmpty()) }
    var proteinas by remember { mutableStateOf(receta?.proteinas?.toString().orEmpty()) }
    var carbohidratos by remember { mutableStateOf(receta?.carbohidratos?.toString().orEmpty()) }
    var expandido by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    Text(if (receta == null) "Nueva receta" else "Editar receta", style = MaterialTheme.typography.titleLarge)
    if (receta == null) {
        ExposedDropdownMenuBox(expanded = expandido, onExpandedChange = { if (!ocupado) expandido = !expandido }) {
            OutlinedTextField(
                value = dia, onValueChange = {}, readOnly = true, enabled = !ocupado,
                label = { Text("Día disponible") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandido) },
                modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
            )
            ExposedDropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
                diasDisponibles.forEach { opcion -> DropdownMenuItem(text = { Text(opcion) }, onClick = { dia = opcion; expandido = false }) }
            }
        }
    } else Text("Día: $dia. Para cambiarlo, crea otra receta en el día disponible.")
    OutlinedTextField(nombre, { nombre = it; error = "" }, label = { Text("Nombre de la receta") }, singleLine = true, enabled = !ocupado, modifier = Modifier.fillMaxWidth())
    OutlinedTextField(ingredientes, { ingredientes = it; error = "" }, label = { Text("Ingredientes") }, minLines = 2, enabled = !ocupado, modifier = Modifier.fillMaxWidth())
    OutlinedTextField(preparacion, { preparacion = it; error = "" }, label = { Text("Preparación") }, minLines = 3, enabled = !ocupado, modifier = Modifier.fillMaxWidth())
    CampoNutriente("Calorías (kcal)", calorias, ocupado) { calorias = it; error = "" }
    CampoNutriente("Proteínas (g)", proteinas, ocupado) { proteinas = it; error = "" }
    CampoNutriente("Carbohidratos (g)", carbohidratos, ocupado) { carbohidratos = it; error = "" }
    OutlinedTextField(recomendacion, { recomendacion = it; error = "" }, label = { Text("Recomendación") }, enabled = !ocupado, modifier = Modifier.fillMaxWidth())
    if (error.isNotEmpty()) MensajeVisual(error, esError = true)
    if (ocupado) { LinearProgressIndicator(Modifier.fillMaxWidth()); Text("Guardando receta…") }
    Button(enabled = !ocupado, modifier = Modifier.fillMaxWidth().height(56.dp), onClick = {
        val kcal = calorias.toIntOrNull()
        val prot = proteinas.toIntOrNull()
        val carb = carbohidratos.toIntOrNull()
        error = when {
            nombre.isBlank() || ingredientes.isBlank() || preparacion.isBlank() || recomendacion.isBlank() -> "Completa todos los campos de texto."
            nombre.trim().length > 120 || ingredientes.trim().length > 2000 || preparacion.trim().length > 4000 || recomendacion.trim().length > 500 -> "Reduce el texto: nombre hasta 120, ingredientes 2000, preparación 4000 y recomendación 500 caracteres."
            kcal == null || kcal !in 1..10000 -> "Ingresa calorías enteras entre 1 y 10000."
            prot == null || carb == null || prot !in 0..1000 || carb !in 0..1000 -> "Ingresa proteínas y carbohidratos enteros entre 0 y 1000."
            dia.isBlank() -> "Selecciona un día disponible."
            else -> ""
        }
        if (error.isEmpty()) guardar(Receta(dia, nombre.trim(), ingredientes.trim(), preparacion.trim(), kcal!!, prot!!, carb!!, recomendacion.trim()))
    }) { Text(if (receta == null) "Crear receta" else "Guardar cambios") }
}

@Composable
private fun CampoNutriente(etiqueta: String, valor: String, ocupado: Boolean, cambiar: (String) -> Unit) {
    OutlinedTextField(
        value = valor, onValueChange = cambiar, label = { Text(etiqueta) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true, enabled = !ocupado, modifier = Modifier.fillMaxWidth()
    )
}
