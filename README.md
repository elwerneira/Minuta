# Minuta Nutricional

Aplicación móvil desarrollada con Kotlin, Android Studio, Jetpack Compose y Material Design. Permite iniciar sesión, registrar usuarios temporalmente, recuperar contraseña de forma simulada y revisar una minuta semanal con información nutricional.

## Funcionalidades

- Login con validación de correo, contraseña y credenciales.
- Registro de usuarios en memoria con validación de correo duplicado y contraseña mínima.
- Recuperación de contraseña simulada para correos registrados.
- Minuta semanal con recetas, día libre y tabla de calorías, proteínas y carbohidratos.
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

## Ejecución

1. Abrir el proyecto en Android Studio.
2. Ejecutar la aplicación en un emulador o dispositivo Android.

## Restricciones actuales

Los usuarios y recetas se administran temporalmente en memoria. No se utiliza base de datos, autenticación remota ni envío real de correos.
