# Minuta Nutricional

Aplicación móvil desarrollada con Kotlin, Android Studio, Jetpack Compose y Material Design. Permite iniciar sesión, registrar usuarios temporalmente, recuperar contraseña de forma simulada y revisar una minuta semanal con información nutricional.

## Funcionalidades

- Login con validación de correo, contraseña y credenciales.
- Registro de usuarios en memoria con validación de correo duplicado y contraseña mínima.
- Recuperación de contraseña simulada para correos registrados.
- Menú principal con acceso directo a la planificación semanal.
- Minuta adaptativa con recetas, día libre y resumen nutricional.
- Pantalla independiente de receta con ingredientes, preparación y nutrientes.
- Mensajes visuales de confirmación y error para apoyar la accesibilidad.

## Conceptos Kotlin aplicados

| Concepto             | Aplicación en el proyecto                                                               |
|----------------------|-----------------------------------------------------------------------------------------|
| `arrayOf`            | Almacena los cinco usuarios iniciales en `usuariosIniciales`.                           |
| Lista mutable        | `usuariosPrueba` permite registrar usuarios durante la ejecución.                       |
| Colecciones          | Se utilizan `find`, `any`, `forEach` y `associateBy` para usuarios, opciones y recetas. |
| `Map`                | `recetasPorDia` busca la receta seleccionada mediante el nombre del día.                |
| Interfaz             | `ElementoMinuta` es implementada por la clase `Receta`.                                 |
| Función de extensión | `String.esCorreoValido()` reutiliza la validación de correo.                            |
| Funciones privadas   | Componentes de apoyo como `DatoNutricional` mantienen la pantalla organizada.           |

## Funcionalidades avanzadas de Kotlin

| Funcionalidad | Implementación en el proyecto |
|---------------|-------------------------------|
| Función de extensión | `String.esCorreoValido()` en `utils/Validaciones.kt` valida el formato de correo y se reutiliza en Login, Registro y Recuperación de clave. |
| Función de orden superior | `validar(valor, regla)` en `utils/Validaciones.kt` recibe una lambda con la regla de validación. Se utiliza con `validar(correo) { it.esCorreoValido() }`. |
| Lambdas | Se emplean en los callbacks de navegación y en operaciones de colección como `find`, `any`, `forEach` y `associateBy`. |
| Manejo de excepciones | El registro de usuario usa `try/catch` al agregar el usuario temporal a `usuariosPrueba`, entregando un mensaje visual si ocurre un error inesperado. |

## Evidencias Semana 6

| Requisito | Implementación | Archivo principal |
|-----------|----------------|------------------|
| Pantallas modulares | Login, Registro, Recuperación, HomeMenu, Minuta y Receta se implementan como funciones `@Composable` conectadas con `NavHost`. | `MainActivity.kt` y `ui/screens` |
| Equivalentes de Views y ViewGroups | `Text`, `Button`, `Image` y `OutlinedTextField` son componentes visuales; `Column`, `Row`, `FlowRow` y `Scaffold` organizan la interfaz. | `ui/screens` |
| Diseño adaptativo | Los botones de los días cambian de una a dos columnas según el ancho disponible mediante `BoxWithConstraints` y `FlowRow`. | `MinutaScreen.kt` |
| Widgets y eventos | Botones, campos, checkbox, radio buttons y selector responden mediante `onClick`, `onValueChange` y otros callbacks. | `ui/screens` |
| Content Provider | `MinutaContentProvider` publica las recetas en `content://com.example.minuta_nutricional.recetas/recetas`; la app las consulta mediante `ContentResolver`. | `data/MinutaContentProvider.kt` y `data/RecetasProvider.kt` |
| Palette | La pantalla de receta obtiene un color dominante desde su ilustración y lo aplica como color ambiental, manteniendo un color de respaldo. | `RecetaScreen.kt` |
| Extensión KTX | `Drawable.toBitmap()` de Core KTX simplifica la conversión necesaria para analizar la ilustración con Palette. | `RecetaScreen.kt` |
| Accesibilidad | Controles grandes, mensajes con regiones semánticas, descripciones de estado y navegación con textos directos. | `ui/screens` |
| Repositorio Git | El proyecto se mantiene en `https://github.com/elwerneira/Minuta`. | Repositorio remoto `origin` |

## Ejecución

1. Abrir el proyecto en Android Studio.
2. Ejecutar la aplicación en un emulador o dispositivo Android.

## Restricciones actuales

Los usuarios y recetas se administran temporalmente en memoria. No se utiliza base de datos, autenticación remota ni envío real de correos.
