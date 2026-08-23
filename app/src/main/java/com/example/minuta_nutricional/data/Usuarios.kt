package com.example.minuta_nutricional.data

data class Usuario(
    val correo: String,
    val clave: String,
    val nombre: String
)

val usuariosPrueba = listOf(
    Usuario("admin@gmail.com", "admin123", "Administrador"),
    Usuario("user@gmail.com", "user123", "Usuario Común")
)
