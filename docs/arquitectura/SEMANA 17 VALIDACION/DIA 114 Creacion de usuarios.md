La Consulta SQL de Precisión (Cópiala y pégala en pgAdmin)
SQL
INSERT INTO usuarios (
email,
password,
fecha_registro,
puntos_experiencia,
version
)
VALUES (
'alumno_beta@tesis.com',
'$2a$10$W2neF9.6Agi6kAKVq8q3fec5dHW8KUA.b0VSIGdIZyErawqA25z6e',
CURRENT_TIMESTAMP,
0,
0
);
¿Por qué esta es la consulta "bien hecha"? (Análisis de Arquitecto)
La Contraseña Encriptada (password): Nunca puedes guardar la contraseña "123456" en texto plano. Si lo haces, Spring Security la leerá, se dará cuenta de que no es un Hash de BCrypt, y te lanzará un error 403 Forbidden cuando intentes hacer Login. El texto raro $2a$10... es la clave "123456" ya procesada por BCrypt, lista para que Spring la valide.

La Auditoría (fecha_registro): Usamos la función nativa CURRENT_TIMESTAMP de PostgreSQL para que registre la hora exacta de la creación de este alumno beta.

El Lienzo en Blanco (puntos_experiencia): Lo forzamos a 0. Así garantizamos que no sea NULL (lo cual haría explotar a Hibernate) y que tu patrón Strategy empiece a sumar recompensas limpiamente desde cero.

El Escudo Activo (version): Lo inicializamos en 0. Esto le indica al motor de concurrencia de Spring Boot que este es un registro virgen y que está listo para rastrear la primera actualización.

Exclusión de id e insignia: No incluimos el id porque PostgreSQL lo autogenerará mediante tu secuencia (GenerationType.IDENTITY), y omitimos la insignia porque al inicio el estudiante no tiene ninguna, por lo que Postgres insertará un NULL válido.

Tu plan de acción inmediato:

Ve a tu Query Tool en DBeaver o pgAdmin.

Ejecuta este INSERT.

Haz un SELECT * FROM usuarios WHERE email = 'alumno_beta@tesis.com'; para confirmar que se creó con un nuevo ID.

Ve a Postman, haz Login con el JSON {"email": "alumno_beta@tesis.com", "password": "123456"} y obtén tu Token JWT.

# 2-PLANTILLA — APRENDIZAJE CLAVE DEL DÍA

**🧱 Día 114 — Aprendizaje clave: Telemetría E2E y la "Caja Negra" en PostgreSQL**

**🧠 Qué entendí hoy (explicado simple)**
Entendí que el Motor de Decisiones no solo evalúa si el alumno "pasa" o "reprueba". Actúa como una caja negra de avión, grabando cada fricción y cada intento fallido de forma inmutable. La base de datos no es un almacén de estados finales, es un notario del proceso cognitivo del estudiante.

**⚙️ Cómo funciona (paso a paso real)**
1. **Instrumentación (El Guardián):** Aseguro que el `ProgresoRepository` ejecute un `INSERT` (append-only) y no un `UPDATE`, documentando cada choque con la lección en la tabla `progresos`.
2. **Inyección de Prueba:** Utilizo SQL puro para inyectar un alumno beta respetando el escudo arquitectónico (`@Version = 0`) y la encriptación BCrypt de Spring Security.
3. **Simulación de Fricción:** Desde Postman, lanzo peticiones erróneas (0 puntos, completado: false) y luego la decisión correcta (100 puntos, completado: true).
4. **Auditoría Forense:** Extraigo la telemetría conectándome a Azure Database for PostgreSQL y consultando la evolución exacta del aprendizaje, validando que PostgreSQL auditó la verdad inmutable detrás de la fachada de Angular.

**🧱 Regla de oro del día 👉** "El código asume, pero la telemetría garantiza. Si el sistema no guarda el historial de los errores, estoy perdiendo la información más valiosa sobre cómo aprende el usuario."

**❌ Qué pensaba antes**
Pensaba que una base de datos solo servía para guardar el puntaje final del alumno y si aprobó la lección, limpiando los intentos fallidos para ahorrar espacio.

**✅ Qué entiendo ahora**
Entiendo que "el error es información". La evidencia científica de mi tesis vive en la tabla `progresos`. La fricción (los intentos `completado = false` con `puntaje_obtenido = 0`) es necesaria para demostrar que el sistema guía al usuario hacia la decisión correcta arquitectónica.

**🔥 Diferencia clave**
* **Antes:** Postman dice "200 OK", confío a ciegas en que el usuario aprendió.
* **Ahora:** Audito el cruce de datos (`SELECT p.completado, p.puntaje_obtenido, u.puntos_experiencia...`) directo en producción (Azure) para garantizar la consistencia matemática del Gamification Strategy.

**🧠 Dónde vive cada cosa (ARQUITECTURA CLOUD)**
* **Lógica (quién decide):** `EvaluacionService` (orquesta el Patrón Strategy).
* **Ejecución (quién hace):** `ProgresoRepository` (el bibliotecario append-only).
* **Persistencia (quién guarda):** Azure Database for PostgreSQL (La Bóveda Inmutable).
* **Transporte (quién solo pasa datos):** Las consultas estructuradas de auditoría vía DBeaver.

**🧠 Frase ancla 👉** *"Postman evalúa la fachada, pero PostgreSQL audita la verdad inmutable."*