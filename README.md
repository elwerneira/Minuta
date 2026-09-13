# Minuta Nutricional

Aplicación móvil desarrollada con Kotlin, Android Studio, Jetpack Compose y Material Design. Permite iniciar sesión, registrar usuarios temporalmente, recuperar contraseña de forma simulada y revisar una minuta semanal con información nutricional.

## Funcionalidades

- Login con validación de correo, contraseña y credenciales.
- Registro de usuarios en memoria con validación de correo duplicado y contraseña mínima.
- Recuperación de contraseña simulada para correos registrados.
- Minuta semanal con recetas, día libre y tabla de calorías, proteínas y carbohidratos.
- Mensajes visuales de confirmación y error para apoyar la accesibilidad.

## Conceptos Kotlin aplicados

|----------------------|-----------------------------------------------------------------------------------------|
| Concepto             | Aplicación en el proyecto                                                               |
|----------------------|-----------------------------------------------------------------------------------------|
| `arrayOf`            | Almacena los cinco usuarios iniciales en `usuariosIniciales`.                           |
| Lista mutable        | `usuariosPrueba` permite registrar usuarios durante la ejecución.                       |
| Colecciones          | Se utilizan `find`, `any`, `forEach` y `associateBy` para usuarios, opciones y recetas. |
| `Map`                | `recetasPorDia` busca la receta seleccionada mediante el nombre del día.                |
| Interfaz             | `ElementoMinuta` es implementada por la clase `Receta`.                                 |
| Función de extensión | `String.esCorreoValido()` reutiliza la validación de correo.                            |
| Funciones privadas   | Componentes de apoyo como `DatoNutricional` mantienen la pantalla organizada.           |
|----------------------|-----------------------------------------------------------------------------------------|

## Ejecución

1. Abrir el proyecto en Android Studio.
2. Ejecutar la aplicación en un emulador o dispositivo Android.

## Restricciones actuales

Los usuarios y recetas se administran temporalmente en memoria. No se utiliza base de datos, autenticación remota ni envío real de correos.
