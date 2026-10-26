package ec.edu.epn.envio;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de caja negra derivadas de TC-01..TC-08 (base R1-R5).
 * No se inspeccionó el bytecode del JAR para diseñar estos casos.
 */
class CalculadoraEnvioTest {

    private final CalculadoraEnvio calculadora = new CalculadoraEnvio();

    /** TC-01 | R1 | monto negativo → IllegalArgumentException */
    @Test
    void tc01_montoNegativo_lanzaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> calculadora.calcular(-1.0, false));
    }

    /** TC-02 | R2 | Premium y monto > 100 → envío 0 */
    @Test
    void tc02_premium_montoMayor100_envioGratis() {
        assertEquals(0.0, calculadora.calcular(150.0, true));
    }

    /** TC-03 | R3 | Premium y monto = 100 (frontera ≤ 100) → envío 3 */
    @Test
    void tc03_premium_montoIgual100_paga3() {
        assertEquals(3.0, calculadora.calcular(100.0, true));
    }

    /** TC-04 | R3 | Premium y monto < 100 → envío 3 */
    @Test
    void tc04_premium_montoMenor100_paga3() {
        assertEquals(3.0, calculadora.calcular(80.0, true));
    }

    /** TC-05 | R4 | No Premium y monto < 50 → envío 7 */
    @Test
    void tc05_noPremium_montoMenor50_paga7() {
        assertEquals(7.0, calculadora.calcular(49.99, false));
    }

    /** TC-06 | R5 | No Premium y monto = 50 (frontera ≥ 50) → envío 5 */
    @Test
    void tc06_noPremium_montoIgual50_paga5() {
        assertEquals(5.0, calculadora.calcular(50.0, false));
    }

    /** TC-07 | R4 | No Premium y monto = 0 (válido, < 50) → envío 7 */
    @Test
    void tc07_noPremium_montoCero_paga7() {
        assertEquals(7.0, calculadora.calcular(0.0, false));
    }

    /** TC-08 | R2 | Premium y monto justo > 100 → envío 0 */
    @Test
    void tc08_premium_montoJustoMayor100_envioGratis() {
        assertEquals(0.0, calculadora.calcular(100.01, true));
    }
}
