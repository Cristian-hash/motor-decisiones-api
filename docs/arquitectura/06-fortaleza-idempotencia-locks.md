sequenceDiagram
autonumber
actor U as 🧑‍💻 Usuario Impaciente
participant API as 🛎️ Controller
participant S as 🧠 EvaluacionService
participant DB as 🗄️ PostgreSQL (Bóveda)

    Note over U, API: ¡Caos de Red! El usuario presiona "Enviar" 3 veces.
    
    par Peticiones Concurrentes
        U->>API: 📨 Clic 1 (Mismo milisegundo)
    and
        U->>API: 📨 Clic 2 (Mismo milisegundo)
    and
        U->>API: 📨 Clic 3 (Mismo milisegundo)
    end

    Note over API, S: 🛡️ Escudo 1: Idempotencia en Memoria
    API->>S: Peticiones entran al unísono
    S->>S: ¿Ya procesé esta lección hoy?
    Note over S: Las 3 peticiones pasan el filtro inicial<br/>porque la BD aún está vacía.

    Note over S, DB: 🛡️ Escudo 2: Lock en Base de Datos (El Juez Final)
    par Transacciones
        S->>DB: Intenta GUARDAR Progreso (Petición 1)
        S->>DB: Intenta GUARDAR Progreso (Petición 2)
        S->>DB: Intenta GUARDAR Progreso (Petición 3)
    end

    Note right of DB: 🔒 UNIQUE CONSTRAINT / LOCK<br/>"Un registro a la vez, los demás mueren"
    
    DB-->>S: ✅ Petición 1: Éxito (El asiento se ocupa)
    DB-->>S: ❌ Petición 2: DataIntegrityViolationException
    DB-->>S: ❌ Petición 3: DataIntegrityViolationException

    S-->>U: 🎉 200 OK (Puntos otorgados 1 sola vez)
    S-->>U: 🚫 Ignorado (Excepción atrapada por el Backend)
    S-->>U: 🚫 Ignorado (Excepción atrapada por el Backend)