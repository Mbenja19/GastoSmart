package Clases;

public abstract class Transaccion {
    private static int contador = 0; // static: UNO solo para todas las transacciones
    private final int id; // final: se asigna una vez y ya no cambia
    private double monto;
    private String descripcion;
    private String categoria;

    public Transaccion(double monto, String descripcion, String categoria) {
        contador++;
        this.id = contador;
        this.monto = monto;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }

    public abstract double getEfectoEnSaldo();

    public abstract String getTipo();

    public int getId() {
        return id;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    @Override
    public String toString() {
        return "#" + id + " | " + getTipo() + " | " + categoria
                + " | S/ " + monto + " | " + descripcion;
    }
}
