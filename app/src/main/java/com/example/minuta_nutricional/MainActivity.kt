package com.example.minuta_nutricional

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
    val navController = rememberNavController()

    Scaffold { padding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(padding)
        ) {
            composable("login") {
                Login(
                    modifier = Modifier,
                    ingresar = {
                        navController.navigate("minuta") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    registrar = { navController.navigate("registro") },
                    recuperar = { navController.navigate("recuperar") }
                )
            }
            composable("registro") {
                Registro(Modifier) { navController.popBackStack() }
            }
            composable("recuperar") {
                RecuperarClave(Modifier) { navController.popBackStack() }
            }
            composable("minuta") {
                MinutaSemanal(Modifier) {
                    navController.navigate("login") {
                        popUpTo("minuta") { inclusive = true }
                    }
                }
            }
        }
    }
}
