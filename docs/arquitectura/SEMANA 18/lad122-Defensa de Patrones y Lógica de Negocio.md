DÍA 122 (Miércoles): Defensa de Patrones y Lógica de Negocio
Prepara la defensa de tu código. ¿Por qué descartaste usar un simple switch o if/else gigantesco para evaluar las respuestas? Pensar como un ingeniero requiere alejarse del código y observar los problemas desde un punto de abstracción alto. Identifica por qué un "switch" limita tu crecimiento y por qué separar los manuales protege tu sistema.
FLUJO
Orquestador: Únicamente recibe la solicitud y delega.

Lógica (Dueño real): El EvaluacionService toma el control. Delega la creación al Factory y ejecuta el comportamiento del Strategy. Todo permanece estable frente a los cambios.

Datos: Transportan el resultado hacia el almacenamiento.
Fórmula la respuesta perfecta: "Diseñé este sistema respetando el Principio Abierto/Cerrado. Delegué la creación de instancias al almacén (Factory) y el comportamiento al manual de reglas (Strategy)."