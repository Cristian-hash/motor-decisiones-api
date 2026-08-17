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



Correcion de F1 a B1 

Aprendizaje clave: La Realidad de la Infraestructura (Cuotas, Firewalls y Despliegues)

🧠 Qué entendí hoy (explicado simple)
Entendí que para que mi aplicación funcione en internet, el código perfecto no es suficiente. El servidor en la nube es como un local alquilado: si tengo el plan gratuito, solo me dan "luz" por 60 minutos al día, y si se acaba, me apagan el sistema. Además, mi base de datos es una bóveda privada; si no pongo explícitamente a mi servidor en la "Lista VIP" (Firewall), la base de datos lo ignorará por completo, aunque tengan la misma contraseña.

⚙️ Cómo funciona (paso a paso real)

Despliegue Continuo: Conecto mi repositorio a Azure Static Web Apps. Azure inyecta un robot (GitHub Actions) que compila y publica mi Frontend automáticamente sin que yo mueva un dedo.

El Bloqueo por Cuota (403/503): Si mi Backend en Java agota sus 60 minutos diarios de CPU intentando arrancar, Azure hace un apagado de emergencia y le pone un candado físico (Quota Exceeded).

El Escalado Vertical: Para quitar el límite, subo la potencia del servidor al plan Basic (B1). Ahora el motor tiene gasolina ilimitada y vuelve a arrancar.

El Guardia del Firewall: Cuando Java intenta conectarse a PostgreSQL, el guardia lo bloquea por defecto (Connect timed out). Entro a las reglas de red de la base de datos y marco "Allow Azure services" para dejarlo pasar.

🧱 Regla de oro del día 👉 "El código perfecto es inútil si la infraestructura no tiene energía (Cuota) ni permisos (Firewall)."

❌ Qué pensaba antes
Pensaba que un Error 403 (Forbidden) o un Error 503 (Service Unavailable) significaba que mi código de Java o Angular estaba roto y debía buscar fallas en mis clases.

✅ Qué entiendo ahora
Entiendo que en la nube, los errores 400 y 500 muchas veces son bloqueos administrativos. Las dudas son oportunidades de aprendizaje: ahora leo la "caja negra" (Log stream) para descubrir si el problema es falta de cuota, un Firewall cerrado, o un simple caché de mi navegador.

🔥 Diferencia clave

Antes: Adivinaba los errores, me frustraba con pantallas azules de Azure y pensaba que la nube era impredecible.

Ahora: Primero entiendo el problema; después escribo el código. Observo los logs, diferencio un problema de Código de un problema de Infraestructura, y escalo mi servidor de forma estratégica para administrar mis costos.

🎯 Ejemplo que puedo explicar
Imagina alquilar un local comercial (Servidor) con una caja fuerte adentro (Base de Datos). El plan gratis te da luz solo por 1 hora al día. Si intentas trabajar más, te cortan la luz de golpe (Quota exceeded). Y si intentas abrir la caja fuerte, no basta con tener la clave; el guardia necesita que tu nombre esté en la lista de empleados autorizados (Firewall). Yo soy el administrador que paga por más luz y actualiza la lista del guardia.

🧠 Dónde vive cada cosa (ARQUITECTURA CLOUD)

Lógica (quién decide): El Backend en Spring Boot, corriendo en un Azure App Service (Plan B1).

Ejecución (quién hace): El Frontend en Angular, servido por la red global de Azure Static Web Apps.

Persistencia (quién guarda): Azure Database for PostgreSQL, protegida tras un muro de Firewall estricto.

Transporte (quién solo pasa datos): El Pipeline de GitHub Actions (la cinta transportadora) que lleva el código de mi PC al mundo. El código asume, pero el Pipeline garantiza.

🧠 Frase ancla 👉 "La pantalla presenta información, el código aplica las reglas, pero la infraestructura da la energía."

🚀 Cómo sé que lo entendí
Lo sé porque pude leer un log crudo que decía Connect timed out, deducir lógicamente que Java estaba encendido pero el Firewall lo ignoraba, corregir la regla en la bóveda, y escalar verticalmente (hacer Scale Up) mi plan de hosting para vencer el temido bloqueo de Quota Exceeded sin entrar en pánico.