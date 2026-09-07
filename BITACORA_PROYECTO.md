# Bitácora del proyecto: Minuta Nutricional

## Objetivo del proyecto

Aplicación móvil básica creada en Android Studio con Kotlin y Jetpack Compose. Permite iniciar sesión, registrar usuarios, recuperar contraseña y revisar una minuta nutricional semanal.

## Estado actual

- Proyecto desarrollado con Kotlin y Jetpack Compose.
- Repositorio remoto configurado: `https://github.com/elwerneira/Minuta.git`.
- Última validación realizada: compilación exitosa con `:app:compileDebugKotlin`.
- El proyecto se encuentra funcional para las pantallas y flujos principales.

## Trabajo realizado

### Pantallas y navegación

- Se creó la pantalla de inicio de sesión.
- Se creó la pantalla de registro de usuario.
- Se creó la pantalla de recuperación de contraseña.
- Se creó la pantalla de minuta semanal con recetas de lunes a domingo y un día libre.
- Se migró la navegación a `NavController` y `NavHost`.
- Al cerrar sesión, la aplicación vuelve al login y no permite regresar a la minuta con el botón Atrás.

### Registro e inicio de sesión

- Se validan campos vacíos y formato de correo.
- Los usuarios se guardan en la colección `usuariosPrueba` mientras la aplicación está abierta.
- Después de registrar una cuenta, se realiza inicio de sesión automático.
- La minuta muestra un mensaje de bienvenida con el nombre del usuario registrado o autenticado.
- Al cerrar sesión se limpia el nombre del usuario actual.

### Interfaz, usabilidad y accesibilidad visual

- Se agregaron mensajes visuales para errores de login, registro y recuperación de contraseña.
- Se eliminó la dependencia de sonidos para confirmar acciones.
- Se agregó un popup de bienvenida al entrar a la minuta.
- Al seleccionar un día, aparece una confirmación visual compacta con ícono y texto.
- Los botones de días indican el estado seleccionado/no seleccionado.
- Las pantallas de formularios y la minuta permiten desplazamiento vertical en pantallas pequeñas.

### Kotlin y datos

- Se usa `data class Receta` para guardar información de cada receta.
- Se usa `arrayOf` en `recetasSemanales` para guardar la minuta de la semana.
- Se usa `mutableStateListOf` en `usuariosPrueba` para los usuarios disponibles durante la ejecución.
- Se utilizan variables `var`, valores `val`, tipos `String`, `Int`, `Boolean` y `Regex`.
- Se utilizan condicionales `if` y `when`, además de recorridos con `forEach`.
- Se añadieron proteínas y carbohidratos a los datos de cada receta.
- Se dejaron comentarios simples para identificar variables, tipos y colecciones, con nivel introductorio.

## Componentes UI presentes

- Inputs: `OutlinedTextField`.
- Botones: `Button`, `TextButton`, `OutlinedButton` e `IconButton`.
- Textos: `Text`.
- Grilla de días: `LazyVerticalGrid`.
- Combo box: `ExposedDropdownMenuBox`.
- Radio buttons: `RadioButton`.
- Check list: `Checkbox`.
- Vínculos de navegación: botones de texto para registro y recuperación de contraseña.

## Pendientes

### Pendiente para una mejora futura

1. **Tabla nutricional visual.**
   - Los datos de calorías, proteínas y carbohidratos ya existen en `Receta`.
   - Falta mostrarlos como una tabla simple dentro de cada receta.
   - Este punto es recomendable porque el instructivo de Semana 3 menciona tablas.

2. **Pruebas manuales en emulador o dispositivo.**
   - Login con usuario existente.
   - Registro y auto login.
   - Validaciones de campos y correo.
   - Recuperación de contraseña.
   - Selección de día, scroll y cierre de sesión.

3. **Entrega final por Git y AVA.**
   - Revisar cambios con `git status`.
   - Crear commit y ejecutar `git push`.
   - Comprimir la carpeta completa del proyecto en formato `.zip`.
   - Subir el archivo solicitado a AVA.

### Opcional, fuera del alcance actual

- Probar la aplicación con TalkBack.
- Corregir la advertencia deprecada de `menuAnchor()`.
- Guardar usuarios en una base de datos o en almacenamiento permanente.
- Incorporar una API o datos nutricionales reales.

## Cómo continuar

1. Ejecutar la app en el emulador y revisar los flujos principales.
2. Si se decide completar el componente pendiente, agregar la tabla nutricional usando `Column`, `Row` y `Text`.
3. Revisar visualmente que el contenido se vea completo en una pantalla pequeña.
4. Hacer el commit final, subir al repositorio y preparar el archivo `.zip` de entrega.

## Pruebas sugeridas

| Acción | Resultado esperado |
|---|---|
| Iniciar con un usuario existente | Se abre la minuta y se muestra bienvenida. |
| Registrar un usuario nuevo | Se crea el usuario y se abre la minuta automáticamente. |
| Dejar campos vacíos | Se muestra un mensaje visual de error. |
| Elegir un día de la minuta | Cambia la receta y se muestra confirmación visual. |
| Deslizar la minuta | Se puede llegar a toda la receta y al botón Cerrar sesión. |
| Cerrar sesión | Se vuelve al login y se limpia la sesión actual. |

## Nota para Semana 3

El proyecto ya evidencia sintaxis básica de Kotlin, variables, tipos de datos, funciones, condicionales, colecciones y componentes de Jetpack Compose. La tabla nutricional es el único componente del instructivo que se ha decidido dejar para una mejora posterior.
