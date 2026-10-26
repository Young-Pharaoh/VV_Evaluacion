package ec.edu.epn.envio;

public class CalculadoraEnvio {
    public double calcular(double monto, boolean premium) {
        if (monto < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }

        if (premium) {
            if (monto >= 100) {
                return 0.0;
            }
            return 3.0;
        }

        if (monto < 50) {
            return 7.0;
        }

        return 5.0;
    }
}
