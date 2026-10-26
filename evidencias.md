# ISWD752 — Plantilla de evidencias (completada)

Evaluación práctica 01 | Caja negra + ejecución inicial sobre JAR entregado

> Copiar este contenido a `02_Plantilla_Evidencias_y_Entrega.docx` para la entrega oficial.

---

## PLAN-01 — Planificación

| Elemento | Respuesta |
|----------|-----------|
| Objeto de prueba | Clase `ec.edu.epn.envio.CalculadoraEnvio`, método `public double calcular(double monto, boolean premium)`, versión inicial empaquetada en `lib/calculadora-envio.jar`. |
| Objetivo de prueba | Obtener evidencia suficiente para determinar si el componente satisface R1–R5 con el conjunto de casos diseñados y ejecutados. |
| Base de prueba | Documento `01_Base_de_Prueba_Especificacion.pdf` (reglas R1–R5). |
| Nivel de prueba | Prueba unitaria / de componente (caja negra funcional). |
| Riesgo principal | Fronteras mal interpretadas (50 y 100) o rechazo incorrecto de montos inválidos; falso PASS si no se prueban límites exactos. |
| Criterio de salida | Suite JUnit ejecutada; EXEC-01 completo; DEF-01 si hay discrepancia; conclusión acotada a evidencia (sin afirmar “libre de defectos”). |

---

## ANAL-01 — Condiciones de prueba

| ID | Condición de prueba | Base (R#) |
|----|---------------------|-----------|
| CP-01 | Monto de compra negativo debe rechazarse con `IllegalArgumentException`. | R1 |
| CP-02 | Cliente Premium con compra **estrictamente mayor** a USD 100 obtiene envío gratuito (0). | R2 |
| CP-03 | Cliente Premium con compra **menor o igual** a USD 100 paga USD 3. | R3 |
| CP-04 | Cliente no Premium con compra **estrictamente menor** a USD 50 paga USD 7. | R4 |
| CP-05 | Cliente no Premium con compra **mayor o igual** a USD 50 paga USD 5. | R5 |
| CP-06 | Los umbrales exactos 50 y 100 deben comportarse según ≤ / ≥ / > / < de R2–R5 (valores frontera). | R2, R3, R4, R5 |

---

## TC-01 — Diseño funcional / caja negra

| ID | Monto | Premium | Resultado esperado | Base |
|----|-------|---------|-------------------|------|
| TC-01 | -1.0 | false | `IllegalArgumentException` | R1 |
| TC-02 | 150.0 | true | 0.0 | R2 |
| TC-03 | 100.0 | true | 3.0 | R3 |
| TC-04 | 80.0 | true | 3.0 | R3 |
| TC-05 | 49.99 | false | 7.0 | R4 |
| TC-06 | 50.0 | false | 5.0 | R5 |
| TC-07 | 0.0 | false | 7.0 | R4 |
| TC-08 | 100.01 | true | 0.0 | R2 |

---

## EXEC-01 — Ejecución

**Entorno:** OpenJDK 25 / Maven 3.9.12 · comando `mvn test` · fecha 2026-10-26 16:35 (-05)  
**Evidencia:** `target/surefire-reports/ec.edu.epn.envio.CalculadoraEnvioTest.txt` · salida Maven: Tests run: 8, Failures: 1

| ID | Esperado | Obtenido | PASS/FAIL | Evidencia / observación |
|----|----------|----------|-----------|-------------------------|
| TC-01 | IllegalArgumentException | IllegalArgumentException | PASS | Método `tc01_...` |
| TC-02 | 0.0 | 0.0 | PASS | Método `tc02_...` |
| TC-03 | 3.0 | **0.0** | **FAIL** | `AssertionFailedError: expected: <3.0> but was: <0.0>` → ver DEF-01 |
| TC-04 | 3.0 | 3.0 | PASS | Método `tc04_...` |
| TC-05 | 7.0 | 7.0 | PASS | Método `tc05_...` |
| TC-06 | 5.0 | 5.0 | PASS | Método `tc06_...` |
| TC-07 | 7.0 | 7.0 | PASS | Método `tc07_...` |
| TC-08 | 0.0 | 0.0 | PASS | Método `tc08_...` |

---

## DEF-01 — Registro de discrepancia / defecto

| Campo | Registro |
|-------|----------|
| ID de caso que lo expone | TC-03 |
| Descripción objetiva de la discrepancia | Para Premium con monto exactamente 100.0, la especificación (R3) exige envío 3.0; el JAR devolvió 0.0 (mismo comportamiento que envío gratuito de R2). |
| Base de prueba relacionada | R3 (Premium y compra de hasta USD 100 paga USD 3). Contraste con R2 (solo compras **superiores** a USD 100 son gratis). |
| Resultado esperado | 3.0 |
| Resultado obtenido | 0.0 |
| Pasos mínimos para reproducir | 1) Abrir proyecto Maven `03_Proyecto_Inicial_Caja_Negra`. 2) Ejecutar `mvn test` o el método `tc03_premium_montoIgual100_paga3`. 3) Observar fallo: expected 3.0, was 0.0. Equivalente: `new CalculadoraEnvio().calcular(100.0, true)`. |
| Evidencia disponible | Surefire report + log Maven (Failure en TC-03). Captura de pantalla del IDE recomendada para la entrega. |
| Conclusión provisional | Discrepancia reproducible respecto a R3 en la frontera monto=100 Premium. **Confirmado con fuente V1:** la condición es `if (monto >= 100) return 0.0` (línea 10), que contradice R2/R3 (gratis solo si monto **>** 100). Pendiente build corregido (CONF-01). |

---

## WB-01 — Análisis de caja blanca

**Estado: COMPLETADO** — Fuente: `LIBERACIÓN_01_FUENTE_V1/src/main/java/ec/edu/epn/envio/CalculadoraEnvio.java`

Código relevante (V1):

```java
if (monto < 0) { throw new IllegalArgumentException(...); }  // D1
if (premium) {                                               // D2
    if (monto >= 100) { return 0.0; }                        // D3  ← defecto vs R2/R3
    return 3.0;
}
if (monto < 50) { return 7.0; }                              // D4
return 5.0;
```

| Decisión / condición | True ejercitado | False ejercitado | Caso(s) asociados | ¿Agregar prueba? |
|----------------------|-----------------|------------------|-------------------|------------------|
| D1: `monto < 0` | Sí | Sí | True: TC-01, WB-A · False: resto | WB-A (premium=true + negativo) |
| D2: `premium` | Sí | Sí | True: TC-02..04, TC-08, WB-C · False: TC-05..07, WB-B | No (ya cubierto) |
| D3: `monto >= 100` (rama Premium) | Sí | Sí | True: TC-02, TC-03, TC-08 · False: TC-04, WB-C | WB-C (99.99) |
| D4: `monto < 50` (rama no Premium) | Sí | Sí | True: TC-05, TC-07 · False: TC-06, WB-B | WB-B (monto 200) |

**Hallazgo estructural:** D3 usa `>=` en lugar de `>`; explica el FAIL de TC-03.

Pruebas adicionales creadas a partir del análisis estructural: **WB-A, WB-B, WB-C** en `CalculadoraEnvioTest.java`.

---

## CONF-01 — Confirmación

**Estado: PENDIENTE** — No hay nuevo build/JAR del docente.

| Campo | Registro |
|-------|----------|
| Caso seleccionado | TC-03 (candidato) |
| Razón de selección | Única discrepancia FAIL contra R3 |
| Resultado antes del nuevo build | FAIL (esperado 3.0, obtenido 0.0) |
| Resultado después del nuevo build | *Pendiente* |
| Conclusión | *Pendiente* |

---

## REG-01 — Regresión

**Estado: PENDIENTE** — Ejecutar tras sustituir el JAR.

| Campo | Registro |
|-------|----------|
| Cantidad total de pruebas reejecutadas | *Pendiente* |
| PASS | *Pendiente* |
| FAIL | *Pendiente* |
| Nuevas discrepancias observadas | *Pendiente* |
| Conclusión de regresión | *Pendiente* |

---

## TSR-01 — Informe final de prueba (parcial — caja negra + caja blanca V1)

| Campo | Registro |
|-------|----------|
| Objeto y versión final | JAR inicial + fuente V1 inspeccionada; **sin** build de corrección aún |
| Alcance probado | R1–R5 (TC-01..TC-08) + cobertura de decisiones D1–D4 (WB-A..C) |
| Casos ejecutados | 8 funcionales + 3 estructurales = 11 (tras reejecución WB) |
| Discrepancias/defectos registrados | 1 (DEF-01 / TC-03); causa raíz: `monto >= 100` en V1 |
| Confirmación | No ejecutada (falta nuevo build) |
| Regresión | No ejecutada |
| Riesgo residual / limitaciones | Defecto de frontera Premium=100 sigue presente en el JAR/V1. Dominio no exhaustivo. CONF/REG pendientes. |
| Conclusión técnica final | Evidencia funcional + estructural confirma incumplimiento de R3 en monto=100 Premium por operador `>=` en D3. Resto de decisiones D1–D4 ejercitadas en True y False. No se afirma ausencia de otros defectos. |

---

## Lista de entrega (checklist)

- [x] Documento de evidencias legible (este archivo; copiar a DOCX oficial)
- [x] Casos trazables a R1–R5
- [x] Esperados y obtenidos registrados
- [x] `CalculadoraEnvioTest.java` implementado
- [x] Conclusión limitada a la evidencia
- [x] WB-01 completado (fuente V1)
- [ ] CONF-01 / REG-01 cuando el docente libere nuevo build
- [ ] Defensa oral (en aula)
- [ ] Carpeta final `APELLIDO_NOMBRE_ISWD752_EP01`
