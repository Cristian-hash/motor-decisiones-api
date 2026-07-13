![Orquestación del Motor de Decisiones](images/03-escudo criptografico jwt.png)

@startuml
skinparam ArchimateBackgroundColor #FFFFFF
skinparam ActivityBackgroundColor #FFFFFF
skinparam ActivityBorderColor #333333
skinparam ArrowColor #333333

|🦹‍♂️ Atacante (Hacker)|
start
:1. Manipula el JWT;
:Modifica el Payload (Base64);
note right
Ejemplo:
"rol": "ADMIN"
end note

|🎫 Token JWT (Pasaporte)|
:El Payload cambia;
:La Firma permanece intacta;

|🦹‍♂️ Atacante (Hacker)|
:2. Envía petición HTTP POST;

|🚪 JwtAuthenticationFilter (Guardia)|
:3. Intercepta petición con\ndoFilterInternal();
:Extrae String jwt = authHeader.substring(7);
:Delega extracción llamando a\njwtService.extractUsername(jwt);

|⚙️ JwtService (Trituradora HMAC)|
:4. extractUsername() invoca al\nmétodo maestro extractClaim();
:5. Aplica criptografía usando la llave:\nJwts.parserBuilder()\n.setSigningKey(getSignInKey())\n.build()\n.parseClaimsJws(token);

if (¿Las firmas coinciden?) then (Verdadero, Válido)
|⚙️ JwtService (Trituradora HMAC)|
:Devuelve el email (userEmail);

    |🚪 JwtAuthenticationFilter (Guardia)|
    :Valida vigencia llamando a\njwtService.isTokenValid(jwt, usuario);
    :Crea UsernamePasswordAuthenticationToken;
    :Guarda en SecurityContextHolder;
    :Continúa con filterChain.doFilter();

    |🏰 MotorDecisionesController (Cocina/API)|
    :Acceso Permitido al Endpoint;

else (Falso, Alterado)
|⚙️ JwtService (Trituradora HMAC)|
:Lanza SignatureException o MalformedJwtException;

    |🚪 JwtAuthenticationFilter (Guardia)|
    :Atrapa excepción en el bloque catch;
    :Ejecuta manejarErrorEstructurado();

    |🦹‍♂️ Atacante (Hacker)|
    :Recibe JSON 401 Unauthorized;

endif

stop
@enduml