package Clases;

public class Gasto extends Transaccion {

    // Constructor 1: con categoría
    public Gasto(double monto, String descripcion, String categoria) {
        super(monto, descripcion, categoria);
    }

    // Constructor 2: sin categoría (llama al constructor 1 con "General")
    public Gasto(double monto, String descripcion) {
        this(monto, descripcion, "General");
    }

    @Override
    public double getEfectoEnSaldo() {
        return -getMonto();   // un gasto RESTA del saldo
    }

    @Override
    public String getTipo() {
        return "GASTO";
    }
}