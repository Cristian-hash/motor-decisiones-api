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
* Eliminación en cascada (cascade)

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
- [ ] **Fase 5:** API de Lecciones Inteligentes (Siguiente paso)
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

---

## 📡 Endpoints Principales

### 1️⃣ Health Check (Ping)
Verifica que el recepcionista del servidor está vivo.
```http
GET http://localhost:8080/api/v1/test/ping


### ✅ Respuesta esperada

```
¡El Motor de Decisiones está vivo y escuchando a los Arquitectos!
```
POST http://localhost:8080/api/v1/evaluaciones
Content-Type: application/json

{
  "usuarioId": 1,
  "leccionId": 1,
  "opcionSeleccionadaId": 1
}
.....
{
  "esCorrecto": true,
  "mensajeJustificacion": "¡Exacto! El patrón estrategia usa interfaces para delegar el comportamiento."
}
---
{
  "mensaje": "Error: Opción con ID 999 no encontrada",
  "codigoEstado": 404,
  "fecha": "2026-05-01T06:30:15.123"
}
---

## 🧠 Filosofía del Sistema

Este proyecto está construido bajo una idea clave:

> *Un buen desarrollador escribe código.*
> *Un arquitecto toma decisiones y entiende sus consecuencias.*

---

## 🧭 Enfoque Arquitectónico

Este sistema está construido bajo principios SOLID y Arquitectura Limpia, siguiendo reglas inquebrantables:

* **Responsabilidad Única por Capa:** * Los **Controllers** son "recepcionistas": solo traducen JSON a Java y delegan (máximo 3 líneas de código).
    * Los **Services** son el "cerebro": aquí vive la lógica de evaluación, cálculo de puntos y transacciones (`@Transactional`).
    * Los **Repositories** son la "memoria": interfaces que dialogan con PostgreSQL.
* **Seguridad Fronteriza (DTOs):** Las entidades de la base de datos NUNCA viajan a internet. Se utilizan *Records* de Java (`RespuestaEstudianteDTO`, `FeedbackDTO`) para transportar solo la información necesaria.
* **Manejo de Caos (Global Exception Handling):** El sistema está blindado con `@RestControllerAdvice`. Si un cliente envía datos inválidos, el sistema no colapsa (Error 500); en su lugar, intercepta la falla y devuelve una respuesta elegante y estructurada (Error 404/400).

---

## 🧩 Visión a Futuro

* Sistema de evaluación basado en decisiones
* Feedback inteligente sobre elecciones arquitectónicas
* Gamificación del aprendizaje técnico
* Simulación de escenarios reales de software

---

## ✍️ Autor

**Crhistian Pacori**
Ingeniero de Sistemas enfocado en backend y arquitectura

---

## 🔥 Frase del Proyecto

> *Programar para entender. Diseñar para decidir.*
