# informe.md — Bitácora de trabajo ISWD752 EP01

Registro cronológico de pasos realizados, resultados y pendientes.  
Fecha de sesión: **2026-10-26**.

---

## Paso 0 — Lectura del material (contexto previo)

**Acción:** Se leyeron `LEER_PRIMERO.txt`, los PDF de instrucciones / base de prueba / plantilla, el README, `pom.xml`, el stub de test y `AGENTS.md`.  
**Resultado:** Se comprendió el ciclo (plan → análisis → diseño → JUnit → ejecución → caja blanca → confirmación → regresión → TSR) y las reglas R1–R5.  
**Restricción respetada:** no se decompiló el JAR ni se modificó `lib/calculadora-envio.jar`.

---

## Paso 1 — PLAN-01 (Planificación)

**Acción:** Definir objeto, objetivo, base, nivel, riesgo y criterio de salida.  
**Resultado:** Registrado en `evidencias.md` (sección PLAN-01).

| Elemento | Valor |
|----------|--------|
| Objeto | `CalculadoraEnvio.calcular(double, boolean)` vía JAR |
| Objetivo | Evidencia sobre cumplimiento de R1–R5 |
| Base | `01_Base_de_Prueba_Especificacion.pdf` |
| Nivel | Componente / unitario (caja negra) |
| Riesgo | Fronteras 50/100 mal aplicadas |
| Salida | Suite ejecutada + EXEC/DEF + conclusión acotada |

---

## Paso 2 — ANAL-01 (Condiciones de prueba)

**Acción:** Derivar CP-01..CP-06 desde R1–R5, incluyendo fronteras.  
**Resultado:**

| ID | Condición | Base |
|----|-----------|------|
| CP-01 | Monto negativo → IllegalArgumentException | R1 |
| CP-02 | Premium y monto > 100 → 0 | R2 |
| CP-03 | Premium y monto ≤ 100 → 3 | R3 |
| CP-04 | No Premium y monto < 50 → 7 | R4 |
| CP-05 | No Premium y monto ≥ 50 → 5 | R5 |
| CP-06 | Valores frontera 50 y 100 distinguen reglas | R2–R5 |

---

## Paso 3 — TC-01 (Diseño caja negra ≥ 6 casos)

**Acción:** Diseñar 8 casos funcionales solo con la especificación.  
**Resultado:**

| ID | Monto | Premium | Esperado | Base |
|----|-------|---------|----------|------|
| TC-01 | -1.0 | false | IllegalArgumentException | R1 |
| TC-02 | 150.0 | true | 0.0 | R2 |
| TC-03 | 100.0 | true | 3.0 | R3 |
| TC-04 | 80.0 | true | 3.0 | R3 |
| TC-05 | 49.99 | false | 7.0 | R4 |
| TC-06 | 50.0 | false | 5.0 | R5 |
| TC-07 | 0.0 | false | 7.0 | R4 |
| TC-08 | 100.01 | true | 0.0 | R2 |

---

## Paso 4 — Implementación JUnit

**Acción:** Completar  
`03_Proyecto_Inicial_Caja_Negra/src/test/java/ec/edu/epn/envio/CalculadoraEnvioTest.java`  
con 8 métodos `@Test` alineados a TC-01..TC-08 (`assertThrows` / `assertEquals`).  
**Resultado:** Archivo implementado. Compilación Maven OK (release 17).

---

## Paso 5 — Ejecución y evidencia (EXEC-01)

**Acción:** En el directorio del proyecto Maven:

```bash
mvn test
```

**Entorno observado:**

- OpenJDK 25.0.4.1
- Apache Maven 3.9.12
- JUnit Jupiter 5.10.2 (descargado de Maven Central)
- Hora aprox.: 2026-10-26 16:35 (-05)

**Resultado global Maven:**

```
Tests run: 8, Failures: 1, Errors: 0, Skipped: 0
BUILD FAILURE
```

**Detalle por caso:**

| ID | Esperado | Obtenido | Estado |
|----|----------|----------|--------|
| TC-01 | excepción | excepción | PASS |
| TC-02 | 0.0 | 0.0 | PASS |
| TC-03 | 3.0 | **0.0** | **FAIL** |
| TC-04 | 3.0 | 3.0 | PASS |
| TC-05 | 7.0 | 7.0 | PASS |
| TC-06 | 5.0 | 5.0 | PASS |
| TC-07 | 7.0 | 7.0 | PASS |
| TC-08 | 0.0 | 0.0 | PASS |

**Evidencia de archivo:**  
`03_Proyecto_Inicial_Caja_Negra/target/surefire-reports/ec.edu.epn.envio.CalculadoraEnvioTest.txt`

Mensaje de fallo:

```
AssertionFailedError: expected: <3.0> but was: <0.0>
at CalculadoraEnvioTest.tc03_premium_montoIgual100_paga3
```

---

## Paso 6 — DEF-01 (Discrepancia)

**Acción:** Registrar defecto sin “arreglar” el caso de prueba (el esperado sigue la especificación R3).  
**Resultado:**

- **Caso:** TC-03  
- **Esperado (R3):** Premium + monto=100.0 → 3.0  
- **Obtenido:** 0.0  
- **Reproducción:** `calcular(100.0, true)` o `mvn test` / método `tc03_...`  
- **Conclusión provisional:** Frontera 100 Premium no cumple R3 en el JAR inicial. Hipótesis (sin ver código): se usa `>= 100` para envío gratis en lugar de `> 100`.

---

## Paso 7 — Documentos generados en esta sesión

| Archivo | Contenido |
|---------|-----------|
| `CalculadoraEnvioTest.java` | 8 pruebas JUnit |
| `evidencias.md` | Plantilla PLAN/ANAL/TC/EXEC/DEF/WB/CONF/REG/TSR completada o marcada pendiente |
| `informe.md` | Este bitácora de pasos y resultados |
| `AGENTS.md` | (sesión anterior) guía para agentes |

**Nota:** El archivo oficial de entrega de evidencias es `02_Plantilla_Evidencias_y_Entrega.docx`. Hay que **copiar** el contenido de `evidencias.md` al DOCX (o PDF) antes de entregar.

---

## Paso 8 — Fases NO ejecutadas (bloqueadas por materiales del docente)

| Fase | Producto | Motivo de bloqueo |
|------|----------|-------------------|
| Caja blanca | WB-01 | Falta fuente V1 |
| Nuevo build / confirmación | CONF-01 | Falta JAR corregido |
| Regresión | REG-01 | Depende del nuevo build |
| Defensa oral | — | En aula con el docente |
| Carpeta entrega | `APELLIDO_NOMBRE_ISWD752_EP01` | Renombrar/empaquetar al cerrar |

---

## Paso 9 — Conclusión técnica parcial (TSR-01 preliminar)

Con la evidencia de la suite de 8 casos sobre el JAR inicial:

1. Se observó **PASS** en TC-01, TC-02, TC-04, TC-05, TC-06, TC-07, TC-08.  
2. Se observó **FAIL** en TC-03 (R3, frontera 100 Premium).  
3. **No** se afirma que el componente esté libre de defectos fuera de este alcance.  
4. Pendiente: caja blanca, confirmación y regresión cuando el docente libere recursos.

---

## Anexo A — Cosas que piden / conviene documentar con fotos (o capturas)

La plantilla habla de “evidencia disponible” y de registrar ejecución; no exige un número fijo de fotos, pero en la práctica conviene adjuntar capturas:

1. **Ejecución JUnit en el IDE** (árbol de resultados: 7 verdes / 1 rojo en TC-03) o salida de `mvn test` en terminal.  
2. **Mensaje de fallo** (expected 3.0 / was 0.0) — surefire o panel de failures del IDE.  
3. **(Opcional)** Vista del proyecto Maven con `lib/calculadora-envio.jar` sin modificar.  
4. **Cuando liberen V1:** captura del código fuente y de WB-01 (decisiones True/False).  
5. **Cuando den el nuevo JAR:** captura de CONF-01 (TC-03 PASS) y REG-01 (suite completa).  
6. **Defensa oral:** no es foto; preparación verbal con DEF-01 y fronteras R2/R3.

---

## Anexo B — Resumen corto: hecho vs falta

### Hecho
- Planificación, análisis y 8 casos de caja negra documentados.  
- `CalculadoraEnvioTest.java` implementado.  
- `mvn test` ejecutado; EXEC-01 y DEF-01 (TC-03) registrados.  
- `evidencias.md` + `informe.md` creados.

### Falta
- Copiar evidencias al **DOCX/PDF oficial** de entrega.  
- WB-01 (fuente V1).  
- Sustituir JAR, CONF-01 y REG-01.  
- Completar TSR-01 final tras confirmación/regresión.  
- Defensa oral y carpeta `APELLIDO_NOMBRE_ISWD752_EP01`.  
- Capturas de pantalla recomendadas (Anexo A).
