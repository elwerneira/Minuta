package com.example.minuta_nutricional

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.minuta_nutricional.ui.screens.Login
import com.example.minuta_nutricional.ui.screens.MinutaSemanal
import com.example.minuta_nutricional.ui.screens.RecuperarClave
import com.example.minuta_nutricional.ui.screens.Registro
import com.example.minuta_nutricional.ui.theme.Minuta_NutricionalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { Minuta_NutricionalTheme { AplicacionMinuta() } }
    }
}

@Composable
fun AplicacionMinuta() {
    var pantalla by remember { mutableStateOf("login") }
    Scaffold { padding ->
        when (pantalla) {
            "login" -> Login(
                modifier = Modifier.padding(padding),
                ingresar = { pantalla = "minuta" },
                registrar = { pantalla = "registro" },
                recuperar = { pantalla = "recuperar" }
            )
            "registro" -> Registro(Modifier.padding(padding)) { pantalla = "login" }
            "recuperar" -> RecuperarClave(Modifier.padding(padding)) { pantalla = "login" }
            else -> MinutaSemanal(Modifier.padding(padding)) { pantalla = "login" }
        }
    }
}
