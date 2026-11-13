package Clases;

import java.time.LocalDate;

// Hija de Transaccion: dinero que se aparta para una meta de ahorro.
public class Ahorro extends Transaccion {
    public Ahorro(double monto, String descripcion) {
        super(monto, LocalDate.now(), descripcion, "Ahorro");
    }

    @Override
    public double getEfectoEnSaldo() {
        return -getMonto(); // sale del saldo disponible, pero no cuenta como gasto del presupuesto
    }

    @Override
    public String getTipo() {
        return "AHORRO";
    }
}
