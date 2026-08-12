# 🧱 Día 105 — Aprendizaje clave: La Fábrica Automática, Resiliencia CI/CD y el Artefacto Inmutable

## 🧠 Qué entendí hoy

Entendí que mi computadora de desarrollo no es el centro del universo. Si quiero que mi aplicación funcione en la nube, necesito una fábrica automatizada (**Jenkins**) que trabaje en un entorno aislado.

Esta fábrica necesita:

- Instrucciones claras (`Jenkinsfile`).
- Permisos explícitos para usar las herramientas (`chmod`).
- Un idioma universal para evitar problemas con las tildes (`UTF-8`).
- Pruebas que evalúen el cerebro de mi aplicación sin depender de mi base de datos local.

Si todo sale bien, la fábrica hornea un pastel sellado: el **artefacto `.jar`**, listo para producción.

## ⚙️ Cómo funciona — Paso a paso real

1. Hago un `git push` de mi código a GitHub.
2. Jenkins lee el contrato (`Jenkinsfile`).
3. **El permiso:** el pipeline otorga permisos de ejecución en Linux:
   ```bash
   chmod +x mvnw