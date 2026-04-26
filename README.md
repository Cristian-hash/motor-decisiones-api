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
* [ ] **Fase 4:** Lógica de Negocio (Services) + DTOs *(En progreso)*
* [ ] **Fase 5:** Seguridad (Spring Security + JWT)
* [ ] **Fase 6:** CI/CD + Despliegue en la nube (Azure)

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

## 📡 Endpoint de prueba

Verifica que el sistema está activo:

```http
GET http://localhost:8080/api/v1/test/ping
```

### ✅ Respuesta esperada

```
¡El Motor de Decisiones está vivo y escuchando a los Arquitectos!
```

---

## 🧠 Filosofía del Sistema

Este proyecto está construido bajo una idea clave:

> *Un buen desarrollador escribe código.*
> *Un arquitecto toma decisiones y entiende sus consecuencias.*

---

## 🧭 Enfoque Arquitectónico

Este sistema sigue una regla clara:

* **La lógica vive en los Services**
* **Los Controllers exponen**
* **Los Repositories acceden a datos**
* **Las entidades representan el dominio**

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
