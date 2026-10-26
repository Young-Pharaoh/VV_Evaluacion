# AGENTS.md — ISWD752 Evaluación práctica 01

Guía persistente para agentes de IA que asistan en esta evaluación.  
**Curso:** ISWD752 · Verificación y Validación de Software (EPN / FIS)  
**Trabajo:** individual · 120 minutos · /10,00 puntos

---

## Qué es esta asignación

Ciclo completo de pruebas sobre el componente Java `CalculadoraEnvio` (cálculo de costo de envío). Se evalúa el **proceso** (planificar → analizar → diseñar → implementar → ejecutar → evidenciar → caja blanca → confirmación → regresión → informe), no la velocidad de programación. La evidencia y el razonamiento técnico pesan más que la cantidad de código.

---

## Estructura del material (no alterar salvo lo indicado)

| Archivo / carpeta | Uso |
|---|---|
| `LEER_PRIMERO.txt` | Orden de lectura y entrega |
| `00_Instrucciones_Evaluacion_Practica.pdf` | Reglas, cronograma, rúbrica |
| `01_Base_de_Prueba_Especificacion.pdf` | Requisitos R1–R5 (base normativa) |
| `02_Plantilla_Evidencias_y_Entrega.docx` | Completar durante la evaluación |
| `03_Proyecto_Inicial_Caja_Negra/` | Proyecto Maven de pruebas |
| `03_Proyecto_Inicial_Caja_Negra/lib/calculadora-envio.jar` | SUT compilado (no modificar / no decompilar en fase negra) |
| `.../CalculadoraEnvioTest.java` | Único archivo de pruebas a completar |

**Entrega final:** carpeta `APELLIDO_NOMBRE_ISWD752_EP01` con documento de evidencias + `CalculadoraEnvioTest.java`. No entregar JAR ni artefactos del IDE.

---

## Componente bajo prueba (SUT)

- **Clase:** `ec.edu.epn.envio.CalculadoraEnvio`
- **Método:** `public double calcular(double monto, boolean premium)`
- **monto:** valor de compra en USD
- **premium:** cliente Premium o no

### Reglas funcionales (base de prueba)

| ID | Regla |
|----|--------|
| R1 | Monto negativo → inválido → `IllegalArgumentException` |
| R2 | Premium y compra **>** USD 100 → envío gratuito (`0`) |
| R3 | Premium y compra **≤** USD 100 → envío USD `3` |
| R4 | No Premium y compra **<** USD 50 → envío USD `7` |
| R5 | No Premium y compra **≥** USD 50 → envío USD `5` |

Fuera de alcance: impuestos, redondeos, descuentos, cupones, otros tipos de cliente.

---

## Proyecto Maven (`03_Proyecto_Inicial_Caja_Negra`)

- Java 17 · JUnit Jupiter 5.10.2 · dependencia `system` al JAR en `lib/`
- Ejecutar: IDE o `mvn test`
- Escribir solo en `src/test/java/ec/edu/epn/envio/CalculadoraEnvioTest.java`
- Plantilla actual: instancia `CalculadoraEnvio` + comentarios; **sin `@Test` aún**

---

## Reglas obligatorias para el agente

1. **No decompilar, desensamblar ni inspeccionar bytecode/JAR** para inferir la implementación durante la fase de caja negra (antes de que el docente libere el fuente V1).
2. **No modificar ni sustituir** `lib/calculadora-envio.jar` hasta que el docente lo indique (fase de nuevo build ~min 85).
3. En caja negra, diseñar a partir **solo** de la especificación (`01_...pdf`). Todo esperado debe trazarse a R1–R5.
4. Seguir el orden de fases y registrar en la plantilla **antes** de saltar a la siguiente.
5. Si una prueba falla: **registrar evidencia primero**; no inventar causas sin datos.
6. **No afirmar** que el software está “libre de defectos”; conclusiones acotadas a alcance y evidencia.
7. Trabajo individual: no compartir código, casos ni resultados entre estudiantes.
8. Ayudar con diseño de casos, JUnit, trazabilidad y redacción de evidencias; **no** completar la evaluación “por el estudiante” de forma que oculte su razonamiento propio si el usuario solo pide estudiar/entender.

---

## Cronograma y productos (obligatorio)

| Min | Fase | Producto |
|-----|------|----------|
| 0–15 | Planificación | PLAN-01 |
| 15–25 | Análisis | ANAL-01 |
| 25–40 | Diseño funcional / caja negra (≥6 casos) | TC-01 |
| 40–55 | Implementación JUnit | `CalculadoraEnvioTest.java` |
| 55–65 | Ejecución y evidencia | EXEC-01 / DEF-01 si aplica |
| 65–85 | Caja blanca (docente libera fuente V1) | WB-01 + pruebas extra si hace falta |
| 85–100 | Nuevo build y confirmación | CONF-01 |
| 100–108 | Regresión | REG-01 |
| 108–115 | Cierre | TSR-01 |
| 115–120 | Defensa oral breve | Pregunta del docente |

### Secuencia de trabajo

1. Leer base de prueba → PLAN-01 antes de crear casos.  
2. Condiciones de prueba → ANAL-01 (CP-01…).  
3. ≥6 casos funcionales solo con especificación → TC-01.  
4. Implementar y ejecutar con JUnit.  
5. EXEC-01: esperado / obtenido / PASS-FAIL; si hay discrepancia → DEF-01 reproducible.  
6. Con fuente V1: decisiones/rutas → WB-01; añadir pruebas si hay ramas no ejercitadas.  
7. Sustituir JAR por build del docente; primero la prueba de la discrepancia → CONF-01.  
8. Reejecutar toda la suite → REG-01.  
9. TSR-01 con conclusión técnica limitada a la evidencia.

---

## Plantilla de evidencias (campos clave)

- **PLAN-01:** objeto, objetivo, base, nivel, riesgo, criterio de salida  
- **ANAL-01:** condiciones CP-* trazadas a R#  
- **TC-01:** monto, premium, esperado, base (mín. 6; hay filas hasta TC-08)  
- **EXEC-01:** esperado, obtenido, PASS/FAIL, evidencia  
- **DEF-01:** caso, discrepancia objetiva, base, pasos, evidencia, conclusión provisional  
- **WB-01:** decisiones True/False ejercitados + casos; pruebas estructurales añadidas  
- **CONF-01 / REG-01 / TSR-01:** confirmación del fix, regresión de la suite, informe final (incl. riesgo residual)

---

## Rúbrica (10,00)

| Criterio | Puntos |
|----------|--------|
| Planificación | 1,0 |
| Análisis y condiciones | 1,0 |
| Diseño funcional / caja negra | 1,5 |
| Implementación JUnit | 1,0 |
| Ejecución y evidencia | 1,0 |
| Discrepancia/defecto | 1,0 |
| Caja blanca y decisiones | 1,5 |
| Confirmación y regresión | 1,0 |
| Informe final | 0,5 |
| Defensa oral | 0,5 |

---

## Cómo debe ayudar el agente por fase

| Fase | Hacer | Evitar |
|------|--------|--------|
| Plan / análisis | Redactar PLAN-01 y CP-* claros, trazables a R1–R5 | Empezar a codear sin plan |
| Diseño | Proponer ≥6 TC con fronteras (0, 50, 100, negativos, premium/no) | Valores que no distingan reglas |
| JUnit | Assertions claras; excepción en R1; nombres ligados a TC-ID | Depender de implementación interna |
| Ejecución | Tablas esperado vs obtenido | Explicar fallos sin evidencia |
| Caja blanca | Solo tras fuente V1; cobertura de decisiones | Decompilar el JAR inicial |
| Confirmación / regresión | Orden: caso de defecto → suite completa | Sustituir JAR antes de tiempo |
| Cierre | Conclusión acotada a evidencia | “Sin defectos” / alcance inventado |

---

## Pistas de diseño (caja negra)

Fronteras útiles a partir de R2–R5 (justificar siempre con R#):

- Monto `< 0` → R1  
- Premium + `> 100` vs `≤ 100` → R2 / R3  
- No Premium + `< 50` vs `≥ 50` → R4 / R5  
- Incluir valores en el límite exacto (50, 100) y un caso negativo tipificado con `assertThrows`

---

## Estado actual del repo de estudiante

- Material oficial presente (PDFs, DOCX, ZIP, proyecto Maven).  
- `CalculadoraEnvioTest.java` es plantilla vacía (sin casos implementados).  
- Aún no hay documento de evidencias completado por el estudiante.  
- Fase esperada al abrir el material: **caja negra** (especificación + JAR; sin fuente).

---

*Última actualización del contexto del agente: lectura del paquete ESTUDIANTE (instrucciones, base de prueba, plantilla, README, pom, test stub y presencia del JAR). No se decompiló la lógica del componente.*
