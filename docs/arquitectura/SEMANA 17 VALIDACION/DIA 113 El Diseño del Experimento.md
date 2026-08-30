# Métricas de la Lección 1

## 1. Métrica de Efectividad Pedagógica

### Objetivo

Medir la **tasa de éxito del estudiante al elegir la opción correcta**, siguiendo una experiencia de gamificación inspirada en Duolingo.

La idea es comprobar si la metáfora, las preguntas y el pseudocódigo ayudan realmente al estudiante a tomar la decisión correcta.

### Flujo de la actividad

#### 1. Aparece la pregunta

El estudiante recibe una pregunta de tipo **Verdadero o Falso**.

![Pregunta Verdadero o Falso](image.png)

#### 2. El estudiante elige una opción

Si selecciona la respuesta correcta:

- Mantiene sus **5 corazones**.
- Obtiene el resultado correspondiente.
- Puede continuar con la siguiente actividad.

Si selecciona una respuesta incorrecta:

- Pierde **1 corazón**.
- Recibe retroalimentación.
- Puede continuar mientras conserve corazones.

![Respuesta seleccionada](image.png)

#### 3. Pregunta de opción múltiple

La lección también incorpora preguntas de **opción múltiple** para evaluar la comprensión del estudiante.

![Opción múltiple](image.png)

#### 4. Retroalimentación

Después de la respuesta, el sistema presenta una retroalimentación que explica el resultado y ayuda al estudiante a comprender el concepto.

![Retroalimentación](image.png)

### ¿Cómo se medirá en la base de datos?

La métrica se registrará mediante el campo:

`decision_correcta` **(Boolean)**

Este campo pertenece a la tabla:

`Progreso`

Ejemplo:

| Usuario | Lección | Intento | decision_correcta |
|---|---:|---:|---|
| Usuario 1 | 1 | 1 | true |
| Usuario 1 | 1 | 1 | false |
| Usuario 1 | 1 | 1 | true |

### Regla de medición

Para evaluar la efectividad pedagógica de la **Lección 1**, se medirán los registros donde:

`decision_correcta = true`

considerando únicamente el **primer intento del usuario**.

### Fórmula

**Tasa de éxito = respuestas correctas en el primer intento / total de decisiones evaluadas × 100**

### Interpretación

Una tasa de éxito alta indicará que el diseño de la lección facilita que el estudiante comprenda el concepto y tome la decisión correcta.

Una tasa baja indicará una oportunidad para revisar:

- La metáfora utilizada.
- La explicación del concepto.
- El pseudocódigo.
- La dificultad de las preguntas.
- La retroalimentación.

### Alcance

La Lección 1 tendrá **6 actividades evaluables**, considerando las actividades principales y omitiendo el cofre de recompensa de esta medición.

---

# 2. Métrica de Engagement

## Objetivo

Medir el **nivel de interacción del estudiante mediante los puntos de experiencia (XP) obtenidos** durante una sesión.

La métrica busca determinar si los elementos de gamificación realmente incentivan al estudiante a continuar interactuando con el sistema.

## ¿Qué se medirá?

El engagement se medirá utilizando los puntos de experiencia obtenidos por el usuario.

El dato principal corresponde al campo:

`puntos_experiencia`

Este campo pertenece a la tabla:

`Usuario`

## Relación con el motor de gamificación

Los puntos de experiencia son calculados por el **motor de gamificación**, implementado mediante el patrón **Strategy**.

El patrón Strategy permite utilizar diferentes estrategias para calcular los puntos, por ejemplo:

- Puntos por respuesta correcta.
- Bono por racha.
- Bono por velocidad.
- Otros bonos definidos por el sistema.

## Regla de medición

Se calculará el **promedio de puntos de experiencia obtenidos por sesión**.

### Fórmula

**XP promedio por sesión = puntos de experiencia acumulados / número de sesiones**

### Ejemplo

| Sesión | XP obtenida |
|---|---:|
| Sesión 1 | 120 |
| Sesión 2 | 180 |
| Sesión 3 | 150 |

**XP promedio = (120 + 180 + 150) / 3 = 150 XP**

### Interpretación

Un promedio mayor de XP por sesión permitirá observar si los elementos de gamificación están incentivando una mayor interacción.

Esta métrica permitirá analizar especialmente el efecto de:

- Bonos por racha.
- Bonos por velocidad.
- Puntos por respuestas correctas.
- Otras estrategias de recompensa.

---
# 3. Métrica de Fricción (Cualitativa y Analítica)

## Objetivo

Registrar y analizar **los errores más comunes al intentar aplicar patrones de diseño**.

Esta métrica analítica busca determinar si los estudiantes están fallando por falta de comprensión del concepto o si la lección necesita ajustes para no inducir al error. El objetivo no es impresionar con velocidad de respuestas, sino auditar la claridad en la toma de decisiones.

## ¿Qué se medirá?

Se analizará exactamente qué botón (opción) presionó el usuario cuando falló.

El dato principal corresponde al campo:

`opcion_elegida_id` **(Llave Foránea)**

Este campo pertenece a la tabla:

`Progreso`

## Regla de medición

Se auditará la tabla `Progreso` para identificar los patrones de error, agrupando y contabilizando las respuestas incorrectas más frecuentes.

### Ejemplo de interpretación

Aplicando el principio de que *"el error es solo información: lo analizamos, aprendemos y avanzamos"*:

*   Si la base de datos revela que la mayoría eligió "Fuerza Bruta" en lugar de "Backtracking", el problema no recae en el estudiante.
*   Es una señal arquitectónica de que la lección necesita un rediseño en su metáfora, justificación o *feedback* para despejar esa fricción.

---

# 4. Relación entre las tres métricas

Las tres métricas evalúan dimensiones diferentes y complementarias del motor de decisiones:

| Métrica | Qué mide | Campo | Tabla |
|---|---|---|---|
| Efectividad pedagógica | Capacidad de elegir correctamente | `decision_correcta` | `Progreso` |
| Engagement | Interacción mediante recompensas | `puntos_experiencia` | `Usuario` |
| Fricción | Naturaleza y tendencia de los errores | `opcion_elegida_id` | `Progreso` |

### Idea central

**Efectividad pedagógica:**  
¿El estudiante está aprendiendo y desarrollando el criterio para tomar decisiones correctas?

**Engagement:**  
¿El motor Strategy está consiguiendo que el estudiante mantenga el interés a través de las recompensas dinámicas?

**Fricción:**  
Cuando el estudiante falla, ¿qué decisión errónea tomó exactamente y cómo podemos mejorar la lección?

Las tres métricas integradas auditan el sistema en su totalidad:

> **Aprendizaje + Motivación + Análisis del Error = Experiencia de aprendizaje inteligente y gamificada**> 