package com.arquitectura.motor_decisiones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MotorDecisionesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MotorDecisionesApplication.class, args);
    }

}
/*
 // 1. La herramienta común que todas las estrategias implementan
public interface EstrategiaEvaluacion {
    void evaluar();
    TipoEvaluacion getTipo();
}

// 2. La Fábrica (El Almacén)
@Component
public class EvaluacionStrategyFactory {

    // Aquí guardamos el catálogo de herramientas listas
    private final Map<TipoEvaluacion, EstrategiaEvaluacion> estrategias;

    // Inyectamos todas las estrategias disponibles automáticamente
    public EvaluacionStrategyFactory(List<EstrategiaEvaluacion> listaEstrategias) {
        this.estrategias = new EnumMap<>(TipoEvaluacion.class);

        // Llenamos nuestro catálogo paso a paso
        for (EstrategiaEvaluacion estrategia : listaEstrategias) {
            this.estrategias.put(estrategia.getTipo(), estrategia);
        }
    }

    // El Service usará este método para pedir la herramienta
    public EstrategiaEvaluacion obtenerEstrategia(TipoEvaluacion tipo) {
        return estrategias.get(tipo);
    }
}


 */
/*
# EJEMPLO SIMPLE

# 📄 2-PLANTILLA — APRENDIZAJE CLAVE DEL DÍA

# 🧱 Ejemplo Simple — Factory vs Strategy (Arequipa → Lima)

---

# 🧠 Qué entendí hoy (explicado simple)

Entendí que:

- **Strategy** es la forma de hacer algo.
- **Factory** es quien decide cuál Strategy entregarme.

En el viaje de Arequipa a Lima:

- Bus, Avión y Tren son las estrategias.
- La agencia de viajes es el Factory.
- Yo soy el Service coordinando el viaje.

La línea exacta es esta:

👉 Factory entrega el vehículo.

👉 Strategy realiza el viaje.

---

# ⚙️ Cómo funciona (paso a paso real)

---

## 1️⃣ El usuario quiere viajar

```
"Quiero ir de Arequipa a Lima"
```

---

## 2️⃣ El Service recibe la petición

```java
String preferencia = "AIRE";
```

El Service todavía evita decidir qué vehículo usar.

---

## 3️⃣ El Factory entra en acción

```java
Vehiculo vehiculo = factory.obtenerVehiculo(preferencia);
```

## ¿Qué hace el Factory?

Busca en su catálogo:

```
"AIRE" → Avión
"TIERRA" → Bus
"TREN" → Tren
```

y entrega el correcto.

🔥 Aquí termina Factory.

---

## 4️⃣ El Strategy empieza a trabajar

```java
vehiculo.viajar();
```

Ahora el vehículo ejecuta su propia lógica.

Si es avión:

```
"Volando a Lima..."
```

Si es bus:

```
"Viajando por carretera..."
```

🔥 Aquí vive Strategy.

---

# 🧱 Código COMPLETO SIMPLE

---

## 1️⃣ STRATEGY (Las formas de viajar)

```java
public interface Vehiculo {
    void viajar();
}
```

---

## 2️⃣ STRATEGY CONCRETA — AVIÓN

```java
public class Avion implements Vehiculo {

    @Override
    public void viajar() {
        System.out.println("Volando de Arequipa a Lima");
    }
}
```

---

## 3️⃣ STRATEGY CONCRETA — BUS

```java
public class Bus implements Vehiculo {

    @Override
    public void viajar() {
        System.out.println("Viajando por carretera a Lima");
    }
}
```

---

# 4️⃣ FACTORY (La agencia)

```java
public class VehiculoFactory {

    public Vehiculo obtenerVehiculo(String tipo) {

        if(tipo.equals("AIRE")) {
            return new Avion();
        }

        return new Bus();
    }
}
```

---

# 5️⃣ SERVICE (El orquestador)

```java
public class ViajeService {

    public void iniciarViaje() {

        VehiculoFactory factory = new VehiculoFactory();

        // FACTORY TRABAJA AQUÍ
        Vehiculo vehiculo = factory.obtenerVehiculo("AIRE");

        // STRATEGY TRABAJA AQUÍ
        vehiculo.viajar();
    }
}
```

---

# 🧱 Regla de oro del día

👉 “Factory entrega la herramienta; Strategy hace el trabajo.”

---

# ❌ Qué pensaba antes

Pensaba que avión, bus y tren eran el Factory.

---

# ✅ Qué entiendo ahora

Entiendo que:

- avión, bus y tren son las estrategias
- Factory es quien decide cuál entregarme

---

# 🔥 Diferencia clave

| Factory | Strategy |
| --- | --- |
| Decide cuál usar | Ejecuta el comportamiento |
| Entrega herramienta | Hace el trabajo |
| Agencia | Vehículo |
| Buscar | Ejecutar |

---

# 🎯 Ejemplo que puedo explicar

“Si quiero viajar de Arequipa a Lima, la Factory es la ventanilla que me entrega el avión o bus correcto. La Strategy es el vehículo viajando realmente.”

---

# 🧠 Dónde vive cada cosa (ARQUITECTURA)

| Pieza | Responsabilidad |
| --- | --- |
| Factory | Elegir vehículo |
| Strategy | Viajar |
| Service | Coordinar |
| String “AIRE” | Transportar preferencia |

---

# 🧠 Frase ancla

👉 “Factory entrega el avión; Strategy despega.”

---

# 🚀 Cómo sé que lo entendí

Porque ahora puedo ver claramente:

```java
factory.obtenerVehiculo(...)
```

👉 Factory

y luego:

```java
vehiculo.viajar()
```

👉 Strategy
 */

//creacion de mi diagrama en
//https://www.notion.so/SPRING-BOOT-ANGULAR-331a978ac3878032ba69e6ef14b380e1#331a978ac38780b9944ec5af3f3e62cb

//lECTURA DE CODIGO, ENTENDEINDO DE COMO DELEGGAR A TRAVEZ DEL PATRON FACTORY Y PATRON STRATEGY