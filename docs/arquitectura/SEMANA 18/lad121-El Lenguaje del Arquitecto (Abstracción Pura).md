# Arquitectura de software: pensar más allá de los frameworks

## 1. Preguntas de reflexión arquitectónica

### ¿Por qué depender exclusivamente de los nombres de los frameworks limita tu visión y crecimiento como ingeniero?

Porque manejar los conceptos a alto nivel me da libertad. Esto me ayuda a abstraer los principios fundamentales de la programación, independientemente de las herramientas que utilice.

### Si cambiaras de lenguaje de programación mañana, ¿qué conceptos de tu sistema permanecerían exactamente iguales?

- **Patrones de diseño:** soluciones reutilizables para problemas comunes de diseño de software.
- **Estructuras de datos:** formas de organizar y manipular la información.
- **Persistencia de datos:** principios que permiten almacenar y recuperar información de forma duradera.

---

## 2. Guion para explicar la arquitectura de mi sistema

Diseñé un **sistema inteligente de aprendizaje gamificado** utilizando una arquitectura de **monolito modular**.

Todo comienza en la **interfaz de usuario reactiva**, donde el estudiante toma una decisión. Esta petición viaja de forma segura mediante un mecanismo de autenticación sin estado y llega a nuestro orquestador en el backend.

El **orquestador no decide las reglas del negocio; su responsabilidad es delegar**. Pasa la estructura de datos entrante a la capa que contiene la lógica de negocio.

Aquí es donde vive la lógica principal: mediante el patrón **Strategy**, el sistema evalúa la respuesta del estudiante, calcula el puntaje gamificado y decide si avanza al siguiente nivel.

Estas reglas de negocio representan el corazón del sistema y deben permanecer independientes de los detalles de infraestructura, como el servidor o el motor de base de datos.

Finalmente, el sistema prepara el nuevo progreso en una estructura de transporte y lo entrega a la capa de abstracción de datos, que se encarga de persistir el historial del usuario en un almacenamiento relacional alojado en una plataforma en la nube.

**Observo el flujo, comprendo las responsabilidades y separo estrictamente quién orquesta, quién decide y quién transporta los datos.**

---

## 3. Diccionario agnóstico de arquitectura

Utiliza esta tabla para traducir tu implementación actual al lenguaje de un arquitecto de software.

| Tecnología o componente | Concepto arquitectónico |
|---|---|
| **Java 21** | Lenguaje de programación orientado a objetos y fuertemente tipado. |
| **Spring Boot** | Framework para construir aplicaciones backend; facilita la inversión de control y la configuración de componentes. |
| **PostgreSQL** | Sistema de gestión de bases de datos relacionales con persistencia duradera. |
| **Angular** | Framework frontend para construir aplicaciones SPA (*Single Page Application*) mediante componentes. |
| **Azure App Service / Static Web Apps** | Servicios de alojamiento y ejecución de aplicaciones en la nube. |
| **JWT (JSON Web Token)** | Formato de token utilizado para transmitir declaraciones y gestionar mecanismos de autenticación o autorización. |
| **Docker** | Plataforma para empaquetar y ejecutar aplicaciones en contenedores aislados. |
| **Controller (`@RestController`)** | Punto de entrada de las peticiones HTTP y coordinador de la interacción con los servicios. |
| **Service (`@Service`)** | Componente que coordina operaciones y puede contener o invocar la lógica de negocio. |
| **Repository / JPA** | Abstracción para acceder y persistir datos; JPA define una especificación de persistencia para Java. |
| **DTO (Data Transfer Object)** | Estructura que permite transportar datos entre distintas capas o sistemas. |

---

## 4. Principios que debo recordar como arquitecto

### ¿Quién es el dueño real de cada responsabilidad?

- **Controller:** recibe las peticiones y delega el trabajo.
- **Lógica de negocio:** define qué debe ocurrir según las reglas del sistema.
- **Repository:** abstrae el acceso a los datos persistidos.
- **DTO:** transporta información sin asumir la responsabilidad de decidir las reglas del negocio.
- **Infraestructura:** proporciona los recursos necesarios para ejecutar el sistema.

### ¿Dónde vive la lógica?

La lógica de negocio vive en el dominio o en los componentes que implementan sus reglas, según la arquitectura elegida. Los controladores, los DTO y los repositorios deben evitar asumir decisiones que no les corresponden.

### ¿Qué debería permanecer estable?

Las reglas del negocio deberían cambiar lo menos posible cuando sustituimos un framework, un servidor o una tecnología de almacenamiento. Para lograrlo, debemos separar las decisiones del negocio de los detalles técnicos.

---

## 5. Resumen en una sola frase

**Un arquitecto de software no depende de memorizar herramientas: comprende las responsabilidades, identifica dónde vive la lógica y diseña un sistema cuyos principios pueden sobrevivir al cambio de tecnología.**

---

## 6. Mi esquema mental

- **Interfaz de usuario:** captura la decisión del estudiante.
- **Controller:** recibe la petición y delega.
- **Dominio / lógica de negocio:** evalúa, decide y aplica las reglas.
- **DTO:** transporta los datos.
- **Repository:** abstrae el acceso a los datos.
- **Base de datos:** conserva la información.
- **Infraestructura en la nube:** permite ejecutar y alojar el sistema.

### Mi regla de oro

**Las herramientas pueden cambiar; las responsabilidades deben estar claras y la lógica del negocio debe permanecer protegida de los detalles técnicos.**