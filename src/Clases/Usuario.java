package Clases;

import java.util.ArrayList;

public class Usuario {

    private static int contador = 0;

    private final int id;
    private String nombre;
    private String correo;
    private double dineroInicial;
    private double saldoActual;
    private ArrayList<Transaccion> transacciones;
    private ArrayList<Categoria> categorias;
    private PresupuestoMensual presupuesto;

    public Usuario(String nombre, String correo) {
        contador++;
        this.id = contador;
        this.nombre = nombre;
        this.correo = correo;
        this.dineroInicial = 0;
        this.saldoActual = 0;
        // Composición: el Usuario crea y es dueño de estos objetos
        this.transacciones = new ArrayList<>();
        this.categorias = new ArrayList<>();
        this.presupuesto = new PresupuestoMensual(0);
        // El UML dice 1..* categorías: siempre habrá al menos "General"
        this.categorias.add(new Categoria("General", false, 0));
    }

    // ---------- métodos del UML ----------
    public void agregarTransaccion(Transaccion t) {
        transacciones.add(t);
        calcularSaldo(); // actualiza saldoActual
    }

    public double calcularSaldo() {
        double saldo = dineroInicial;
        for (Transaccion t : transacciones) {
            saldo = saldo + t.getEfectoEnSaldo(); // polimorfismo
        }
        saldoActual = Math.round(saldo * 100.0) / 100.0;
        return saldoActual;
    }

    public void mostrarTransaccion() {
        if (transacciones.isEmpty()) {
            System.out.println("Todavía no hay movimientos.");
            return;
        }
        for (Transaccion t : transacciones) {
            System.out.println(t);
        }
    }

    public ArrayList<Transaccion> filtrarPorCategoria(Categoria categoria) {
        ArrayList<Transaccion> resultado = new ArrayList<>();
        for (Transaccion t : transacciones) {
            if (t.getCategoria().equalsIgnoreCase(categoria.getNombre())) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    // ---------- método que faltaba en el UML ----------
    public void agregarCategoria(Categoria categoria) {
        categorias.add(categoria);
    }

    // ---------- getters y setters ----------
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public double getDineroInicial() {
        return dineroInicial;
    }

    public void setDineroInicial(double dineroInicial) {
        this.dineroInicial = dineroInicial;
        calcularSaldo(); // al cambiar el dinero inicial, se recalcula el saldo
    }

    public double getSaldoActual() {
        return saldoActual;
    }

    public ArrayList<Transaccion> getTransacciones() {
        return transacciones;
    }

    public ArrayList<Categoria> getCategorias() {
        return categorias;
    }

    public PresupuestoMensual getPresupuesto() {
        return presupuesto;
    }

}
