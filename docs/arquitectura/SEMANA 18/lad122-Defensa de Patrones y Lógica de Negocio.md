DÍA 122 (Miércoles): Defensa de Patrones y Lógica de Negocio
Prepara la defensa de tu código. ¿Por qué descartaste usar un simple switch o if/else gigantesco para evaluar las respuestas? Pensar como un ingeniero requiere alejarse del código y observar los problemas desde un punto de abstracción alto. Identifica por qué un "switch" limita tu crecimiento y por qué separar los manuales protege tu sistema.
FLUJO
Orquestador: Únicamente recibe la solicitud y delega.

Lógica (Dueño real): El EvaluacionService toma el control. Delega la creación al Factory y ejecuta el comportamiento del Strategy. Todo permanece estable frente a los cambios.

Datos: Transportan el resultado hacia el almacenamiento.
Fórmula la respuesta perfecta: "Diseñé este sistema respetando el Principio Abierto/Cerrado. Delegué la creación de instancias al almacén (Factory) y el comportamiento al manual de reglas (Strategy)."

Detenerte al notar que estás leyendo las mismas líneas sin avanzar demuestra que estás aplicando tu regla de oro: **comprender antes de memorizar**. Vamos a cambiar de estrategia ahora mismo.

---

### 🔍 1. Diagnóstico: ¿Qué concepto falta comprender aquí?

El bloqueo aparece porque, al dibujar el centro de la hoja, parece que los manuales estuvieran en dos sitios al mismo tiempo: arriba en el **Almacén (`Factory`)** y abajo en el **Enchufe (`Interfaz Strategy`)**. Tu mente se pregunta con toda razón: *"Si el Almacén ya me entregó el manual en el paso 3, ¿por qué en el paso 4 el Coordinador tiene que lanzar otra flecha hacia abajo a una cajita punteada?"*

El concepto que falta conectar es **cómo se separa el momento de "conseguir la herramienta" (Factory) del momento de "usarla con un mango estándar" (la Interfaz Strategy)**. La cajita punteada de abajo no es otro lugar físico al que viaja el código; representa **el molde universal** que le permite al Coordinador usar cualquier manual a ciegas sin saber cuál tiene en la mano.

---

### 👦 2. Explicado como a un niño de 11 años (Manteniendo tu metáfora de la Oficina y el Almacén)

Regresemos a la oficina de tu Motor de Decisiones con **el Jefe perezoso, el Almacén y los Sellos Automáticos (tus Manuales)**:

Imagina que el **Coordinador (`EvaluacionService`)** es un **Jefe con los ojos vendados** sentado en el centro de la oficina. Como no quiere aprender a sumar puntos de ningún examen, en su mano derecha sostiene únicamente un **Mango Universal de Madera** (esa es la cajita punteada de tu dibujo: la **Interfaz `EstrategiaEvaluacion**`). Ese mango tiene un solo botón rojo que dice: **`evaluar()`**.

Abajo en tu dibujo ves 3 sellos distintos (**Opción Múltiple**, **Código** y **Arquitectura**). Cada uno califica de forma totalmente diferente por dentro, pero **los tres tienen exactamente la misma rosca en la parte superior** para encajar en el Mango Universal del Jefe.

Mira en cámara lenta cómo ocurren las 4 flechas que estás dibujando:

1. **Flecha 1 (Izquierda a Centro):** El recepcionista entra y pone el examen de papel en la mesa del Jefe (*"1. Pide evaluar"*).


2. **Flecha 2 (Centro hacia Arriba):** El Jefe toca la etiqueta del examen, ve que dice *"CÓDIGO"*, se voltea hacia el estante de arriba (**Almacén / `Factory**`) y le pide al ayudante: *"Oye, búscame el sello para calificar CÓDIGO"*.


3. **Flecha 3 (Arriba hacia Centro):** El ayudante del Almacén busca en su estante, saca el sello de *Código* y se lo entrega al Jefe. **¡Ojo aquí! En este instante el ayudante del Almacén termina su trabajo y se cruza de brazos**. El examen todavía no está calificado; el Jefe solo tiene el sello en la mano.


4. **Flecha 4 (Centro hacia Abajo):** El Jefe encaja ese sello en su **Mango Universal (la cajita punteada / Interfaz)** y presiona el botón rojo (*"4. Ejecuta el comportamiento del manual"*). ¡Pum! El sello estampa la nota en el papel aplicando las reglas de Código, sin que el Jefe jamás se quitara la venda de los ojos.



> **El secreto para tu dibujo:** Dibuja la flecha hacia arriba (al `Factory`) pensando en **"¿Quién me consigue la pieza?"**, y dibuja la flecha hacia abajo (a la `Interfaz Strategy`) pensando en **"¿Qué botón presiono para que la pieza trabaje?"**.
>
>

---

### 🏛️ 3. La Arquitectura: ¿Dónde vive cada responsabilidad?

* **¿Quién es el DUEÑO real de la lógica (Quién decide y calcula)?**
  Los **3 Manuales / Sellos concretos (`Strategy`)** que encerraste en el círculo de abajo. Solo dentro de ellos viven las reglas matemáticas para saber si el estudiante acertó o falló. *(A su vez, el **Almacén / `Factory**` es dueño únicamente de la lógica de búsqueda para saber qué sello entregar)*.


* **¿Quién SOLAMENTE la llama / delega?**
  El **Coordinador (`EvaluacionService` / El Jefe)**. Él no sabe calificar ni sabe en qué cajón están guardados los sellos. Primero llama al Almacén para pedir la pieza (flecha de arriba) y luego llama al Mango Universal para estamparla (flecha de abajo).


* **¿Quién SOLAMENTE transporta datos?**
  El **DTO (`RespuestaEstudianteDTO`)** que trae las respuestas marcadas por el alumno, la **etiqueta (`TipoEvaluacion`)** que le avisa al Almacén qué sello buscar, y el **`FeedbackDTO`** que lleva la nota final hacia la salida.


* **¿Quién permanece ESTABLE cuando cambian las reglas?**
  El **Coordinador (`EvaluacionService`)** y el **Mango Universal (`Interfaz`)**. Si mañana inventas un cuarto sello llamado *"Manual de Diagramas"*, no le cambias las manos al Jefe ni le enseñas reglas nuevas: solo fabricas el nuevo sello con la misma rosca universal y lo guardas en el Almacén.



---

### 🧠 4. Tres preguntas para verificar tu comprensión

Tómate un respiro, mira tu dibujo con estos nuevos ojos y respóndeme con tus propias palabras (si hay algún detalle por pulir, lo ajustaremos con total calma):

1. **La frontera entre arriba y abajo:** En el momento exacto en que termina la **Flecha 3** (cuando el Almacén/`Factory` le entrega el manual al Coordinador), ¿el examen del alumno ya está calificado o qué falta que ocurra en la **Flecha 4**?


2. **El porqué del "Enchufe" (La cajita punteada):** ¿Por qué en tu dibujo el Coordinador (`EvaluacionService`) saca la Flecha 4 hacia la **Interfaz punteada** (el Mango Universal) en lugar de sacar 3 flechas directas hacia cada uno de los 3 libros?


3. **Cazando un error en tu dibujo:** Si un estudiante resuelve un ejercicio de *Arquitectura* y el sistema le calcula mal el puntaje, ¿cuál de las cajas de tu dibujo abrirías para corregir la fórmula matemática: el Coordinador, el Almacén o el libro de *Manual Arquitectura*?