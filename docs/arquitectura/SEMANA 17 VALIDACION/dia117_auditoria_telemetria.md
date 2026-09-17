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