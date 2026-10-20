package Clases;

public class Categoria {
    private String nombre;
    private boolean esencial;
    private boolean activa;
    private double montoReservado;

    public Categoria(String nombre, boolean esencial, double montoReservado) {
        this.nombre = nombre;
        this.esencial = esencial;
        this.montoReservado = montoReservado;
        this.activa = true; // toda categoría nueva empieza activa
    }

    public void activar() {
        this.activa = true;
    }

    public void desactivar() {
        this.activa = false;
    }

    // ----- getters y setters -----
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEsencial() {
        return esencial;
    }

    public void setEsencial(boolean esencial) {
        this.esencial = esencial;
    }

    public boolean isActiva() {
        return activa;
    }

    public double getMontoReservado() {
        return montoReservado;
    }

    public void setMontoReservado(double montoReservado) {
        this.montoReservado = montoReservado;
    }

    @Override
    public String toString() {
        String tipo = esencial ? "esencial" : "no esencial";
        String estado = activa ? "activa" : "inactiva";
        return String.format("%s | %s | %s | reservado S/ %.2f", nombre, tipo, estado, montoReservado);
    }
}
