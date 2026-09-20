package com.example.minuta_nutricional.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.minuta_nutricional.data.Receta
import com.example.minuta_nutricional.data.consultarRecetas
import com.example.minuta_nutricional.data.recetasSemanales
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MinutaSemanal(
    modifier: Modifier = Modifier,
    abrirReceta: (Int) -> Unit,
    volver: () -> Unit
) {
    val context = LocalContext.current
    val recetas = remember {
        try {
            context.consultarRecetas().ifEmpty { recetasSemanales.toList() }
        } catch (_: Exception) {
            recetasSemanales.toList()
        }
    }
    var diaSeleccionado by remember { mutableStateOf(recetas.first().dia) }
    var mensajeSeleccion by remember { mutableStateOf("") }
    var mostrarMensajeSeleccion by remember { mutableStateOf(false) }
    var versionMensaje by remember { mutableStateOf(0) }

    LaunchedEffect(versionMensaje) {
        if (versionMensaje > 0) {
            delay(3_000)
            mostrarMensajeSeleccion = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedButton(onClick = volver) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            Text(" Volver al menú")
        }

        Spacer(Modifier.height(12.dp))
        Text(
            text = "Minuta semanal",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E)
        )
        Text(
            text = "Selecciona un día y luego abre la receta completa.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(Modifier.height(16.dp))

        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val anchoBoton = if (maxWidth < 360.dp) maxWidth else (maxWidth - 12.dp) / 2

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                recetas.forEach { receta ->
                    val seleccionado = diaSeleccionado == receta.dia
                    Button(
                        onClick = {
                            diaSeleccionado = receta.dia
                            mensajeSeleccion = "${receta.dia} seleccionado."
                            mostrarMensajeSeleccion = true
                            versionMensaje++
                        },
                        modifier = Modifier
                            .width(anchoBoton)
                            .semantics {
                                stateDescription = if (seleccionado) {
                                    "Día seleccionado"
                                } else {
                                    "Día no seleccionado"
                                }
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (seleccionado) Color(0xFF1A237E) else Color(0xFFE8EAF6),
                            contentColor = if (seleccionado) Color.White else Color(0xFF1A237E)
                        )
                    ) {
                        Text(receta.dia)
                    }
                }

                val seleccionado = diaSeleccionado == DIA_LIBRE
                Button(
                    onClick = {
                        diaSeleccionado = DIA_LIBRE
                        mensajeSeleccion = "Día libre seleccionado."
                        mostrarMensajeSeleccion = true
                        versionMensaje++
                    },
                    modifier = Modifier
                        .width(anchoBoton)
                        .semantics {
                            stateDescription = if (seleccionado) {
                                "Día seleccionado"
                            } else {
                                "Día no seleccionado"
                            }
                        },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (seleccionado) Color(0xFFFF9800) else Color(0xFFFFF3E0),
                        contentColor = if (seleccionado) Color.White else Color(0xFFE65100)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(DIA_LIBRE)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        if (mostrarMensajeSeleccion) {
            MensajeSeleccionCompacto(mensajeSeleccion)
            Spacer(Modifier.height(12.dp))
        }

        if (diaSeleccionado == DIA_LIBRE) {
            TarjetaDiaLibre()
        } else {
            val indice = recetas.indexOfFirst { it.dia == diaSeleccionado }
            recetas.getOrNull(indice)?.let { receta ->
                TarjetaResumenReceta(
                    receta = receta,
                    verDetalle = {
                        val indiceOriginal = recetasSemanales.indexOfFirst { it.dia == receta.dia }
                        abrirReceta(indiceOriginal.coerceAtLeast(0))
                    }
                )
            }
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
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = "Selección confirmada")
            Text(mensaje, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun TarjetaResumenReceta(receta: Receta, verDetalle: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(receta.dia, color = Color(0xFF3F51B5), fontWeight = FontWeight.Bold)
            Text(receta.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(receta.resumen(), style = MaterialTheme.typography.bodyMedium)
            Button(
                onClick = verDetalle,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Ver receta completa")
            }
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
                text = "¡Día libre!",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFFFF9800),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Disfruta tus comidas favoritas con moderación y mantente hidratado.",
                style = MaterialTheme.typography.bodyLarge
            )
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
            Text(receta.dia, color = Color(0xFF3F51B5), fontWeight = FontWeight.Bold)
            Text(receta.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), thickness = 0.5.dp)

            Text("Ingredientes", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(receta.ingredientes, style = MaterialTheme.typography.bodyMedium)
            Text("Preparación", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(receta.preparacion, style = MaterialTheme.typography.bodyMedium)
            Text("Información nutricional", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = "Información nutricional: ${receta.resumen()}"
                    },
                color = Color(0xFFE8EAF6),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DatoNutricional("Calorías", "${receta.calorias} kcal", Modifier.weight(1f))
                    VerticalDivider(modifier = Modifier.height(44.dp), color = Color.White)
                    DatoNutricional("Proteínas", "${receta.proteinas} g", Modifier.weight(1f))
                    VerticalDivider(modifier = Modifier.height(44.dp), color = Color.White)
                    DatoNutricional("Carbohidratos", "${receta.carbohidratos} g", Modifier.weight(1f))
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = receta.recomendacion,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun DatoNutricional(etiqueta: String, valor: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF3F51B5),
            fontWeight = FontWeight.Medium
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF1A237E),
            fontWeight = FontWeight.Bold
        )
    }
}

private const val DIA_LIBRE = "Día libre"
