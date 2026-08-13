**🧱 Día 106 — Aprendizaje clave: El Mapa de la Nube (IaaS vs PaaS)**

**🧠 Qué entendí hoy (explicado simple)**
Entendí que la nube tiene diferentes niveles de servicio. IaaS me alquila una computadora vacía que tengo que configurar desde cero (como alquilar un terreno baldío). PaaS me alquila un entorno ya preparado (como una cocina industrial) donde solo tengo que subir mi código y encenderlo.

**⚙️ Cómo funciona (paso a paso real)**
1. Ingreso a Microsoft Azure.
2. Creo un **Grupo de Recursos** (`rg-tesis-arquitectura`).
3. Este grupo funciona como un perímetro lógico de seguridad, una gran "caja contenedora".
4. Dentro de esta caja vivirán mis futuros servicios administrados (App Service, Base de Datos), sin tocar jamás una terminal de instalación de sistema operativo.

**🧱 Regla de oro del día 👉** "Mi código aporta el valor; la plataforma administra el entorno."

**❌ Qué pensaba antes**
Pensaba que subir mi aplicación a internet significaba alquilar un servidor remoto en Linux, conectarme por consola y pasar horas instalando la base de datos, Java y Node.js a mano.

**✅ Qué entiendo ahora**
Entiendo que como Arquitecto de Software, delego la infraestructura base a Azure usando PaaS. Esto me permite dedicar toda mi energía mental a las reglas de evaluación de mi tesis. La responsabilidad ante un fallo de infraestructura es de Microsoft; la responsabilidad de la lógica de evaluación es mía.

**🔥 Diferencia clave**
* **IaaS (Antes):** Control total, pero mantenimiento doloroso. Eres el dueño del edificio y también el conserje.
* **PaaS (Ahora):** Enfoque 100% en el producto. Entregas el código empaquetado (Artefacto) y la nube se encarga de ejecutarlo.

**🧠 Dónde vive cada cosa (ARQUITECTURA CLOUD)**
* **Lógica (quién decide):** Mi código (Spring Boot / Angular).
* **Ejecución (quién hace):** El entorno administrado de Azure.
* **Persistencia (quién guarda):** Discos físicos administrados por Azure; yo diseño el esquema.
* **Transporte (quién solo pasa datos):** La red global de Microsoft.

**🧠 Frase ancla 👉** *"La nube no es magia, son computadoras de otra persona. Elijo PaaS para diseñar soluciones, no para instalar sistemas operativos."*


https://portal.azure.com/?Microsoft_Azure_Education_correlationId=d8f07e43-daa2-42fc-ba60-a87218669768&Microsoft_Azure_Education_newA4E=true&Microsoft_Azure_Education_asoSubGuid=e8bb38ea-d9c7-4fa8-99e3-edb9cc56ce75#view/Microsoft_Azure_Education/EducationMenuBlade/~/overview

https://portal.azure.com/?Microsoft_Azure_Education_correlationId=d8f07e43-daa2-42fc-ba60-a87218669768&Microsoft_Azure_Education_newA4E=true&Microsoft_Azure_Education_asoSubGuid=e8bb38ea-d9c7-4fa8-99e3-edb9cc56ce75#servicemenu/Microsoft_Azure_Resources/ResourceManager/resourcegroups