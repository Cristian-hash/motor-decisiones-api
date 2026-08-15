🧱 Día 108 — Aprendizaje clave: Empaquetado, Codificación UTF-8 y Cuotas de CPU en la Nube.

🧠 Qué entendí hoy (explicado simple)
Entendí que subir un backend a la nube es una operación de tres bandas. Primero, debo limpiar mi código de configuración de caracteres inválidos (tildes) para que el servidor Linux lo pueda leer. Segundo, debo extraer todas las contraseñas del código y dejarlas como "cables pelados" (Variables de Entorno). Tercero, arrastro el código compilado al disco duro de Azure (Kudu) y dejo que la nube conecte esos cables a la memoria RAM.

⚙️ Cómo funciona (paso a paso real)

Limpio el application-prod.properties para que sea puramente ASCII.

Compilo con ./mvnw clean package usando el motor interno del IDE.

Configuro las Variables de Entorno en el portal de Azure.

Arrastro el .jar al Zip Push Deploy de Kudu. Azure inyecta la memoria, enciende la Máquina Virtual de Java y expone mi URL.

🧱 Regla de oro del día 👉 "La infraestructura expone al código. Lo que funciona localmente en Windows puede fallar en Linux por una tilde, y lo que es infinito en localhost tiene cuotas estrictas en la nube gratuita."

❌ Qué pensaba antes
Pensaba que si mi código compilaba en IntelliJ, funcionaría exactamente igual en cualquier lugar. También pensaba que un "Error 403" en producción significaba que había programado mal la seguridad de mi aplicación.

✅ Qué entiendo ahora
Entiendo la diferencia entre un error de código y un error de infraestructura. Un MalformedInputException en Maven es un choque de idiomas entre Windows y Linux. Un Error 403 Stopped en Azure con plan F1 no es un fallo de mi código, es el límite de los 60 minutos de CPU diaria protegiendo mi billetera. El código asume, la infraestructura limita y provee.