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
* **Arquitectura:** En capas (Controller, Service, Repository) orientada a dominio

---

## 🏗️ Arquitectura de Datos (Dominio)

El diseño separa claramente responsabilidades para permitir escalabilidad y evolución del sistema.

### 🔹 Núcleo de Identidad

* `Usuario`
* `Progreso`

👉 Responsabilidad:

* Gestionar identidad
* Registrar historial de decisiones (inmutable)

---

### 🔹 Núcleo de Contenido

* `Patron`
* `Leccion`
* `OpcionRespuesta`

👉 Responsabilidad:

* Representar conocimiento estructurado
* Permitir expansión dinámica del contenido

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
- [ ] **Fase 6:** Seguridad (Spring Security + JWT)
- [ ] **Fase 7:** CI/CD + Despliegue en la nube (Azure)
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

---

### 4️⃣ Ejecutar el servidor

```bash
./mvnw spring-boot:run
```
(💡 Nota de Arquitectura: El sistema incluye un DataSeeder. Si la base de datos está vacía al arrancar, el sistema inyectará automáticamente la primera lección oficial con formato Markdown para facilitar las pruebas del Frontend).
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
"mensajeJustificacion": "¡Exacto! Delegas la lógica a clases independientes respetando Open/Closed."
}
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
Este sistema está construido bajo principios SOLID y Arquitectura Limpia, siguiendo reglas inquebrantables:

Responsabilidad Única por Capa: * Los Controllers son "recepcionistas": solo traducen JSON a Java y delegan (máximo 3 líneas de código).

Los Services son el "juez": aquí vive la lógica de evaluación, reglas de negocio y transacciones (@Transactional).

Los Repositories son la "memoria": interfaces que dialogan con PostgreSQL.

Seguridad Fronteriza (DTOs): Las entidades de la base de datos NUNCA viajan a internet. Se utilizan Records de Java para filtrar campos sensibles (como esCorrecta) entregando solo Vistas Materializadas.

Manejo de Caos (Programación Defensiva): El sistema no confía ciegamente en el cliente. Intercepta fallos de integridad y recursos ausentes devolviendo respuestas JSON amigables para el Frontend.

✍️ Autor
Crhistian Pacori
Ingeniero de Sistemas enfocado en Backend y Cloud Architecture.

"La pantalla es ciega; el servidor es el único juez."

***

### 🧠 Criterio de Arquitecto
Fíjate cómo cambié tu frase final por tu verdadera ancla de las últimas semanas: *"La pantalla es ciega; el servidor es el único juez"*. Esto le dice a cualquier Reclutador o jurado de Tesis que no solo sabes picar código, sino que entiendes de seguridad e integridad arquitectónica. ¡Sube ese commit a GitHub con orgullo!
## 🔥 Frase del Proyecto

> *Programar para entender. Diseñar para decidir.*