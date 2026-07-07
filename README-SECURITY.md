## 🛡️ Módulo de Seguridad y Autenticación (JWT)

El Motor de Decisiones implementa un sistema de seguridad **Stateless** basado en **JSON Web Tokens (JWT)**.

La autenticación valida la identidad del usuario ("quién eres"), mientras que la autorización controla los permisos y accesos dentro del sistema ("qué puedes hacer").

---

## 🔐 1. Obtener el Token (Login)

Para acceder a los endpoints protegidos, el cliente debe autenticarse primero.

### Endpoint

```http
POST /api/v1/auth/login
```

### Acceso

```text
Público (permitAll)
```

### Body esperado

```json
{
  "email": "usuario@correo.com",
  "password": "mi_password_secreto"
}
```

---

## ✅ Respuesta Exitosa (200 OK)

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJpZCI..."
}
```

El servidor genera un JWT firmado criptográficamente que representa la identidad autenticada del usuario.

---

## 🚪 2. Acceder a Rutas Protegidas

Cualquier endpoint de negocio requiere un token válido.

Ejemplo:

```http
GET /api/v1/lecciones
```

---

## 📦 Header requerido

```http
Authorization: Bearer <TU_TOKEN_AQUI>
```

Ejemplo real:

```http
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

---

## ⚙️ Flujo de Seguridad

1. El usuario inicia sesión.
2. El servidor valida las credenciales.
3. Spring Security genera un JWT firmado.
4. El cliente almacena el token.
5. Cada petición futura envía el token en el Header Authorization.
6. El JwtAuthenticationFilter intercepta la petición.
7. Spring Security valida el token y autoriza el acceso.

---

## 🧠 Arquitectura de Seguridad

### 🔹 Autenticación (Quién eres)

Responsable de validar la identidad del usuario.

```text
Email + Password → JWT válido
```

---

### 🔹 Autorización (Qué puedes hacer)

Responsable de controlar acceso a recursos protegidos.

```text
Usuario autenticado → acceso permitido a endpoints privados
```

---

## 🧱 Componentes Principales

| Componente | Responsabilidad |
|---|---|
| `SecurityConfig` | Define reglas globales de seguridad |
| `JwtAuthenticationFilter` | Intercepta y valida el JWT |
| `Spring Security` | Gestiona autenticación y autorización |
| `SessionCreationPolicy.STATELESS` | Evita sesiones en memoria |
| `Authorization Header` | Transporta el token JWT |

---

## 🔥 Características de Seguridad

✅ Arquitectura Stateless sin sesiones en memoria.
✅ Validación mediante JWT firmado criptográficamente.
✅ Filtros de autenticación personalizados integrados con Spring Security.
✅ **Edge Security:** Validación estricta de Claims en la frontera del sistema para bloquear intrusos y cuentas suspendidas con latencia cero (sin consultar la base de datos).
✅ **Defensa en Profundidad:** Intercepción de fraudes y caducidad a nivel de Filtros (Filter Chain), devolviendo respuestas JSON estandarizadas.
✅ Separación clara entre Autenticación (401) y Autorización (403).

---

## ❌ Casos de Rechazo

El servidor rechazará la petición cuando:

- El token no existe
- El token expiró
- El token fue alterado
- El usuario carece de permisos

Respuesta típica:

```http
403 Forbidden
```
## 🛑 Intercepción en la Frontera (Edge Validation)

El sistema no espera a que las peticiones lleguen a la capa de Controladores o a la Base de Datos para ser rechazadas. Toda la validación criptográfica y de negocio ocurre en la frontera del sistema mediante el `JwtAuthenticationFilter`.

### 🛡️ Defensa en Profundidad (Manejo de Excepciones en Filtros)
En lugar de permitir que el servidor colapse con errores `500 Internal Server Error`, el filtro captura las anomalías criptográficas y utiliza `ObjectMapper` para escribir respuestas JSON nativas y estructuradas directamente en el flujo `HttpServletResponse`.

#### Caso 1: Caducidad (Flujo Normal)
* **Escenario:** El estudiante tardó demasiado y su sesión caducó.
* **Excepción atrapada:** `ExpiredJwtException`
* **Respuesta HTTP:** `401 Unauthorized`
* **Cuerpo JSON:**
```json
{
  "error": "Sesión terminada",
  "causa": "El token ha expirado. Por favor, inicie sesión nuevamente.",
  "codigoEstado": 401
}

 🎯 Ejemplo Conceptual

🔐 Autenticación

text
“Demuestra quién eres”


El usuario inicia sesión y obtiene un JWT.

---

### 🚪 Autorización

```text
“Define qué puedes hacer”
```

El sistema decide si el usuario puede acceder a recursos protegidos.

---

## 🧠 Regla Arquitectónica

> El backend controla la seguridad.
> El frontend solo transporta el token.
> La lógica de autorización vive en el servidor.

---

## 🚀 Demostración Funcional

### Escenario 1 — Sin Token

```http
GET /api/v1/lecciones
```

Resultado:

```http
403 Forbidden
```

---

### Escenario 2 — Con Token Válido

```http
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

Resultado:

```http
200 OK
```

---

## 🏛️ Principio Arquitectónico

> La seguridad moderna basada en JWT permite construir APIs desacopladas, escalables y sin sesiones persistentes en servidor.
> 
> 
> ---

## 🧪 Auditoría de Seguridad (Pruebas E2E)

El Motor de Decisiones cuenta con una muralla defensiva validada empíricamente mediante simulación de ataques. El sistema garantiza cero colapsos ante estos vectores:

### 🟢 Escenario 1: El Camino Feliz
* **Acción:** Petición con JWT íntegro y vigente.
* **Resultado:** `200 OK`. El sistema autoriza el acceso a la ruta protegida.

### 🔴 Escenario 2: El Intruso Torpe (Firma Rota o Texto Mutilado)
* **Acción:** Petición alterando o borrando la última letra del JWT en la cabecera.
* **Excepción:** `SignatureException` o `IllegalArgumentException`.
* **Resultado:** `401 Unauthorized`. El filtro rechaza la forma física o criptográfica del pase y devuelve el JSON de defensa.

### 🔴 Escenario 3: El Hacker Astuto (Payload Adulterado)
* **Acción:** Petición modificando el rol o email en jwt.io, manteniendo el formato Base64 intacto.
* **Excepción:** `SignatureException`.
* **Resultado:** `401 Unauthorized`. El motor matemático detecta la manipulación de datos y frena el acceso.

### 🟡 Escenario 4: El Ladrón de Tiempo (Expiración)
* **Acción:** Petición con un JWT legítimo pero con fecha de expiración caducada.
* **Excepción:** `ExpiredJwtException`.
* **Resultado:** `401 Unauthorized`. El filtro clasifica el evento como "Sesión terminada" e invita al usuario a renovar su acceso mediante un JSON amigable.