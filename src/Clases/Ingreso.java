package Clases;

import java.time.LocalDate;

// Hija de Transaccion: representa dinero que entra.
public class Ingreso extends Transaccion {

    public Ingreso(double monto, String descripcion) {
        super(monto, LocalDate.now(), descripcion, "Ingreso");
    }

    // @Override: reescribe el método abstracto del padre
    @Override
    public double getEfectoEnSaldo() {
        return getMonto(); // un ingreso suma al saldo
    }

    @Override
    public String getTipo() {
        return "INGRESO";
    }
}
