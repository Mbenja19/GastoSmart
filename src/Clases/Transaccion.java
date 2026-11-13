package Clases;

import java.time.LocalDate;

// Clase abstracta: es el molde común de todo movimiento de dinero.
// No se puede hacer "new Transaccion"; se usan sus hijas: Ingreso, Gasto y Ahorro.
public abstract class Transaccion {
    private static int contador = 0; // static: un solo contador compartido por todos los objetos
    private final int id; // final: se asigna una vez y ya no cambia
    private double monto;
    private LocalDate fecha;
    private String descripcion;
    private String categoria;

    public Transaccion(double monto, LocalDate fecha, String descripcion, String categoria) {
        contador++;
        this.id = contador; // cada transacción recibe un id único y automático
        this.monto = monto;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }

    // Métodos abstractos: no tienen cuerpo, cada hija los escribe a su manera
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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return String.format("#%d | %-7s | %-12s | %s | S/ %.2f | %s",
                id, getTipo(), categoria, fecha, monto, descripcion);
    }
}
