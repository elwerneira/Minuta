package com.example.minuta_nutricional.data

// Almacena información de la receta.
data class Receta(
    val dia: String,
    val nombre: String, // Nombre de la receta.
    val ingredientes: String, // Ingredientes.
    val preparacion: String, // Pasos de preparación.
    val calorias: Int, // Número de calorías.
    val proteinas: Int, // Número de proteínas.
    val carbohidratos: Int, // Número de carbohidratos.
    val recomendacion: String // Recomendación para el usuario.
)

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
        "Cocer la pasta y mezclar con atún, tomate y brócoli.", 550, 32, 65, "Usa atún al agua y controla la porción de pasta."),
    Receta("Sábado", "Asado saludable", "Carne magra (posta negra), zapallo italiano, pimentón y papas.",
        "Cocinar la carne y vegetales a la parrilla o al horno.", 600, 40, 45, "Disfruta en familia, prefiere el agua sobre bebidas azucaradas."),
    Receta("Domingo", "Crema de verduras casera", "Zapallo, zanahoria, espinaca, apio y un toque de crema ligera.",
        "Cocer todas las verduras y licuar hasta obtener una crema suave.", 350, 10, 38, "Día ideal para una digestión ligera antes de empezar la semana.")
)
