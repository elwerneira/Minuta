package com.example.minuta_nutricional.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.minuta_nutricional.data.*
import com.example.minuta_nutricional.ui.components.MensajeVisual
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Minuta y detalle utilizan la misma consulta, con estados visibles de carga y error.
@Composable
fun CatalogoRecetas(volver: () -> Unit, contenido: @Composable (List<Receta>) -> Unit) {
    val context = LocalContext.current
    var recetas by remember { mutableStateOf<List<Receta>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var mensaje by remember { mutableStateOf("") }
    var intento by remember { mutableStateOf(0) }
    var usarLocales by remember { mutableStateOf(false) }
    LaunchedEffect(intento, usarLocales) {
        cargando = true
        mensaje = ""
        recetas = emptyList()
        if (usarLocales) {
            recetas = withContext(Dispatchers.IO) {
                try { context.consultarRecetas().ifEmpty { recetasSemanales.toList() } }
                catch (_: Exception) { recetasSemanales.toList() }
            }
        } else {
            FirebaseRecetas.consultar().fold(
                onSuccess = { recetas = it },
                onFailure = { mensaje = it.mensajeFirebase() }
            )
        }
        cargando = false
    }
    Column(Modifier.fillMaxSize()) {
        when {
            cargando -> Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
                Text("Cargando recetas…")
                OutlinedButton(onClick = volver) { Text("Volver al menú") }
            }
            mensaje.isNotEmpty() -> Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                MensajeVisual(mensaje, esError = true)
                Button(onClick = { intento++ }) { Text("Reintentar") }
                OutlinedButton(onClick = { usarLocales = true }) { Text("Usar recetas locales de ejemplo") }
                TextButton(onClick = volver) { Text("Volver") }
            }
            else -> {
                Text(
                    if (usarLocales) "Recetas locales de ejemplo · No son datos de Firebase" else "Catálogo cargado desde Firebase",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.bodySmall
                )
                Box(Modifier.weight(1f)) { contenido(recetas) }
            }
        }
    }
}
