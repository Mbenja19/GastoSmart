package Clases;

public class Ingreso extends Transaccion {

    public Ingreso(double monto, String descripcion) {
        super(monto, descripcion, "Ingreso");
    }

    @Override
    public double getEfectoEnSaldo() {
        return getMonto();   // un ingreso SUMA al saldo
    }

    @Override
    public String getTipo() {
        return "INGRESO";
    }
}
