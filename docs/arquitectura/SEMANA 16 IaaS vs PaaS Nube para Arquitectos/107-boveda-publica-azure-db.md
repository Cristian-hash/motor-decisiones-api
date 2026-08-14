¡A todo gas, arquitecto José! 🚀

Esta es la mentalidad de un verdadero Ingeniero de Software de élite. No confías en tu memoria a corto plazo; confías en tus sistemas y en tu documentación. Cuando el "José del futuro" necesite levantar una infraestructura en la nube dentro de 6 meses (o en su primer trabajo formal como Cloud Architect), no tendrá que adivinar ni cometer los mismos errores. Tendrá este manual exacto.

He analizado absolutamente todos los baches y victorias de tu **Día 107**. Aquí tienes la **Guía Maestra de Replicación** (el paso a paso definitivo con las correcciones ya aplicadas) y tu **Plantilla de Aprendizaje** lista para Notion.

---

### 🗺️ MANUAL DEL ARQUITECTO: Despliegue de Base de Datos en Azure (A prueba de balas)

*Guía de replicación para el José del futuro. Tiempo estimado de ejecución: 15 minutos.*

**1. El Perímetro y el Servicio**

* **Acción:** Ingresa a `portal.azure.com`. Busca "Resource groups" y asegúrate de tener tu carpeta creada (ej. `rg-tesis-arquitectura`).
* **Acción:** Haz clic en **+ Create**, busca `Azure Database for PostgreSQL` y selecciona **Flexible Server** (Servidor flexible).

**2. Configuración Básica (El Bypass de Regiones)**

* **Problema a evitar:** La región `East US` suele bloquear cuentas de estudiantes por alta demanda.
* **Acción:** En la pestaña *Basics*:
* **Server name:** Escribe tu nombre único (ej. `db-tesis-arquitectura-jose`).
* **Region:** Selecciona **Central US** (o `East US 2`).
* **PostgreSQL version:** 16 (o la que uses localmente).



**3. Protección de Billetera (El Bypass de Costos)**

* **Problema a evitar:** La configuración por defecto selecciona el entorno "Production" y máquinas `B2s` que consumen ~$60 a ~$620 dólares mensuales.
* **Acción:**
* Cambia **Workload type** a **Dev/Test**.
* Haz clic en el texto azul **Configure server**.
* En *Compute size*, selecciona **`Standard_B1ms`** (1 vCore, 2 GiB RAM). *Nota: Esta es la máquina mágica que te da 750 horas gratis al mes.*
* Asegúrate de que *High Availability* esté en **Disabled**.
* Guarda los cambios. El costo estimado debe bajar a un rango cubierto por la promoción (las letras verdes indicarán *Free upto 750 hours*).



**4. Forjando Llaves (Autenticación)**

* **Problema a evitar:** La opción híbrida con Microsoft Entra pide configuraciones empresariales complejas.
* **Acción:** Selecciona **PostgreSQL authentication only**. Define tu usuario (`postgres`) y tu contraseña maestra.

**5. El Muro Perimetral (El Bypass de IPs Dinámicas / CGNAT)**

* **Problema a evitar:** Los proveedores de internet rotan tu IP pública (ej. pasas de `.26` a `.15`). Si autorizas una sola IP exacta, Azure te bloqueará el acceso al día siguiente.
* **Acción:** En la pestaña *Networking*:
* Selecciona **Public access (allowed IP addresses)**.
* Haz clic en **+ Add current client IP address**.
* **¡TRUCO MAESTRO!** Modifica el rango que se autocompletó para abrir "toda la cuadra" de tu proveedor. En *Start IP* pon el final en `.0` (ej. `38.137.220.0`) y en *End IP* pon el final en `.255` (ej. `38.137.220.255`).



**6. La Conexión (El Bypass del Nombre Corto)**

* **Problema a evitar:** pgAdmin lanza el error `[Errno 11001] getaddrinfo failed` si usas el nombre corto del servidor.
* **Acción:** Tras crear el servidor, ve a *Overview* y copia el **Endpoint** completo (la URL larga que termina en `.postgres.database.azure.com`). Pega eso en el *Host name* de pgAdmin, usa el puerto `5432` y tus credenciales. ¡Conexión exitosa al 100%!

---

### 2-PLANTILLA — APRENDIZAJE CLAVE DEL DÍA

**🧱 Día 107 — Aprendizaje clave: La Bóveda Pública (PostgreSQL en Azure), Costos y Redes Dinámicas**

**🧠 Qué entendí hoy (explicado simple)**
Entendí que migrar a la nube no es darle "Siguiente, Siguiente, Crear". Microsoft Azure es un negocio y su configuración por defecto me asignará la computadora más cara posible. Como arquitecto, aprendí a modificar el hardware al tamaño exacto de la capa gratuita (`B1ms`). También entendí que mi internet local cambia de dirección constantemente (IP Dinámica), por lo que tuve que enseñarle al guardia de seguridad de Azure (Firewall) a dejar pasar a todo un "rango" de direcciones en lugar de solo a una.

**⚙️ Cómo funciona (paso a paso real)**

1. **Aprovisiono:** Creo el Servidor Flexible en una región con espacio disponible (Central US).
2. **Protejo Costos:** Cambio a "Dev/Test" y achico el servidor a `Standard_B1ms` para garantizar una factura de $0.00.
3. **Muro Flexible (Firewall):** Agrego mi IP pública, pero modifico el último número (de `.0` a `.255`) para que los cambios diarios de mi proveedor de internet no me dejen afuera.
4. **Conexión Exacta:** Tomo la dirección completa (Endpoint público) proporcionada por Azure, voy a pgAdmin y me conecto usando las llaves maestras que forjé.

**🧱 Regla de oro del día 👉** "En la nube, la configuración por defecto te vacía la billetera; el arquitecto lee, ajusta y protege sus recursos."

**❌ Qué pensaba antes**
Pensaba que subir una base de datos era carísimo, que cualquier región daba igual, y que para conectarme a mi servidor en la nube bastaba con usar su "nombre corto".

**✅ Qué entiendo ahora**
Entiendo que puedo usar una infraestructura global 100% gratis si conozco los límites (`B1ms` y `32GB`). Comprendo la existencia de redes CGNAT (múltiples IPs públicas de mi proveedor) y que las herramientas como pgAdmin necesitan la ruta absoluta (FQDN completo) para resolver el DNS y no perderse en internet.

**🔥 Diferencia clave**

* **Antes (Local):** Le daba clic a "Iniciar PostgreSQL", me conectaba a `localhost` y todo funcionaba, pero si mi PC se rompía, mi tesis moría.
* **Ahora (Cloud):** Mi base de datos tiene alta disponibilidad en servidores de EE. UU. Es accesible globalmente, pero me exige rigor técnico absoluto en el manejo de Redes (Firewall), DNS (Endpoints) y Finanzas (Costos).

**🎯 Ejemplo que puedo explicar**
Configurar esto es como alquilar una bóveda de banco en otra ciudad.

* **Región:** Elegir en qué ciudad está el banco (Central US).
* **B1ms:** Elegir la caja fuerte pequeña que es gratis, en lugar de la habitación gigante que cuesta $600/mes.
* **Endpoint:** Usar la dirección GPS completa del banco, no solo decir "llevame al banco de José".
* **Firewall:** Decirle al guardia armado de la puerta que mi familia usa autos con matrículas que empiezan con "38.137.220", y que los deje pasar a todos, sin importar el último número.

**🧠 Dónde vive cada cosa (ARQUITECTURA CLOUD)**

* **Lógica (quién decide):** El Firewall de Azure (evalúa el rango de mi IP entrante y abre o bloquea el puerto 5432).
* **Ejecución (quién hace):** El clúster de hardware dedicado de Microsoft Azure procesando mis consultas.
* **Persistencia (quién guarda):** Los discos SSD Premium redundantes de la nube.
* **Transporte (quién solo pasa datos):** La red de internet global conectando mi pgAdmin en Perú con el Endpoint en Central US.

**🧠 Frase ancla 👉** *"El error es información. Medimos dos veces, cortamos una y la factura sale en cero."*

**🚀 Cómo sé que lo entendí**
Lo sé porque dentro de 6 meses o en mi primer trabajo formal, si intento desplegar una BD y veo un costo estimado de $600 USD, no voy a entrar en pánico. Sabré exactamente cómo abrir el panel de "Compute size", bajarlo a un entorno de desarrollo e implementar una regla de firewall por subred para no quedarme bloqueado al día siguiente.