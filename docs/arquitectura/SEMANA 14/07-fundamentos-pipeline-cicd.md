🧱 Día 1 — Aprendizaje clave: El Fundamento de CI/CD (La Fábrica Automática)

🧠 Qué entendí hoy (explicado simple)
Entendí que compilar y desplegar mi código a mano es un proceso lento y propenso a errores humanos. Necesito una "cinta transportadora" automática. Yo solo escribo el código al inicio de la cinta; la máquina se encarga de compilarlo, pasarle las pruebas y empaquetarlo si todo está correcto.

⚙️ Cómo funciona (paso a paso real)

El Inicio: Escribo código en mi PC (IntelliJ).

El Transporte: Hago Push a GitHub.

El Vigilante: Jenkins detecta el cambio automáticamente en el repositorio.

El Ensamblador: Maven compila el código.

El Inspector: JUnit ejecuta las pruebas en la memoria.

El Producto: Se genera el archivo ejecutable (.jar) listo para la nube.

🧱 Regla de oro del día 👉
"Los ingenieros no hacemos tareas repetitivas a mano; construimos máquinas que las hagan por nosotros."

❌ Qué pensaba antes
Pensaba que el trabajo de un desarrollador terminaba cuando el código corría bien en localhost y que llevarlo a producción era un proceso manual de arrastrar, copiar y pegar archivos en un servidor.

✅ Qué entiendo ahora
Entiendo que el código fuente es solo la materia prima. El verdadero valor se consolida cuando existe un pipeline automatizado que valida, compila y prueba ese código sin intervención humana, asegurando que cada entrega sea predecible y segura.

🔥 Diferencia clave
Antes: Compilación local, pruebas manuales y despliegues lentos que dependen de mi memoria para no olvidar pasos.
Ahora: Una fábrica de software donde cada push dispara una cadena de ensamblaje estricta, rápida y sin margen para el error humano.

🎯 Ejemplo que puedo explicar
CI/CD es como el lavavajillas de un restaurante. Los cocineros (nosotros) no lavamos los platos a mano (desplegar a producción manual). Ponemos los platos en la máquina (Jenkins), y ella automatiza el lavado y secado rápido y sin errores humanos.

🧠 Dónde vive cada cosa (ARQUITECTURA)

Lógica (quién decide): Mi código fuente escrito en la PC y versionado en GitHub.

Ejecución (quién hace): Jenkins, actuando como el "Mayordomo" que orquesta a los obreros (Maven y JUnit).

Persistencia (quién guarda): El repositorio final que guarda el Artefacto (JAR) empaquetado.

Transporte (quién solo pasa datos): La red y los webhooks que le avisan a Jenkins que hubo un cambio en GitHub.

🧠 Frase ancla 👉
"El código es la receta, CI/CD es la cocina automatizada."

🚀 Cómo sé que lo entendí
Lo sé porque puedo dibujar la arquitectura en un papel en blanco sin mirar ningún tutorial, marcando exactamente el viaje del código desde mi teclado hasta convertirse en un artefacto empaquetado, y puedo explicárselo a cualquier persona usando la metáfora del restaurante.