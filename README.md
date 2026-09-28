# Minuta Nutricional

Aplicación móvil desarrollada con Kotlin, Android Studio, Jetpack Compose y Material Design. Integra Firebase Auth para las cuentas y Realtime Database para los perfiles y el catálogo de recetas en el proyecto `minutanutricional`. Permite gestionar las recetas con una cuenta administradora y conserva recetas locales de ejemplo.

## Funcionalidades

- Login con Firebase Auth y lectura del perfil desde Realtime Database.
- Registro con contraseña mínima, control de correo duplicado mediante Firebase Auth y guardado del perfil nutricional.
- Recuperación de contraseña mediante correo enviado por Firebase Auth.
- Menú principal con acceso directo a la planificación semanal.
- Mi perfil: consulta de los datos guardados en Firebase y edición de nombre, alimentación y objetivo. El correo se muestra como solo lectura y el saludo se actualiza al guardar.
- Eliminar cuenta: confirmación con contraseña actual, eliminación del perfil y de Firebase Auth, y regreso al login. Si falla la eliminación en Auth, se intenta restaurar el perfil. La contraseña no se guarda en el dispositivo.
- Minuta adaptativa con recetas, día libre y resumen nutricional.
- Consulta del catálogo Firebase al abrir la minuta y el detalle, con carga, reintento y validación. La navegación identifica la receta por día, no por posición en el arreglo local.
- Gestión del catálogo compartido: cuentas administradoras pueden crear una receta por día, editar su contenido y eliminarla con confirmación. Los demás usuarios conservan lectura autenticada.
- Pantalla independiente de receta con ingredientes, preparación y nutrientes.
- Mensajes visuales de confirmación y error para apoyar la accesibilidad.

## Conceptos Kotlin aplicados

| Concepto             | Aplicación en el proyecto                                                               |
|----------------------|-----------------------------------------------------------------------------------------|
| `arrayOf`            | Almacena las cinco recetas. `usuariosIniciales` se conserva como ejemplo de la etapa anterior. |
| Lista mutable        | La consulta del ContentProvider construye una lista de recetas. `usuariosPrueba` ya no autentica ni registra cuentas. |
| Colecciones          | Se utilizan `find`, `any`, `forEach` y `associateBy` para usuarios, opciones y recetas. |
| `Map` y `Set`        | `recetasPorDia` conserva el ejemplo local de `associateBy`; `toSet` detecta días duplicados en el catálogo Firebase. |
| Interfaz             | `ElementoMinuta` es implementada por la clase `Receta`.                                 |
| Función de extensión | `String.esCorreoValido()` reutiliza la validación de correo.                            |
| Funciones privadas   | Componentes de apoyo como `DatoNutricional` mantienen la pantalla organizada.           |

## Funcionalidades avanzadas de Kotlin

| Funcionalidad | Implementación en el proyecto |
|---------------|-------------------------------|
| Función de extensión | `String.esCorreoValido()` en `utils/Validaciones.kt` valida el formato de correo y se reutiliza en Login, Registro y Recuperación de clave. |
| Función de orden superior | `validar(valor, regla)` en `utils/Validaciones.kt` recibe una lambda con la regla de validación. Se utiliza con `validar(correo) { it.esCorreoValido() }`. |
| Lambdas | Se emplean en los callbacks de navegación y en operaciones de colección como `find`, `any`, `forEach` y `associateBy`. |
| Manejo de excepciones | `FirebaseUsuarios` captura errores de Auth y Database y entrega mensajes visuales. Las cancelaciones de corutinas se propagan. |


## Pruebas de Semana 7


| Prueba | Resultado confirmado |
|--------|----------------------|
| Registro y login | Registro exitoso y nuevo login después de reiniciar la app. |
| Consulta y edición del perfil | Los cambios se guardaron en Firebase. |
| Eliminación de cuenta | La eliminación se comprobó en Firebase. |
| Lectura de recetas | La receta del viernes se mostró con el aviso de catálogo cargado desde Firebase. |
| Gestión de recetas | La cuenta administradora accedió al gestor y se confirmó creación, edición y eliminación de la receta temporal del sábado. |
| Cuenta sin rol administrador | Se confirmó la consulta de recetas sin acceso a Gestionar recetas. |

## Carga inicial de recetas en Firebase

1. Abrir Realtime Database, pestaña Datos, en el proyecto `minutanutricional`.
2. Crear o seleccionar el nodo `/recetas`. Si ya tiene datos, revisarlos y exportarlos como respaldo antes de importar: la importación sustituye el contenido del nodo seleccionado.
3. Importar `firebase/recetas-iniciales.json` dentro de `/recetas`, nunca en la raíz. El archivo contiene solo las cinco recetas; importarlo en la raíz podría reemplazar los perfiles de usuarios.
4. Publicar el contenido de `firebase/database.rules.json`, iniciar sesión y abrir la minuta. Debe aparecer “Catálogo cargado desde Firebase”.
5. Abrir una receta y comprobar que sus ingredientes y nutrientes corresponden al día seleccionado. Para verificar la fuente, cambiar temporalmente el nombre de una receta en la consola, volver a abrir la minuta y comprobar el cambio.

Si falla la carga, se muestra un mensaje con Reintentar o Usar recetas locales de ejemplo. La app no presenta datos locales como si provinieran de Firebase. La consulta se realiza al abrir cada pantalla; no hay sincronización continua ni caché persistente propia. El ContentProvider conserva las recetas locales de la entrega anterior.

`FirebaseRecetasTest` prueba orden semanal, catálogo vacío, días duplicados o desconocidos, campos incompletos, límites de texto y nutrientes, y claves estables por día. Estas pruebas no sustituyen la validación de conexión y permisos con Firebase real.

## Habilitar gestión de recetas

1. En Realtime Database, pestaña Reglas, publicar el contenido completo de `firebase/database.rules.json`. No modificar los datos de usuarios o recetas al hacer este paso.
2. En Authentication, Usuarios, copiar el UID de la cuenta que administrará el catálogo.
3. En Realtime Database, Datos, agregar desde la consola `/administradores/UID_DE_LA_CUENTA` con valor booleano `true`, no el texto `"true"`. No importar un archivo sobre la raíz. La asignación de roles se realiza únicamente desde la consola; la app no puede darse permisos.
4. Cerrar sesión y volver a entrar con esa cuenta. El menú mostrará Gestionar recetas. Si cambia un rol mientras la app está abierta, volver a iniciar sesión actualiza su visibilidad; las reglas verifican cada escritura.
5. Probar con una receta temporal para Sábado: crear, editar, abrir desde la minuta y eliminar confirmando el diálogo. Verificar los cambios en `/recetas/sabado` en la consola.
6. Probar con una cuenta sin rol: puede leer la minuta, no debe aparecer Gestionar recetas y las reglas deben rechazar escrituras al catálogo y a administradores.

Los identificadores son `lunes`, `martes`, `miercoles`, `jueves`, `viernes`, `sabado` y `domingo`. Las reglas validan su correspondencia con el día y los campos de la receta. La creación usa una transacción para evitar sobrescribir un día existente. La edición usa `setValue` sobre la receta de ese día: no tiene control de versiones y, si dos administradores editan simultáneamente, prevalece la última escritura. El borrado solo se ejecuta después de confirmar, nunca como parte de las pruebas automáticas. Si se borra la última receta, la gestión permite crear otra y la minuta informa que no hay recetas disponibles.

## Ejecución

1. Abrir el proyecto en Android Studio.
2. Ejecutar la aplicación en un emulador o dispositivo Android.
