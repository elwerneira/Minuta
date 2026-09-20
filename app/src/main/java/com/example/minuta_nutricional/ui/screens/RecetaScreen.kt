package com.example.minuta_nutricional.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import com.example.minuta_nutricional.R
import com.example.minuta_nutricional.data.Receta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecetaDetalle(
    receta: Receta,
    volver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var colorAcento by remember { mutableStateOf(Color(0xFF2E7D32)) }

    LaunchedEffect(context) {
        val colorRespaldo = colorAcento.toArgb()
        val colorExtraido = withContext(Dispatchers.Default) {
            ContextCompat.getDrawable(context, R.drawable.ic_receta)
                ?.toBitmap()
                ?.let { bitmap ->
                    Palette.from(bitmap).generate().getDominantColor(colorRespaldo)
                }
        }
        colorExtraido?.let { colorAcento = Color(it) }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Receta del ${receta.dia}") },
                navigationIcon = {
                    IconButton(onClick = volver) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver a la minuta"
                        )
                    }
                }
            )
        },
        containerColor = colorAcento.copy(alpha = 0.10f)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(R.drawable.ic_receta),
                contentDescription = "Ilustración de ${receta.nombre}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = "Color de interfaz adaptado con Palette",
                style = MaterialTheme.typography.labelMedium,
                color = colorAcento,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TarjetaReceta(receta)
        }
    }
}
