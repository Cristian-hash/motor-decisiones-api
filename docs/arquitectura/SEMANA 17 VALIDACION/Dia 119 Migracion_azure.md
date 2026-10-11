# Guía de migración de datos: Localhost a Microsoft Azure

## Objetivo

Transferir las lecciones, opciones de respuesta y patrones desde la base de datos local hacia Azure, conservando las cuentas de los usuarios y sus progresos.

**Tablas involucradas:**
- `patrones`
- `lecciones`
- `opciones_respuesta`

**Tablas que se busca conservar:**
- `usuarios`
- `progresos`

> **Importante:** Antes de ejecutar la migración, verifica las claves foráneas que relacionan estas tablas. Si `progresos` depende de `lecciones` o `patrones`, eliminar los registros podría bloquear la operación o borrar datos relacionados mediante una eliminación en cascada. No continúes hasta confirmar que el procedimiento conservará los progresos.

## Fase 1: Preparación y respaldo

Antes de modificar Azure:

1. Abre pgAdmin y conéctate a la base de datos local.
2. Comprueba que contiene los datos que deseas migrar.
3. Realiza un respaldo de la base de datos de Azure para disponer de un punto de recuperación.
4. Verifica que las tablas de origen y destino tienen una estructura compatible.

## Fase 2: Exportación de datos locales

1. En pgAdmin, conéctate a la base de datos **LOCAL**.
2. Haz clic derecho sobre la base de datos y selecciona **Backup**.

### Pestaña General

- **Filename:** `migracion_maestra.backup`
- **Format:** `Custom`

### Pestaña Data Options

- Activa **Only data** para exportar únicamente los datos, sin la estructura de las tablas.

### Selección de tablas

En la pestaña **Objects**, navega hasta:

`Schemas → public → Tables`

Selecciona exclusivamente las siguientes tablas:

- `patrones`
- `lecciones`
- `opciones_respuesta`

Comprueba que no haya otras tablas seleccionadas.

3. Haz clic en **Backup**.
4. Verifica que pgAdmin haya finalizado la operación correctamente y que el archivo de respaldo exista en la ubicación seleccionada.

## Fase 3: Preparación de las tablas en Azure

Esta fase reemplazará los datos existentes en las tres tablas seleccionadas. Ejecuta estos pasos únicamente después de verificar las relaciones entre las tablas y confirmar que el procedimiento preservará los progresos de los usuarios.

1. En pgAdmin, conéctate a la base de datos de **AZURE**.
2. Abre la herramienta **Query Tool**.
3. Ejecuta las siguientes sentencias en el orden indicado:

```sql
DELETE FROM opciones_respuesta;
DELETE FROM lecciones;
DELETE FROM patrones;
```

El orden busca eliminar primero las opciones de respuesta y después los registros de las tablas que podrían estar relacionadas con ellas.

> **Precaución:** Si una clave foránea impide alguna eliminación, detén el procedimiento e investiga la relación. No desactives las restricciones ni utilices eliminaciones en cascada sin verificar sus consecuencias.

## Fase 4: Restauración de datos en Azure

1. En pgAdmin, haz clic derecho sobre la base de datos de **AZURE**.
2. Selecciona **Restore**.

### Pestaña General

- **Format:** `Custom or tar`
- **Filename:** selecciona `migracion_maestra.backup`.

### Pestaña Data Options

- Activa **Only data** para restaurar únicamente los datos.

3. Haz clic en **Restore**.
4. Espera a que termine el proceso y revisa los mensajes de pgAdmin para confirmar que la restauración se completó correctamente.

## Fase 5: Verificación

Después de restaurar los datos:

1. Comprueba que las tres tablas contienen los registros esperados.
2. Verifica que las cuentas de los usuarios siguen existiendo.
3. Comprueba que los progresos anteriores se conservan y siguen relacionados correctamente con sus lecciones.
4. Prueba el funcionamiento de las lecciones y sus opciones de respuesta desde la aplicación.
5. Revisa los registros de errores de PostgreSQL y de la aplicación.

Si alguna validación falla, detén el despliegue y utiliza el respaldo de Azure para recuperar la información afectada.

## Consideraciones de arquitectura

- **Estructura:** Debe existir previamente en Azure, ya que el procedimiento transfiere datos, no cambios en la estructura de la base de datos.
- **Integridad referencial:** Las claves foráneas protegen las relaciones entre los registros. Deben verificarse antes de eliminar datos.
- **Usuarios y progresos:** No basta con evitar eliminaciones directas en esas tablas; también deben comprobarse las posibles eliminaciones en cascada y las relaciones con las tablas migradas.
- **Recuperación:** Conservar un respaldo de Azure permite volver a un estado anterior si ocurre un problema.
- **Validación:** El mensaje de éxito de pgAdmin confirma la ejecución de la operación, pero no garantiza por sí solo que la aplicación conserve su comportamiento esperado.

## Resumen

El proceso consiste en respaldar los datos locales, verificar las relaciones de la base de datos, preparar las tablas de destino, restaurar los datos y comprobar que las cuentas y los progresos de los usuarios se conservan.