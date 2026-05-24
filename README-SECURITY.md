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

✅ Arquitectura Stateless  
✅ Validación mediante JWT firmado  
✅ Protección de endpoints privados  
✅ Filtros de autenticación personalizados  
✅ Integración con Spring Security  
✅ Separación clara entre autenticación y autorización

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

---

## 🎯 Ejemplo Conceptual

### 🔐 Autenticación

```text
“Demuestra quién eres”
```

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