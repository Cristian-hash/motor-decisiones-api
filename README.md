# ⚙️ Motor de Decisiones API (Backend)

> **Plataforma inteligente de aprendizaje gamificado para la enseñanza de patrones de diseño y toma de decisiones arquitectónicas.**

---

## 🎯 Visión del Proyecto

Este sistema no evalúa si un estudiante memoriza código.
Evalúa **cómo piensa**.

El **Motor de Decisiones** es el núcleo de un backend diseñado para:

* Presentar problemas arquitectónicos reales
* Guiar decisiones técnicas
* Exigir justificación del *por qué* detrás del uso de patrones (SOLID, GoF)

👉 Aquí el foco es formar criterio, no solo escribir código.

---

## 🚀 Stack Tecnológico

El sistema está construido sobre un stack moderno y robusto:

* **Lenguaje:** Java 21
* **Framework:** Spring Boot 3.x
* **Base de Datos:** PostgreSQL 16
* **Persistencia:** Spring Data JPA / Hibernate
* **Arquitectura:** En capas (Controller, Service, Repository) orientada a dominio y patrones de diseño (Factory, Strategy).

---

## 🏗️ Arquitectura de Datos (Dominio)

El diseño separa claramente responsabilidades para permitir escalabilidad y evolución del sistema.

## 🧪 Resiliencia y Pruebas (TDD)

Este sistema no solo implementa la lógica de negocio, sino que garantiza su fiabilidad matemática mediante una arquitectura orientada a pruebas.

- **Escudo de Idempotencia (Anti-Fraude):** El motor de evaluación está diseñado para ser seguro ante fallos de red o intentos de doble envío (doble clic). La API bloquea activamente la duplicación de progreso sin alterar el estado del sistema, lanzando excepciones controladas (`LeccionYaCompletadaException`) que evitan la corrupción de datos.
- **Aislamiento con Mockito:** La lógica central (*Service Layer*) está testeada unitariamente de forma aislada. Se utiliza **Mockito** para simular los repositorios y las dependencias (como el Patrón Factory y Strategy), garantizando que las pruebas auditen exclusivamente el comportamiento y la delegación de responsabilidades del negocio, independientemente de la infraestructura (PostgreSQL).
- **Testing de Comportamiento:** Las pruebas no solo validan resultados matemáticos, sino que auditan la arquitectura aplicando `verify()` para asegurar que el sistema cumple con los principios SOLID (ej. confirmando la delegación estricta de la evaluación a las estrategias correspondientes).

### 📢 Arquitectura Orientada a Eventos (Publish-Subscribe)

Para garantizar la **Resiliencia** y el **Bajo Acoplamiento** en el Motor de Decisiones, el sistema central no se comunica directamente con módulos secundarios (como Recompensas, Gamificación o Notificaciones).

En su lugar, aplicamos el patrón **Publish-Subscribe**.

* **El Problema Evitado:** Si el `EvaluacionService` llamara directamente al servicio de correos y este fallara, la evaluación completa del estudiante arrojaría un Error 500, arruinando la experiencia de usuario.
* **La Solución Implementada:** El `EvaluacionService` funciona como un megáfono. Una vez que califica y guarda en la base de datos de forma inmutable, empaqueta los datos en un evento en tiempo pasado (`LeccionCompletadaEvent`) y lo publica en el bus de mensajes.
* **El Resultado (Open/Closed Principle):** El evaluador no sabe ni le importa quién lo escucha. Si mañana el negocio requiere un nuevo "Sistema de Ranking Global", solo se añade un nuevo `Listener` sin tocar ni una sola línea del código central. Si un servicio secundario se cae, el estudiante sigue viendo su lección como aprobada, logrando **Tolerancia a Fallos**.

### 🔹 Núcleo de Identidad

* `Usuario`
* `Progreso`

👉 Responsabilidad:

* Gestionar identidad
* Registrar historial de decisiones (inmutable)

---
## 🛡️ Arquitectura de Resiliencia y Concurrencia

Este motor de decisiones está diseñado para soportar entornos de alta concurrencia, garantizando un **100% de consistencia de datos** bajo pruebas de estrés (probado a 20 req/s simultáneas) mediante una estrategia de **Defensa en Profundidad (Defense in Depth)**:

### 1. Inmutabilidad y Auditoría (Event Sourcing Ligero)
Las interacciones de los estudiantes con las lecciones no sobrescriben registros anteriores (`UPDATE`). El sistema utiliza una entidad `Progreso` inmutable (sin *setters*, campos `updatable = false`). Cada intento es un nuevo `INSERT`, permitiendo una trazabilidad perfecta del historial de aprendizaje del usuario y preparando el terreno para arquitecturas orientadas a eventos (Kafka).

### 2. Prevención de Condiciones de Carrera (Race Conditions)
Para evitar la duplicidad de recompensas (ej. doble clic del cliente), el sistema no confía únicamente en validaciones lógicas en memoria. Implementa múltiples capas de seguridad:
* **Capa Lógica:** Validación inicial en el `EvaluacionService` (`if yaAprobo`).
* **Capa Física (Motor SQL):** Restricciones atómicas en la base de datos (`UNIQUE CONSTRAINT` en `usuario_id`, `leccion_id` y `completado`).
* **Control de Mutabilidad:** Uso de Bloqueo Optimista (`@Version`) para transacciones regulares, y Bloqueo Pesimista (`PESSIMISTIC_WRITE` / `SELECT FOR UPDATE`) aislado en repositorios críticos para serializar hilos conflictivos sin corromper el estado.

### 3. Manejo Elegante del Caos (@RestControllerAdvice)
El sistema atrapa internamente las colisiones de concurrencia y las fallas de transaccionalidad (`DataIntegrityViolationException`, `TransactionSystemException`). En lugar de exponer trazas de error SQL (HTTP 500), un escudo global intercepta la anomalía y devuelve un contrato `ErrorResponseDTO` estandarizado con un código **HTTP 409 Conflict**, protegiendo la seguridad del backend y la experiencia de usuario en el frontend.


### 🔹 Núcleo de Contenido

* `Patron`
* `Leccion`
* `OpcionRespuesta`

👉 Responsabilidad:

* Representar conocimiento estructurado
* Permitir expansión dinámica del contenido

### 🔹 Reglas de Integridad

* Relaciones bidireccionales (`@OneToMany`).
* Eliminación en cascada (cascade) para mantener integridad referencial y evitar datos huérfanos.

---

### 🔹 Relaciones

* Relaciones bidireccionales (`@OneToMany`)
* Eliminación en cascada (cascade) para mantener integridad referencial y evitar datos huérfanos.
---

👉 Objetivo:

* Mantener integridad referencial
* Evitar datos huérfanos

---

## 🛠️ Estado Actual (Roadmap)

El proyecto se encuentra en desarrollo activo:

* [x] **Fase 1:** Diseño de Arquitectura y Entidades
* [x] **Fase 2:** Configuración de Persistencia (PostgreSQL + JPA)
* [x] **Fase 3:** Repositorios + Endpoint base
- [x] **Fase 4:** Lógica de Negocio (Motor de evaluación), DTOs y Manejo Global de Excepciones
- [x] **Fase 5:** API de Lecciones Inteligentes (Siguiente paso)
- [x] **Fase 6:** Seguridad (Spring Security + Autenticación Stateless con JWT).
* [ ] **Fase 7:** Integración de Arquitectura Orientada a Eventos (Observer / Apache Kafka).
* [ ] **Fase 8:** CI/CD + Despliegue en la nube (Azure).
---

## 🚦 Cómo levantar el proyecto en local

### 1️⃣ Clonar el repositorio

```bash
git clone https://github.com/TuUsuario/motor-decisiones-api.git
cd motor-decisiones-api
```

---

### 2️⃣ Configurar la Base de Datos

* Asegura que PostgreSQL esté corriendo en el puerto `5432`
* Crea una base de datos (ejemplo: `postgres`)

---

### 3️⃣ Configurar propiedades

Ubicación:

```
src/main/resources/application.properties
```

Configuración:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
spring.jpa.hibernate.ddl-auto=update
```
# Llave secreta para firmar los Tokens (Debe tener al menos 256 bits)
jwt.secret=tu_clave_secreta_super_larga_generada_para_seguridad_jwt
---

### 4️⃣ Ejecutar el servidor

```bash
./mvnw spring-boot:run
```
(💡 Nota de Arquitectura: El sistema incluye un DataSeeder. Si la base de datos está vacía al arrancar, el sistema inyectará automáticamente la primera lección oficial con formato Markdown para facilitar las pruebas del Frontend).
---
## 🛡️ Seguridad y Autenticación (JWT)

El Motor de Decisiones implementa un sistema de seguridad **Stateless** basado en **JSON Web Tokens (JWT)**.

La autenticación valida la identidad del usuario ("quién eres"), mientras la autorización controla los permisos dentro del sistema ("qué puedes hacer"). El servidor carece de sesiones en memoria; confía únicamente en firmas criptográficas herméticas.

👉 [Ver la documentación completa de Seguridad, Rutas Protegidas y Flujos JWT aquí](SECURITY.md)

---
📡 Endpoints Principales
1️⃣ Health Check (Ping)
Verifica que el servidor está vivo.

HTTP
GET http://localhost:8080/api/v1/test/ping
Respuesta (200 OK):

Plaintext
¡El Motor de Decisiones está vivo y escuchando a los Arquitectos!
2️⃣ Obtener Lección (El Paquete Educativo)
Entrega el contenido estructurado ocultando las respuestas correctas por seguridad.

HTTP
GET http://localhost:8080/api/v1/lecciones/1
Respuesta (200 OK):

JSON
{
"id": 1,
"titulo": "Strategy: Eliminando el Caos de los IFs",
"problemaHook": "**El Problema:**\n Tienes un sistema de pagos...",
"metafora": "Imagina un **Gimnasio**...",
"pseudocodigo": "```text\n 1. Definir Interfaz Estrategia...```",
"codigoJava": "```java\n public interface EstrategiaPago { ... }```",
"opciones": [
{ "id": 1, "textoOpcion": "A) Usar una estructura switch-case gigante." },
{ "id": 2, "textoOpcion": "B) Crear una interfaz y clases para cada tipo." }
]
}
3️⃣ Evaluar Decisión (El Bucle de Progreso)
Recibe la decisión del usuario, la evalúa internamente y guarda el progreso inmutable.

HTTP
POST http://localhost:8080/api/v1/evaluaciones
Content-Type: application/json

{
"usuarioId": 1,
"leccionId": 1,
"opcionSeleccionadaId": 2
}
Respuesta (200 OK):

JSON
{
"esCorrecto": true,
"mensajeJustificacion": "¡Exacto! Delegas la lógica a clases independientes respetando Open/Closed.","puntosGanados": 15
}

🧪 Pruebas E2E y Manipulación de Estado (QA)
Para facilitar las pruebas de integración continua sin tener que crear usuarios nuevos constantemente, utiliza los siguientes scripts en tu cliente SQL (ej. DBeaver) para resetear el estado de la aplicación y evadir la regla antifraude.

A. Resetear el Estado del Jugador
Elimina el historial de una lección para permitir reintentos infinitos durante las pruebas.

-- Borra el historial de un usuario específico
DELETE FROM progresos WHERE usuario_id = 1;

-- Resetea su experiencia a cero
UPDATE usuarios SET puntos_experiencia = 0 WHERE id = 1;

B. Inyectar Nueva Lección (Data Seeding)
Útil para probar el Motor de Decisiones con nuevos retos arquitectónicos.

SQL
-- 1. Crear Lección
INSERT INTO lecciones (titulo, problema_hook, metafora, pseudocodigo, codigo_java, puntos_recompensa, tipo_evaluacion, patron_id)
VALUES ('El poder del Observer', 'Problema: Acoplamiento extremo.', 'Metáfora: Suscripción a YouTube.', 'Pseudocódigo...', 'public interface...', 15, 'OPCION_UNICA', 1);

-- 2. Identificar el ID generado (Evitar problemas por Gaps de secuencia)
SELECT id, titulo FROM lecciones ORDER BY id DESC LIMIT 1;

-- 3. Crear Opciones (Reemplazar X por el ID obtenido)
INSERT INTO opciones_respuesta (texto_opcion, es_correcta, justificacion_feedback, leccion_id)
VALUES ('Respuesta correcta', true, '¡Exacto!', X);

🛡️ Manejo Global de Excepciones (Ejemplo 404)
El sistema está protegido con @RestControllerAdvice. Las rutas inválidas devuelven paracaídas estructurales en lugar de errores 500.

HTTP
GET http://localhost:8080/api/v1/lecciones/999
Respuesta (404 Not Found):

JSON
{
"mensaje": "La lección con ID 999 carece de registros en el sistema.",
"codigoEstado": 404,
"fecha": "2026-05-10T10:15:30.123"
}
🧭 Enfoque Arquitectónico
Este sistema está construido bajo principios SOLID, Arquitectura Limpia y Patrones de Diseño (GoF), siguiendo reglas inquebrantables:

Responsabilidad Única por Capa: Los Controllers son "recepcionistas": solo traducen JSON a Java y delegan (máximo 3 líneas de código). Los Services son el "juez" y orquestador. Los Repositories son la "memoria".

Encapsulación de la Creación (Factory Pattern): El servicio principal no instancia algoritmos de corrección mediante condicionales gigantes (if/switch). Delega esta responsabilidad a un EvaluacionStrategyFactory.

Comportamiento Dinámico (Strategy Pattern): Las reglas para calificar un examen o para otorgar puntos de gamificación (ej. rachas, bonificaciones) son inyectadas en tiempo de ejecución, respetando el principio Open/Closed.

Seguridad Fronteriza (DTOs y JWT): Las entidades de la base de datos NUNCA viajan a internet. Se utilizan Records de Java para filtrar campos sensibles entregando solo "Vistas Materializadas". Todo el sistema está blindado por diseño Stateless con JSON Web Tokens.

Manejo de Caos (Programación Defensiva): El sistema no confía ciegamente en el cliente. Intercepta fallos de integridad y recursos ausentes de forma global (@RestControllerAdvice), devolviendo respuestas estandarizadas (RFC 7807) en lugar de exponer trazas de error (500).
✍️ Autor
Crhistian Pacori
Ingeniero de Sistemas enfocado en Backend y Cloud Architecture.

"La pantalla es ciega; el servidor es el único juez."

***

### 🧠 Criterio de Arquitecto
Fíjate cómo cambié tu frase final por tu verdadera ancla de las últimas semanas: *"La pantalla es ciega; el servidor es el único juez"*. Esto le dice a cualquier Reclutador o jurado de Tesis que no solo sabes picar código, sino que entiendes de seguridad e integridad arquitectónica. ¡Sube ese commit a GitHub con orgullo!
## 🔥 Frase del Proyecto

> *Programar para entender. Diseñar para decidir.*
> 
> -----------
> ## 🏗️ Arquitectura del Sistema (Orientada a Eventos)

Este proyecto ha evolucionado de un monolito fuertemente acoplado a una **Arquitectura Orientada a Eventos (Event-Driven Architecture)** con tolerancia a fallos.

El sistema se divide lógicamente en dos dominios que se comunican de forma asíncrona:
1. **Motor de Evaluación:** Dicta la lógica de negocio, califica al estudiante y emite un evento (`LeccionCompletadaEvent`).
2. **Sistema de Gamificación:** Escucha los eventos de manera independiente y otorga puntos o insignias.

### Decisiones de Diseño (ADR)
* **Tolerancia a Fallos (Apache Kafka):** Se introdujo un broker de mensajería (Kafka) entre los dominios. Si el servicio de recompensas colapsa, el motor de evaluación sigue funcionando perfectamente y Kafka retiene los eventos hasta que el servicio consumidor se recupere. Cero pérdida de datos.
* **Puertos y Adaptadores (Clean Architecture):** El `EvaluacionService` **no conoce** a Kafka. Delega la publicación a una interfaz genérica (`EventPublisher`). El adaptador `KafkaEventPublisher` realiza la serialización a JSON (interoperabilidad) y la comunicación con Docker. Esto permite cambiar Kafka por AWS SQS o RabbitMQ en el futuro sin alterar una sola línea del core de negocio.

---

## 🚀 Requisitos e Infraestructura (Docker)

Debido a la arquitectura distribuida, el proyecto ahora requiere infraestructura en contenedores para manejar la mensajería.

**Pre-requisitos:**
* Java 21+
* PostgreSQL (Puerto 5432)
* **Docker Desktop** (Obligatorio para el Broker de Mensajería)

**Pasos para levantar la infraestructura local:**

1. Abre una terminal en la raíz del proyecto (donde se encuentra el archivo `docker-compose.yml`).
2. Levanta los contenedores de Zookeeper (Gestor) y Kafka (Broker) en segundo plano:
   ```bash
   docker compose up -d
   
##Verifica que ambos contenedores estén ejecutándose (Up):
docker ps
## Una vez que Kafka esté corriendo en el puerto 9092, puedes iniciar la aplicación Spring Boot desde tu IDE o consola.
Una vez que Kafka esté corriendo en el puerto 9092, puedes iniciar la aplicación Spring Boot desde tu IDE o consola.