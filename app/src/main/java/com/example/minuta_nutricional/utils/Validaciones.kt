package com.example.minuta_nutricional.utils

private val patronCorreo = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]+$"
    .toRegex(RegexOption.IGNORE_CASE)

// Extensión reutilizable para validar los correos ingresados en los formularios.
fun String.esCorreoValido(): Boolean {
    return trim().matches(patronCorreo)
}
