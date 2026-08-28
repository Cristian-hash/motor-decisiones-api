# Arquitectura de Despliegue - Motor de Decisiones en la Nube

Este documento describe la arquitectura técnica del proyecto enfocado en el "Desarrollo de un sistema inteligente de aprendizaje basado en gamificación para la enseñanza de fundamentos de programación y patrones de diseño orientado a la toma de decisiones en la resolución de problemas" [cite: Desarrollo de un sistema inteligente de aprendizaje basado en gamificación para la enseñanza de fundamentos de programación y patrones de diseño orientado a la toma de decisiones en la resolución de problemas].

## 🏗️ 1. Topología Cloud (Microsoft Azure)

Diseñé una arquitectura distribuida nativa en la nube separando las responsabilidades en tres capas fundamentales para garantizar escalabilidad y desacoplamiento:

*   **La Vitrina (Frontend):**
   *   **Tecnología:** Angular 17+ (Standalone Components, Signals).
   *   **Infraestructura:** Azure Static Web Apps.
   *   **Justificación:** Un cliente ciego desplegado en una red CDN global. No consume recursos de cómputo del servidor, garantizando tiempos de carga en milisegundos mediante un pipeline de CI/CD automatizado.
*   **El Cerebro (Backend):**
   *   **Tecnología:** Java 21, Spring Boot 3, Spring Security.
   *   **Infraestructura:** Azure App Service (PaaS).
   *   **Justificación:** Lógica estricta de evaluación y motor de decisiones aislada en un entorno administrado. Procesa las reglas de negocio sin estado (Stateless) y se comunica con el cliente bajo estrictas políticas de CORS.
*   **La Bóveda (Persistencia):**
   *   **Tecnología:** PostgreSQL.
   *   **Infraestructura:** Azure Database for PostgreSQL (Flexible Server).
   *   **Justificación:** Base de datos inmutable y de alta disponibilidad. Protegida por firewalls y variables de entorno dinámicas, garantizando que ninguna credencial quede expuesta en el código fuente.

## ⚙️ 2. Patrones de Diseño (Motor de Decisiones)

El núcleo del sistema inteligente utiliza principios SOLID para evitar el acoplamiento y permitir la extensión de la gamificación sin modificar el código existente (Open/Closed Principle):

*   **Patrón Factory (Creación):** Implementado para la instanciación dinámica de los desafíos. El sistema recibe el nivel del estudiante y la Fábrica (`EvaluacionFactory`) determina y construye el tipo exacto de reto (lógica, opción múltiple, toma de decisiones) sin saturar los controladores con lógica de creación.
*   **Patrón Strategy (Comportamiento):** Utilizado para el motor de recompensas. Dependiendo de la resolución del alumno y el uso de mecánicas gamificadas, se inyecta un algoritmo de puntuación específico en tiempo de ejecución para calcular los Puntos de Experiencia (XP) otorgados.

## 🛡️ 3. Flujo de Seguridad y Comunicación (End-to-End)

La comunicación entre el Frontend en el CDN y el Backend en el PaaS está blindada mediante los siguientes mecanismos:

*   **Autenticación Stateless (JWT):** Spring Security valida las credenciales contra la base de datos utilizando encriptación `BCrypt` y emite un Token JWT. El servidor no almacena sesiones en memoria.
*   **Interceptores HTTP (Angular):** Una aduana configurada en el cliente intercepta todas las peticiones salientes y adjunta automáticamente el Token JWT (`Bearer`) en la cabecera `Authorization`.
*   **Políticas CORS Estrictas:** El filtro de seguridad de Spring Boot autoriza explícitamente y de forma exclusiva al dominio de producción en Azure Static Web Apps, rechazando cualquier petición de orígenes no verificados.

## 🚀 4. Integración y Despliegue Continuo (CI/CD)

El ciclo de entrega de valor está completamente automatizado:

*   **GitHub Actions:** El repositorio está vinculado a la infraestructura. Cada nuevo `push` a la rama principal dispara un flujo de trabajo que ejecuta el proceso de compilación (`ng build` para el frontend) y distribuye los artefactos resultantes directamente a los servidores globales de Microsoft Azure de forma invisible y segura.
* 