🛠️ Guía de Consultas SQL para Pruebas y Auditoría (E2E)
Este documento contiene los scripts esenciales de PostgreSQL para manipular el estado del Motor de Decisiones. Utiliza estas consultas durante la fase de desarrollo para resetear escenarios, evadir reglas antifraude y sembrar nuevos datos de prueba sin necesidad de interfaces gráficas.

🧹 1. Limpieza y Reseteo de Estado (Reset State)
Útiles para poder repetir pruebas de evaluación sin que el sistema bloquee al usuario por la regla de "Lección ya completada".

A. Borrar el historial de progreso de un usuario
Elimina los intentos de un usuario para que pueda volver a enviar una respuesta y ganar puntos nuevamente.

SQL
-- Reemplaza el '1' por el ID de tu usuario de pruebas
DELETE FROM progresos WHERE usuario_id = 1;
B. Resetear los puntos de experiencia (XP)
Útil si quieres ver cómo el usuario sube de nivel desde cero o si la columna quedó en NULL por un error previo.

SQL
-- Poner la experiencia a cero para un usuario específico
UPDATE usuarios SET puntos_experiencia = 0 WHERE id = 1;

-- (Opcional) Curar a TODOS los usuarios que tengan NULL
UPDATE usuarios SET puntos_experiencia = 0 WHERE puntos_experiencia IS NULL;
C. Restaurar la contraseña a "123456" (Formato BCrypt)
Si olvidaste la contraseña o la base de datos la tiene en texto plano (lo cual causa un error 403 Forbidden en Spring Security), este script inyecta el hash correcto para "123456".

SQL
UPDATE usuarios
SET password = '$2a$10$XURPShQNCsLjp1ESc2laoObo9QZDhxz73hJPaEv7/cBha4pk0AgP.'
WHERE email = 'arquitecto@tesis.com';
🌱 2. Siembra de Datos (Data Seeding)
Útiles para inyectar nuevas lecciones y retos arquitectónicos para el motor de gamificación.

PASO 1: Crear una Nueva Lección
Asegúrate de que el patron_id corresponda a un patrón que ya exista en tu tabla patrones (ej. 1).

SQL
INSERT INTO lecciones (titulo, problema_hook, metafora, pseudocodigo, codigo_java, puntos_recompensa, tipo_evaluacion, patron_id)
VALUES (
'El poder del Factory Method',
'Problema: Tienes la palabra "new" esparcida por todo tu código. Si la clase cambia, tienes que modificar 50 archivos.',
'Metáfora: Una fábrica de juguetes. Tú pides un "Oso", la fábrica sabe cómo armarlo y te lo entrega listo.',
'1. Interfaz Producto 2. Clase Creadora 3. El cliente llama a crearProducto()',
'public interface Creador { Producto crear(); }',
20, -- Esta lección da 20 XP
'OPCION_UNICA',
1
);
PASO 2: Averiguar el ID de la nueva Lección
Los IDs saltan (Gaps) debido al rendimiento de PostgreSQL. Nunca adivines el ID. Averígualo con esta consulta:

SQL
SELECT id, titulo FROM lecciones ORDER BY id DESC;
(Anota el número de id que le tocó a la lección "El poder del Factory Method". Supongamos que fue el 5).

PASO 3: Insertar las Opciones de Respuesta
Remplaza el número 5 al final del VALUES por el ID real que anotaste en el Paso 2.

SQL
-- Opción CORRECTA
INSERT INTO opciones_respuesta (texto_opcion, es_correcta, justificacion_feedback, leccion_id)
VALUES ('Centralizar la creación de objetos en un método especializado', true, '¡Excelente! Esto aísla el código de la creación y respeta el principio Open/Closed.', 5);

-- Opción INCORRECTA
INSERT INTO opciones_respuesta (texto_opcion, es_correcta, justificacion_feedback, leccion_id)
VALUES ('Crear una clase global estática con todos los métodos', false, 'Cuidado, eso se parece más a un Singleton mal implementado o a un God Object.', 5);
🔎 3. Consultas de Auditoría Forense
Útiles para confirmar que tu código Java (Spring Boot) realmente hizo lo que prometió hacer.

A. Ver el estado actual del Perfil del Jugador
Verifica si el Motor de Strategy sumó los puntos correctamente.

SQL
SELECT id, email, puntos_experiencia
FROM usuarios
WHERE id = 1;
B. Ver el último intento del estudiante
Verifica si el Factory de evaluación y el EvaluacionService registraron el paso del alumno.

SQL
SELECT p.id, p.fecha_intento, p.completado, p.puntaje_obtenido, l.titulo
FROM progresos p
JOIN lecciones l ON p.leccion_id = l.id
WHERE p.usuario_id = 1
ORDER BY p.fecha_intento DESC
LIMIT 1;
C. Buscar las respuestas correctas para hacer trampa en Postman
Útil cuando estás haciendo pruebas de integración rápida (E2E) y necesitas armar el JSON correcto al instante.

SQL
SELECT l.id AS leccion_id, l.titulo, o.id AS opcion_correcta_id, o.texto_opcion
FROM lecciones l
JOIN opciones_respuesta o ON l.id = o.leccion_id
WHERE o.es_correcta = true;
------------------------------------------------------------------------------
PSEUDOCODIGO 

1. El Diagnóstico (Análisis de tu código)
   Lo que escribiste fue:

Java
public interface EvaluacionStrategyFactory{
public UserRepository<>  x(String tipodeEvaluacion){
}

public class OpcionUnica implements EvaluacionStrategyFactory{
}
Tu idea (La metáfora del avión): "Si yo le digo 'aire' que sería el parámetro de entrada, él me da las llaves del avión que sería el objeto que él me retorna".
¡Tu idea está perfecta! Entendiste el concepto de la "Fábrica" (Factory) a la perfección. El problema que tuviste fue simplemente organizar qué nombre le pertenece a qué pieza.

En tu código, mezclaste el nombre del Molde de las herramientas (EstrategiaEvaluacion) con el nombre de la Fábrica (EvaluacionStrategyFactory).

2. La Corrección Arquitectónica (Las 3 Piezas)
   Vamos a poner los nombres correctos usando tu misma metáfora del avión.

PIEZA 1: El Molde de la Herramienta (La Interfaz Strategy)
Primero, necesitamos definir qué es un "vehículo". Todas las herramientas de evaluación deben firmar este contrato.

Java
// Este es el MOLDE. Todo el que evalúe debe tener este método.
public interface EstrategiaEvaluacion {
FeedbackDTO evaluar(RespuestaEstudianteDTO dto, Leccion leccion);
}
PIEZA 2: Las Herramientas Concretas (Las Clases Strategy)
Ahora construimos los vehículos reales (avión, barco). Estas son tus clases de evaluación específicas. Ellas implementan la interfaz anterior.

Java
// Herramienta A: El "Barco"
public class OpcionUnicaStrategy implements EstrategiaEvaluacion {
@Override
public FeedbackDTO evaluar(RespuestaEstudianteDTO dto, Leccion leccion) {
// Lógica para corregir opción única
return new FeedbackDTO(...);
}
}

// Herramienta B: El "Avión"
public class AnalisisCodigoStrategy implements EstrategiaEvaluacion {
@Override
public FeedbackDTO evaluar(RespuestaEstudianteDTO dto, Leccion leccion) {
// Lógica para corregir código
return new FeedbackDTO(...);
}
}
PIEZA 3: La Fábrica (El Factory)
Aquí es donde entra tu genial metáfora de las llaves. La Fábrica NO implementa las estrategias; ella conoce todas las estrategias y te devuelve la correcta.

Java
// Esta es la FÁBRICA. Tú le das una palabra, ella te da la herramienta.
public class EvaluacionStrategyFactory {

    // El "Mostrador" donde la fábrica tiene guardados todos los vehículos
    private Map<String, EstrategiaEvaluacion> estrategias;

    // Tu método de la metáfora: le das "aire" (String), te devuelve el Avión (EstrategiaEvaluacion)
    public EstrategiaEvaluacion obtenerEstrategia(String tipoEvaluacion) {
        return estrategias.get(tipoEvaluacion);
    }
}

3. La Lección de Arquitectura (La "Aja!" Moment)
   ¿Ves la diferencia clave?

No implementaste EvaluacionStrategyFactory en OpcionUnica. Las opciones únicas implementan EstrategiaEvaluacion (el molde).

La Fábrica (Factory) es una clase separada. Su único trabajo en el mundo es tener un mapa (Map) y ejecutar tu metáfora: "Toma esta palabra, dame el objeto que sirve para eso".

}