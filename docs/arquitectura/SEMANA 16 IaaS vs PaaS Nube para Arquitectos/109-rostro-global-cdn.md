# 2-PLANTILLA — APRENDIZAJE CLAVE DEL DÍA

**🧱 Día 109 — Aprendizaje clave: El Rostro Global (Azure Static Web Apps y CI/CD)**

**🧠 Qué entendí hoy (explicado simple)**
Entendí que mi código de Angular, a diferencia de mi backend en Java, no necesita un servidor pesado corriendo 24/7. Una vez que lo trituro (`ng build`), se convierte en archivos estáticos planos. Por lo tanto, su hogar ideal es un CDN (Red de Distribución de Contenido) que clona estos archivos por todo el mundo para que carguen en milisegundos.

**⚙️ Cómo funciona (paso a paso real)**
1. El código fuente vive en la rama `main` de GitHub.
2. Azure Static Web Apps se conecta a GitHub y le inyecta un robot (archivo `.yml` de GitHub Actions).
3. Cada vez que hago un `push`, GitHub Actions ejecuta la compilación en la nube de forma automática y envía la carpeta `dist/` a los servidores de Microsoft.

**🧱 Regla de oro del día 👉** "Un frontend moderno no gasta recursos de cómputo, solo consume ancho de banda. La lógica pertenece al servidor; la velocidad pertenece al CDN."

**❌ Qué pensaba antes**
Pensaba que desplegar un frontend requería instalar un servidor web (como Apache o Tomcat) en Linux y arrastrar carpetas manualmente. También pensaba que un error de "Policy Violation" significaba que mi código estaba roto.

**✅ Qué entiendo ahora**
Entiendo el poder de la automatización (CI/CD). Además, aprendí a leer errores de infraestructura: un "Policy Violation" es un bloqueo administrativo de cuenta (en mi caso, por la región `East US 2`), que se resuelve reubicando el recurso en una zona permitida (`Central US`).

**🔥 Diferencia clave**
* **Antes (Manual):** Compilar localmente, arrastrar archivos, arriesgarme a subir versiones equivocadas.
* **Ahora (Automatizado):** Escribo código, hago `git push`. La nube compila y publica automáticamente mediante la cinta transportadora de GitHub Actions.

**🧠 Dónde vive cada cosa (ARQUITECTURA CLOUD)**
* **Lógica (quién decide):** El Backend en Spring Boot.
* **Ejecución (quién hace):** Azure Static Web Apps (repartiendo archivos globalmente).
* **Persistencia (quién guarda):** Base de Datos PostgreSQL.
* **Transporte (quién solo pasa datos):** GitHub Actions (el robot que compila y transporta).

**🧠 Frase ancla 👉** *"El frontend es la vitrina, el backend es la bóveda. Azure automatiza la entrega de mi vitrina al mundo."*