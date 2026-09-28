package com.example.minuta_nutricional

import android.os.Bundle
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.minuta_nutricional.ui.screens.CatalogoRecetas
import com.example.minuta_nutricional.ui.screens.GestionRecetas
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.foundation.layout.Column
import com.example.minuta_nutricional.data.FirebaseUsuarios
import com.example.minuta_nutricional.ui.screens.HomeMenu
import com.example.minuta_nutricional.ui.screens.Login
import com.example.minuta_nutricional.ui.screens.MinutaSemanal
import com.example.minuta_nutricional.ui.screens.RecuperarClave
import com.example.minuta_nutricional.ui.screens.RecetaDetalle
import com.example.minuta_nutricional.ui.screens.Registro
import com.example.minuta_nutricional.ui.screens.Perfil
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
    var nombreUsuario by rememberSaveable { mutableStateOf("") }

    Scaffold { padding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(padding)
        ) {
            composable("login") {
                Login(
                    modifier = Modifier,
                    ingresar = { nombre ->
                        nombreUsuario = nombre
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    registrar = { navController.navigate("registro") },
                    recuperar = { navController.navigate("recuperar") }
                )
            }
            composable("registro") {
                Registro(
                    modifier = Modifier,
                    volver = { navController.popBackStack() },
                    registroExitoso = { nombre ->
                        nombreUsuario = nombre
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
            composable("recuperar") {
                RecuperarClave(Modifier) { navController.popBackStack() }
            }
            composable("home") {
                HomeMenu(
                    nombreUsuario = nombreUsuario,
                    verMinuta = { navController.navigate("minuta") },
                    verPerfil = { navController.navigate("perfil") },
                    gestionarRecetas = { navController.navigate("gestion-recetas") },
                    salir = {
                        FirebaseUsuarios.cerrarSesion()
                        nombreUsuario = ""
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }
            composable("perfil") {
                Perfil(
                    modifier = Modifier,
                    volver = { navController.popBackStack() },
                    perfilActualizado = { nombreUsuario = it },
                    cuentaEliminada = {
                        nombreUsuario = ""
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }
            composable("gestion-recetas") {
                GestionRecetas(volver = { navController.popBackStack() })
            }
            composable("minuta") {
                CatalogoRecetas(volver = { navController.popBackStack() }) { recetas ->
                    MinutaSemanal(
                        modifier = Modifier,
                        recetas = recetas,
                        abrirReceta = { dia -> navController.navigate("receta/${Uri.encode(dia)}") },
                        volver = { navController.popBackStack() }
                    )
                }
            }
            composable(
                route = "receta/{dia}",
                arguments = listOf(navArgument("dia") { type = NavType.StringType })
            ) { entrada ->
                val dia = entrada.arguments?.getString("dia")
                CatalogoRecetas(volver = { navController.popBackStack() }) { recetas ->
                    val receta = recetas.find { it.dia == dia }
                    if (receta != null) {
                        RecetaDetalle(receta = receta, volver = { navController.popBackStack() })
                    } else {
                        Column {
                            Text("La receta de este día ya no está disponible.")
                            TextButton(onClick = { navController.popBackStack() }) { Text("Volver") }
                        }
                    }
                }
            }
        }
    }
}
