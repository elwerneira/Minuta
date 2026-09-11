package com.example.minuta_nutricional.data

import androidx.compose.runtime.mutableStateListOf

data class Usuario(
    val correo: String,
    val clave: String,
    val nombre: String
)

// Cinco usuarios y sus contraseñas de prueba.
val usuariosIniciales = arrayOf(
    Usuario("admin@gmail.com", "admin123", "Administrador"),
    Usuario("user@gmail.com", "user123", "Usuario Común"),
    Usuario("javier@gmail.com", "javier123", "Javier"),
    Usuario("elwer@gmail.com", "elwer123", "Elwer"),
    Usuario("francisca@gmail.com", "francisca123", "Francisca")
)

// Lista que permite agregar los perfiles creados desde Registro.
val usuariosPrueba = mutableStateListOf(*usuariosIniciales)
