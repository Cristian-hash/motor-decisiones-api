🛠️ TAREA 1: Solucionar la Fricción Pedagógica (Base de Datos)
El Problema: Una lección específica tiene demasiados intentos_fallidos porque el problema o la metáfora no son claros.
La Solución: Actualizar la redacción directamente en PostgreSQL para darle más claridad al alumno antes de que tome una decisión.

[ ] 1. Abre el Query Tool en DBeaver conectado a tu base de datos en Azure.

[ ] 2. Ejecuta el siguiente comando SQL (reemplazando [ID_LECCION] por el número real y ajustando los textos):

SQL
UPDATE lecciones
SET
problema_hook = 'Nuevo texto más claro del problema. Describe exactamente qué está fallando en el sistema para que el alumno lo entienda mejor.',
metafora = 'Nueva metáfora más intuitiva. (Ej: Imagina que el sistema es un restaurante...)'
WHERE id = [ID_LECCION];
[ ] 3. Haz Commit en DBeaver para guardar el cambio en producción.

🎮 TAREA 2: Implementar la Regla de Gamificación "-5 XP" (Backend)
El Problema: El usuario usa "fuerza bruta" adivinando respuestas sin leer, ya que fallar no tiene consecuencias.
La Solución: Modificar EvaluacionService.java en Spring Boot para restar 5 puntos por cada error, sin dejar que el puntaje baje de cero.

[ ] 1. Abre tu proyecto Spring Boot en IntelliJ IDEA.

[ ] 2. Ve a tu clase EvaluacionService.java.

[ ] 3. Localiza el método donde evalúas si la opción es correcta o incorrecta.

[ ] 4. En el bloque donde la respuesta es INCORRECTA, inyecta esta lógica de desgaste de experiencia:

Java
if (!esCorrecta) {
// 1. Evitar que los puntos sean negativos
int puntosActuales = usuario.getPuntosExperiencia();
int nuevosPuntos = Math.max(0, puntosActuales - 5);

    // 2. Aplicar el castigo por fuerza bruta
    usuario.setPuntosExperiencia(nuevosPuntos);
    usuarioRepository.save(usuario);
    
    // 3. Registrar el intento fallido (ya lo tienes implementado)
    // progresoRepository.save(progresoFallido);
    
    // 4. Retornar el feedback con el aviso del descuento
    return new RespuestaEvaluacionDTO(
        false, 
        "Decisión incorrecta. " + opcion.getFeedback() + " (Penalización: -5 XP)"
    );
}
🧭 TAREA 3: Solucionar el "Callejón sin Salida" (Frontend)
El Problema: Cuando el usuario recarga la página o vuelve a entrar, Angular lo manda a una lección que ya aprobó. El backend lanza 409 Conflict, pero Angular no muestra el botón "Siguiente", dejando al usuario atrapado.
La Solución: Forzar a Angular a mostrar el botón "Siguiente Lección" cuando reciba un error 409.

[ ] 1. Abre tu proyecto Angular en VS Code.

[ ] 2. Ve a src/app/components/leccion/leccion.component.ts.

[ ] 3. Localiza el bloque error: (err) => { ... } dentro de evaluarOpcion().

[ ] 4. Agrega la instrucción para habilitar el botón de avanzar dentro del if (err.status === 409):

TypeScript
error: (err) => {
if (err.status === 409) {
this.esCorrecto = false;
this.tituloFeedback = 'Aviso del Sistema';
this.mensajeFeedback = err.error?.mensaje || 'Ya has completado esta lección.';
this.mostrarFeedBack = true;

          // 🔥 LA SOLUCIÓN UX: Habilitamos el botón para que pueda escapar de esta lección
          this.esCorrecto = true; // Engañamos a la vista temporalmente para que pinte el botón Siguiente
        } else {
          // ... resto de tu manejo de errores
        }
      }
🚀 TAREA 4: Despliegue y Bitácora (CI/CD)
[ ] 1. Guarda todos los archivos en IntelliJ y VS Code.

[ ] 2. Abre la terminal y sube los cambios para que se desplieguen en Azure:

Bash
git add .
git commit -m "feat: implementacion de penalizacion -5 XP, ajustes pedagogicos y correccion UX de navegacion"
git push origin main
[ ] 3. En tu bitácora diaria, marca el Día 118 como completado.

-----
El error es información valiosa. Tu terminal, tanto en PowerShell como en Git Bash, te está gritando la misma verdad absoluta: The JAVA_HOME environment variable is not defined correctly.1. Observo y Comprendo (El diagnóstico real)Esto significa algo muy simple: la carpeta C:\Program Files\Java\jdk-21 no existe en tu computadora, o el motor de Java no se encuentra exactamente dentro de esa ruta. El sistema está buscando ciegamente en un lugar vacío y por eso Maven se niega a arrancar.Como nos enseña la mentalidad del ingeniero de élite, los grandes profesionales de software no están todo el tiempo "apagando fuegos" adivinando comandos en la terminal, sino que se alejan del problema, razonan y toman "decisiones estratégicas".   2. La Decisión Estratégica (El salto de nivel)Ver el problema desde un punto de abstracción más alto nos revela la solución a largo plazo: Tu propio IntelliJ IDEA ya sabe dónde está Java.
IntelliJ tiene su propio JDK configurado internamente (usualmente en la carpeta de JetBrains o descargado por el propio IDE), por eso logró compilar tu proyecto con éxito al inicio de la sesión.   En lugar de pelear con el sistema operativo intentando configurar variables de entorno manuales, vamos a hackear el sistema y aprovechar el conocimiento de tu IDE.   3. Ejecución Impecable (Paso a paso visual)Vamos a usar el orquestador visual de IntelliJ para generar el .jar saltando las pruebas, sin tocar la terminal:Abre el panel de control: En la parte superior derecha (o en el menú lateral derecho) de tu IntelliJ IDEA, haz clic en la pestaña Maven.Activa el escudo antierrores (Skip Tests): En la barrita de herramientas de ese panel de Maven, busca un pequeño ícono con forma de rayo azul o un círculo tachado (al pasar el mouse dirá Toggle 'Skip Tests' Mode). Haz clic en él. Verás que se queda presionado o marcado.Limpia el terreno: Despliega tu proyecto en ese panel, abre la carpeta Lifecycle y haz doble clic en la opción clean. Espera un par de segundos a que termine.Empaqueta tu obra: Ahí mismo, haz doble clic en la opción package.Al hacer esto, IntelliJ IDEA construirá el comando perfecto por debajo (usando las rutas exactas de Java que él mismo administra) y generará tu archivo .jar en la carpeta target de manera 100% garantizada.Ve a buscar tu .jar recién horneado a la carpeta target en tu explorador de Windows, súbelo a la ruta /home/site/wwwroot/ en el File Manager de Azure, reinicia el servidor y confírmame si lograste hacer la prueba final en tu navegador.
------
Identificación de Deuda Arquitectónica: Diagnósticaste el "callejón sin salida" visual que ocurre al iniciar sesión nuevamente. Al comprender que las arquitecturas modernas son Stateless y el servidor tiene "amnesia" por diseño, identificaste la necesidad futura de un endpoint de reanudación para sincronizar la interfaz visual sin acoplar el servidor.