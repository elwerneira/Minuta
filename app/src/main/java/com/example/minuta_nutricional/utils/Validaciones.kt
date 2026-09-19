package com.example.minuta_nutricional.utils

private val patronCorreo = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]+$"
    .toRegex(RegexOption.IGNORE_CASE)

// Función de orden superior que permite reutilizar reglas de validación.
inline fun validar(valor: String, regla: (String) -> Boolean): Boolean = regla(valor)

// Extensión para validar los correos ingresados en los formularios.
fun String.esCorreoValido(): Boolean {
    return trim().matches(patronCorreo)
}
