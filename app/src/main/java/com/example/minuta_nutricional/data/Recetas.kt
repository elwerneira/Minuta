package com.example.minuta_nutricional.data

// Define el comportamiento común de los elementos mostrados en la minuta.
interface ElementoMinuta {
    val nombre: String
    fun resumen(): String
}

// Almacena información de la receta.
data class Receta(
    val dia: String,
    override val nombre: String, // Nombre de la receta.
    val ingredientes: String, // Ingredientes.
    val preparacion: String, // Pasos de preparación.
    val calorias: Int, // Número de calorías.
    val proteinas: Int, // Número de proteínas.
    val carbohidratos: Int, // Número de carbohidratos.
    val recomendacion: String // Recomendación para el usuario.
) : ElementoMinuta {
    // Entrega un resumen simple de los nutrientes de la receta.
    fun resumenNutricional(): String {
        return "$calorias kcal | Proteínas: $proteinas g | Carbohidratos: $carbohidratos g"
    }

    // Implementa la interfaz reutilizando el resumen nutricional de la receta.
    override fun resumen(): String {
        return resumenNutricional()
    }
}

// Recetas para los días.
val recetasSemanales = arrayOf(
    Receta("Lunes", "Pollo con arroz y ensalada", "Pechuga de pollo, arroz integral, lechuga y tomate.",
        "Cocinar el pollo a la plancha y servir con arroz y ensalada.", 520, 38, 50, "Aporta proteínas y fibra. Prefiere poca sal."),
    Receta("Martes", "Lentejas con verduras", "Lentejas, zanahoria, cebolla, zapallo y arroz.",
        "Cocer las lentejas con las verduras picadas.", 480, 23, 68, "Buena fuente de hierro y proteína vegetal."),
    Receta("Miércoles", "Pescado al horno con papas", "Filete de pescado, papas, limón y ensalada verde.",
        "Hornear el pescado con limón y acompañar con papas cocidas.", 500, 35, 45, "Incluye omega-3. Evita freír el pescado."),
    Receta("Jueves", "Tortilla de verduras", "Huevos, espinaca, zanahoria, cebolla y tomate.",
        "Mezclar los ingredientes y cocinar en sartén antiadherente.", 410, 25, 28, "Aporta proteína y verduras en una preparación simple."),
    Receta("Viernes", "Pasta integral con atún", "Pasta integral, atún al agua, tomate y brócoli.",
        "Cocer la pasta y mezclar con atún, tomate y brócoli.", 550, 32, 65, "Usa atún al agua y controla la porción de pasta.")
)

// Permite buscar la receta correspondiente a cada día de manera clara y directa.
val recetasPorDia: Map<String, Receta> = recetasSemanales.associateBy { receta -> receta.dia }
