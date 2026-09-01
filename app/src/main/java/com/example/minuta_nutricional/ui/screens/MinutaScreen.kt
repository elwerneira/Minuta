package com.example.minuta_nutricional.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.minuta_nutricional.data.Receta
import com.example.minuta_nutricional.data.recetasSemanales
import kotlinx.coroutines.delay

@Composable
fun MinutaSemanal(modifier: Modifier, nombreUsuario: String, salir: () -> Unit) {
    // Cambia cuando el usuario selecciona un día.
    var diaSeleccionado: Int by remember { mutableStateOf(0) }
    // Muestra la confirmación de la selección.
    var mensajeSeleccion: String by remember { mutableStateOf("") }
    // Booleano
    var mostrarMensajeSeleccion: Boolean by remember { mutableStateOf(false) }
    var versionMensaje: Int by remember { mutableStateOf(0) }
    var mostrarBienvenida: Boolean by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(versionMensaje) {
        if (versionMensaje > 0) {
            delay(3_000)
            mostrarMensajeSeleccion = false
        }
    }

    if (mostrarBienvenida) {
        AlertDialog(
            onDismissRequest = { mostrarBienvenida = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Inicio de sesión correcto"
                )
            },
            title = { Text("Sesión iniciada correctamente") },
            text = {
                val mensajeBienvenida = if (nombreUsuario.isBlank()) {
                    "Ahora puedes revisar tu minuta semanal y seleccionar un día para ver su receta."
                } else {
                    "Bienvenido, $nombreUsuario. Ahora puedes revisar tu minuta semanal y seleccionar un día para ver su receta."
                }
                Text(mensajeBienvenida)
            },
            confirmButton = {
                TextButton(onClick = { mostrarBienvenida = false }) {
                    Text("Continuar")
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Minuta Semanal",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Seleccione un día para ver su plan nutricional.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ){
            items(recetasSemanales.size) { indice ->
                val esSeleccionado = diaSeleccionado == indice
                Button(
                    onClick = {
                        diaSeleccionado = indice
                        mensajeSeleccion = "${recetasSemanales[indice].dia} seleccionado. Receta actualizada."
                        mostrarMensajeSeleccion = true
                        versionMensaje++
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics {
                            stateDescription = if (esSeleccionado) {
                                "Día seleccionado"
                            } else {
                                "Día no seleccionado"
                            }
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (esSeleccionado) Color(0xFF1A237E) else Color(0xFFE8EAF6),
                        contentColor = if (esSeleccionado) Color.White else Color(0xFF1A237E)
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = if (esSeleccionado) 4.dp else 0.dp)
                ) {
                    Text(recetasSemanales[indice].dia)
                }
            }
            item {
                val esDiaLibre = diaSeleccionado == 7
                Button(
                    onClick = {
                        diaSeleccionado = 7
                        mensajeSeleccion = "Día libre seleccionado. Recomendación actualizada."
                        mostrarMensajeSeleccion = true
                        versionMensaje++
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics {
                            stateDescription = if (esDiaLibre) {
                                "Día seleccionado"
                            } else {
                                "Día no seleccionado"
                            }
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (esDiaLibre) Color(0xFFFF9800) else Color(0xFFFFF3E0),
                        contentColor = if (esDiaLibre) Color.White else Color(0xFFE65100)
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = if (esDiaLibre) 4.dp else 0.dp)
                ) {
                    Text("Día Libre")
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        if (mostrarMensajeSeleccion) {
            MensajeSeleccionCompacto(mensaje = mensajeSeleccion)
            Spacer(Modifier.height(12.dp))
        }

        if (diaSeleccionado == 7) {
            TarjetaDiaLibre()
        } else {
            TarjetaReceta(recetasSemanales[diaSeleccionado])
        }

        Spacer(Modifier.height(24.dp))

        OutlinedButton(
            onClick = salir,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
        ) {

            Text("Cerrar Sesión")
        }
    }
}

@Composable
private fun MensajeSeleccionCompacto(mensaje: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { liveRegion = LiveRegionMode.Polite },
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Selección confirmada"
            )
            Text(
                text = mensaje,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun TarjetaDiaLibre() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "¡Día Libre!",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFFFF9800),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Momento de relajarse",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), thickness = 0.5.dp)
            
            Text(
                text = "Hoy no tienes una receta estricta. Puedes disfrutar de tus comidas favoritas con moderación.",
                style = MaterialTheme.typography.bodyLarge
            )
            
            Text(
                text = "Sugerencia: Intenta mantenerte hidratado y no excederte demasiado para no perder el progreso de la semana.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "¡Mas que merecido! Retomamos con energía el siguiente día.",
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE65100)
                )
            }
        }
    }
}

@Composable
fun TarjetaReceta(receta: Receta) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = receta.dia,
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF3F51B5),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = receta.nombre,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold
            )
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), thickness = 0.5.dp)
            
            Text(
                text = "Ingredientes:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(receta.ingredientes, style = MaterialTheme.typography.bodyMedium)
            
            Text(
                text = "Preparación:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(receta.preparacion, style = MaterialTheme.typography.bodyMedium)
            
            ListItem(
                headlineContent = { Text("Aporte Energético", fontWeight = FontWeight.Bold) },
                trailingContent = { 
                    Text(
                        "${receta.calorias} kcal",
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Bold
                    ) 
                },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
            )
            
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = " ${receta.recomendacion}",
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
