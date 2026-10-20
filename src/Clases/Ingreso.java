package Clases;

import java.time.LocalDate;

public class Ingreso extends Transaccion {

    public Ingreso(double monto, String descripcion) {
        super(monto, LocalDate.now(), descripcion, "Ingreso");
    }

    @Override
    public double getEfectoEnSaldo() {
        return getMonto(); // un ingreso SUMA al saldo
    }

    @Override
    public String getTipo() {
        return "INGRESO";
    }
}
