**Por qué fallaba el sistema ante
1- Solo habia un usuario. Se ha creado varios, y que puedan entrar con diferentes(ademas de arquitecto, se crearon los usuarios alumno y prueba)
2- Cuando ponia otra URL me decia error 500.
3- Se verifico el URL. Y no se encontraba bien redireccionado, ya que si ponia en el URL leccion 4,si podia llegar a ingresar.
4- Luego puse para que se redireccionara a donde habia quedado su progreso si es qucerraba sesion y volvia a ingresar.

Aun me falta corregir cuando se equivocan y les sale, a los usuarios ,respusta ya respondida.  siento que no se deja entender y habria otra respuesta mas entendible.

SOLUCION : SERIA QUE CUANDO SE EQUIVOQUEN ,DIGA ERROR ,MANDE A LA SIGUIENTE LECCION , Y VUELVA A REPETIR LA LECCION EQUIVOCADA COMO LO HACE DUOLINGO. ,OJO REGISTRANDO , LOS ERRORES, PARA TENER EL REGISTRO DONDE SE EQUVIOCAN MAS , QUE MARCAN Y ESO.

A. Arquitectura de Resiliencia (El Cliente Ciego)

"Para evitar la interrupción del aprendizaje, se implementó una arquitectura de 'Cliente Ciego'. 
En este modelo, el Motor de Decisiones (backend) actúa como la única fuente de verdad inmutable. Ante una acción redundante del estudiante, 
el servidor intercepta la anomalía y emite un contrato estructurado (JSON). El cliente, ignorante de la regla de negocio subyacente, se limita a renderizar el mensaje,
logrando una separación total de responsabilidades y un diseño agnóstico a la tecnología de la vista."

B. Telemetría y Monitoreo en Azure**

"La eficacia de la arquitectura se validó mediante telemetría en tiempo real utilizando Azure Log Stream 
y auditorías en PostgreSQL. Se monitorizó el flujo de peticiones, confirmando que las infracciones de estado (HTTP 409 
Conflict) fueron correctamente aisladas por el servidor antes de propagarse como fallos críticos (HTTP 500), 
asegurando la estabilidad del entorno."
