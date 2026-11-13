package Clases;

// Objetivo de ahorro: cuánto se quiere juntar (montoObjetivo) y cuánto se lleva (montoAhorrado).
public class Meta {

    private String nombre;
    private double montoObjetivo;
    private double montoAhorrado;

    public Meta(String nombre, double montoObjetivo) {
        this.nombre = nombre;
        this.montoObjetivo = montoObjetivo;
        this.montoAhorrado = 0; // toda meta empieza sin ahorro
    }

    public void agregarAhorro(double monto) {
        montoAhorrado = Math.round((montoAhorrado + monto) * 100.0) / 100.0;
    }

    public double calcularFaltante() {
        double faltante = montoObjetivo - montoAhorrado;
        return Math.round(faltante * 100.0) / 100.0;
    }

    public double calcularProgreso() {
        return (montoAhorrado / montoObjetivo) * 100; // porcentaje completado
    }

    public boolean estaCumplida() {
        return montoAhorrado >= montoObjetivo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getMontoObjetivo() {
        return montoObjetivo;
    }

    public void setMontoObjetivo(double montoObjetivo) {
        this.montoObjetivo = montoObjetivo;
    }

    public double getMontoAhorrado() {
        return montoAhorrado;
    }

    @Override
    public String toString() {
        String texto = String.format("%s | ahorrado S/ %.2f de S/ %.2f | falta S/ %.2f",
                nombre, montoAhorrado, montoObjetivo, calcularFaltante());
        if (estaCumplida()) {
            texto = texto + " | ¡CUMPLIDA!";
        }
        return texto;
    }
}
