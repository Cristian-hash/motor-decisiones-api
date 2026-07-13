@startuml
skinparam BackgroundColor #FFFFFF
skinparam ArrowColor #333333
skinparam NoteBackgroundColor #FFFFFF
skinparam NoteBorderColor #333333
skinparam ParticipantBackgroundColor #FFFFFF
skinparam ParticipantBorderColor #333333

participant "🧠 EvaluacionService\n(Motor / Productor)" as E
participant "🌪️ Apache Kafka\n(Canal / Topic)" as K
participant "🏆 LogrosService\n(Medallas / Consumidor)" as L
participant "📧 NotificacionService\n(Correos / Consumidor)" as N

note over E: El estudiante responde la lección correctamente
E -> E: Guarda progreso y puntos en BD

note over E, K: El Motor avisa sobre el logro y lo lanza al canal
E ->> K: 📢 Publica: LeccionCompletadaEvent (JSON)

note right of E
¡El EvaluacionService SIGUE ADELANTE SIN ESPERAR!
Termina su trabajo y devuelve
el DTO al Frontend inmediatamente.
end note

note over K, N: Los otros sistemas detectan el evento a su propio ritmo

par Consumidor 1
K ->> L: 🎧 Lee el Evento
L -> L: Otorga Medalla de Oro al Estudiante
else Consumidor 2
K ->> N: 🎧 Lee el Evento
N -> N: Envía Email: "¡Felicidades por tu logro!"
end

note over L, N
Si el envío de correos sufre alguna falla,
el Motor de Evaluación jamás se entera y sigue funcionando perfectamente.
end note
@enduml