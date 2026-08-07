# ⚙️ Motor de Decisiones API (Backend)

> **Plataforma inteligente de aprendizaje gamificado para la enseñanza de patrones de diseño y toma de decisiones arquitectónicas.**

---

# 🎯 Visión del Proyecto

Este sistema evalúa **cómo piensa un estudiante**, más allá de verificar si recuerda una sintaxis o un fragmento de código.

El **Motor de Decisiones** constituye el núcleo del backend y tiene como objetivo:

- Presentar problemas arquitectónicos reales.
- Guiar decisiones técnicas.
- Solicitar la justificación del **por qué** detrás del uso de principios SOLID y patrones GoF.

> Aquí el objetivo principal es desarrollar criterio de ingeniería.

---

# 🚀 Stack Tecnológico

- **Lenguaje:** Java 21
- **Framework:** Spring Boot 3.x
- **Base de Datos:** PostgreSQL 16
- **Persistencia:** Spring Data JPA / Hibernate
- **Arquitectura:** En capas (Controller, Service, Repository) orientada a dominio, eventos y patrones de diseño.

---

# 🏗️ Arquitectura del Sistema (Event-Driven Architecture)

El proyecto evolucionó desde un monolito fuertemente acoplado hacia una **Arquitectura Orientada a Eventos (Event-Driven Architecture)**, priorizando:

- Bajo acoplamiento.
- Alta cohesión.
- Resiliencia.
- Escalabilidad.

El sistema está dividido en dos dominios independientes.

## Dominio 1 — Motor de Evaluación

Responsable de:

- Resolver la lógica de negocio.
- Evaluar respuestas.
- Registrar el progreso.
- Publicar un evento cuando una evaluación termina.

Evento emitido:

```text
LeccionCompletadaEvent
```

---

## Dominio 2 — Sistema de Gamificación

Responsable de:

- Escuchar eventos.
- Otorgar experiencia.
- Entregar insignias.
- Desbloquear recompensas.

Este dominio funciona completamente desacoplado del Motor de Evaluación.

---

# 📢 Publish-Subscribe mediante Apache Kafka

## Problema arquitectónico

Si el `EvaluacionService` llamara directamente a:

- Gamificación
- Correos
- Recompensas
- Notificaciones

cualquier falla en uno de ellos podría afectar toda la evaluación del estudiante.

---

## Solución

El Motor de Evaluación únicamente publica un evento.

Kafka funciona como intermediario.

```text
EvaluacionService
        │
        ▼
LeccionCompletadaEvent
        │
        ▼
      Kafka
   ┌────┴────┐
   ▼         ▼
Gamificación Notificaciones
```

Cada consumidor procesa el evento cuando esté disponible.

Si uno de ellos se detiene temporalmente, Kafka conserva el mensaje hasta que el servicio vuelva a estar operativo.

---

# 🧩 Clean Architecture

El dominio permanece completamente independiente de la infraestructura.

El `EvaluacionService` solamente conoce una abstracción:

```java
EventPublisher
```

La implementación concreta pertenece a infraestructura.

Ejemplo:

```text
EvaluacionService
        │
        ▼
EventPublisher
        │
        ▼
KafkaEventPublisher
```

Gracias a ello, Kafka podría reemplazarse por:

- RabbitMQ
- AWS SQS
- Google Pub/Sub

sin modificar la lógica del negocio.

---

# 🛡️ Arquitectura de Resiliencia y Concurrencia

El sistema garantiza consistencia incluso bajo múltiples usuarios trabajando simultáneamente.

---

## 1. Inmutabilidad (Event Sourcing Ligero)

La entidad `Progreso` representa un historial inmutable.

Características:

- Sin setters.
- Campos `updatable = false`.
- Cada intento genera un nuevo registro.

En lugar de modificar información anterior:

```text
Intento 1 → INSERT

Intento 2 → INSERT

Intento 3 → INSERT
```

Así se conserva todo el historial del aprendizaje.

---

## 2. Prevención de Condiciones de Carrera

El sistema implementa varias capas de protección.

### Primera capa

Validaciones dentro del `EvaluacionService`.

---

### Segunda capa

Restricciones físicas en PostgreSQL.

Ejemplo:

```sql
UNIQUE CONSTRAINT
```

La base de datos garantiza la unicidad incluso cuando llegan múltiples solicitudes simultáneamente.

---

### Tercera capa

Control de concurrencia.

Se utilizan:

- `@Version` (Optimistic Locking)
- Bloqueos pesimistas en operaciones críticas.

---

# 🛡️ Manejo Global de Excepciones

Todas las excepciones son interceptadas mediante:

```java
@RestControllerAdvice
```

Por ejemplo:

```text
DataIntegrityViolationException
```

se transforma en:

```http
HTTP 409 Conflict
```

El frontend recibe una respuesta uniforme y el backend mantiene un contrato consistente.

---

# 🧭 Arquitectura por Capas

Cada capa posee una responsabilidad claramente definida.

## Controller

Responsabilidad:

- Recibir HTTP.
- Convertir JSON a objetos Java.
- Delegar al Service.

Jamás contiene reglas del negocio.

---

## Service

Responsabilidad:

- Orquestar.
- Aplicar reglas.
- Coordinar casos de uso.

Representa el cerebro del sistema.

---

## Repository

Responsabilidad:

- Acceder a PostgreSQL.
- Leer información.
- Guardar información.

Funciona como la memoria permanente.

---

# 🧱 Arquitectura de Frontend Ciego (Desacoplamiento Total)

Este proyecto rechaza deliberadamente el uso de aplicaciones monolíticas acopladas (como renderizado de vistas con Thymeleaf o Vaadin). En su lugar, se implementa una arquitectura distribuida donde el Backend y el Frontend operan de forma 100% independiente:

* **El Servidor es el Único Juez (Spring Boot):** La lógica de evaluación, el cálculo de puntajes y la validación de integridad viven protegidos detrás de una API REST. El backend es *stateless* y se defiende mediante filtros de seguridad JWT y escudos de idempotencia.
* **El Cliente es Ciego (Angular Standalone):** El frontend actúa exclusivamente como una interfaz de usuario esclava de los datos. Se limita a capturar eventos (`click`, `input`), empaquetarlos en DTOs seguros y despacharlos mediante peticiones asíncronas. Angular jamás evalúa si una respuesta es correcta o incorrecta, erradicando por completo el riesgo de manipulación de código en el navegador por parte del usuario final.
* **Seguridad Fronteriza Automatizada:** Las credenciales (JWT) no contaminan la capa de servicios del cliente. Se inyectan dinámicamente utilizando un `HttpInterceptor` global, garantizando que todo el tráfico saliente esté autenticado por defecto.
* **Entrada (Backend):** Las peticiones cruzadas se gestionan mediante una política de **CORS Global** en la capa de seguridad (`SecurityConfig`), evitando el uso repetitivo y riesgoso de anotaciones `@CrossOrigin` en los controladores.

### 📊 Diagrama de Secuencia End-to-End (Cliente Ciego ➔ Backend)

```mermaid
graph LR
    %% Definición de Estilos
    classDef frontend fill:#dd0031,stroke:#fff,stroke-width:2px,color:#fff;
    classDef aduana fill:#fbc02d,stroke:#fff,stroke-width:2px,color:#000;
    classDef backend fill:#6db33f,stroke:#fff,stroke-width:2px,color:#fff;
    classDef user fill:#8e44ad,stroke:#fff,stroke-width:2px,color:#fff;

    %% Nodos
    U((🧑‍🎓 Alumno)):::user
    
    subgraph ANGULAR ["Frontend Ciego (Angular)"]
        HTML["🖼️ app.component.html<br>La Vitrina (@if, @for)"]:::frontend
        TS["⚙️ app.component.ts<br>El Empacador"]:::frontend
        SVC["🚚 leccion.service.ts<br>El Mensajero"]:::frontend
    end

    subgraph FRONTERA ["Red / Seguridad"]
        INT{"🦾 auth.interceptor.ts<br>El Brazo Robótico"}:::aduana
    end

    subgraph SPRING_BOOT ["El Juez (Backend)"]
        API["🧠 Controller / Service<br>Motor de Decisiones"]:::backend
    end

    %% Flujo de datos
    U -- "1. Lee y hace (click)" --> HTML
    HTML -- "2. Pasa el ID" --> TS
    TS -- "3. Crea RespuestaDTO" --> SVC
    SVC -- "4. Inicia Petición HTTP" --> INT
    INT -- "5. Inyecta Header: Bearer JWT" --> API
    API -- "6. Devuelve FeedbackDTO" --> HTML

---

# 🔐 DTOs (Seguridad Fronteriza)

Las entidades de la base de datos permanecen dentro del servidor.

Hacia Internet únicamente viajan DTOs.

Ejemplo:

```text
Cliente

↓

DTO

↓

Controller

↓

Service

↓

Entity

↓

Repository
```

De esta forma se protege la estructura interna del dominio.

---

# 🧠 Factory + Strategy

Las responsabilidades permanecen completamente separadas.

---

## 1. EvaluacionService

Responsabilidad:

- Coordinar.
- Delegar.
- Orquestar.

Jamás realiza el cálculo.

---

## 2. EvaluacionStrategyFactory

Responsabilidad:

Elegir la estrategia adecuada.

Ejemplo:

```text
Pregunta de opción múltiple

↓

MultipleChoiceStrategy
```

o

```text
Caso arquitectónico

↓

DecisionArquitectonicaStrategy
```

Su única tarea consiste en entregar la herramienta correcta.

---

## 3. EstrategiaEvaluacion

Responsabilidad:

Resolver el algoritmo específico de evaluación.

Cada estrategia contiene únicamente su propia lógica.

Ejemplo:

```text
MultipleChoiceStrategy

↓

calificar()
```

Otra estrategia podría implementar:

```text
DecisionArquitectonicaStrategy

↓

calificar()
```

Cada una evoluciona de forma independiente.

---

# 🚦 Infraestructura Local

## 1. Clonar el proyecto

```bash
git clone [https://github.com/TuUsuario/motor-decisiones-api.git](https://github.com/TuUsuario/motor-decisiones-api.git)

cd motor-decisiones-api
```

---

## 2. Levantar Kafka

```bash
docker compose up -d
```

Se iniciarán:

- Zookeeper
- Apache Kafka

---

## 3. Configurar PostgreSQL

Crear una base de datos.

Configurar:

```properties
spring.datasource.username=tu_usuario

spring.datasource.password=tu_password

jwt.secret=tu_clave_secreta_super_larga
```

---

## 4. Ejecutar Spring Boot

```bash
./mvnw spring-boot:run
```

Durante el inicio se ejecutará automáticamente el `DataSeeder`.

---

# 🔐 Seguridad (JWT)

La autenticación utiliza:

- JSON Web Token (JWT)
- Arquitectura Stateless

Cada petición protegida incluye un token válido.

---

# 📡 Endpoints

## Obtener una lección

```http
GET /api/v1/lecciones/1
```

Entrega el contenido educativo ocultando las respuestas correctas.

---

## Evaluar una decisión

```http
POST /api/v1/evaluaciones
```

Ejemplo:

```json
{
  "usuarioId": 1,
  "leccionId": 1,
  "opcionSeleccionadaId": 2
}
```

El backend:

1. Evalúa.
2. Guarda el progreso.
3. Publica el evento.
4. Finaliza la operación.

---

# 🛠️ Roadmap

## Completado

- [x] Diseño del dominio
- [x] Persistencia
- [x] Lógica de negocio
- [x] Manejo global de excepciones
- [x] Seguridad JWT
- [x] Arquitectura Orientada a Eventos (Apache Kafka)

## Próximas etapas

- [ ] CI/CD
- [ ] Despliegue en la nube
- [ ] Observabilidad
- [ ] Monitoreo
- [ ] Pruebas de carga

---

# ✍️ Autor

**Crhistian Pacori**

Ingeniero de Sistemas especializado en Backend y Cloud Architecture.

> *"La pantalla presenta información; el servidor aplica las reglas del negocio."*

> *"Programar para entender. Diseñar para decidir."*

---