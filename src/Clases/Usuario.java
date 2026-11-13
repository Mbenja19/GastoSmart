package Clases;

import java.util.ArrayList;

// Clase principal del modelo: guarda las transacciones, categorías, metas y el presupuesto.
// Implementa la interfaz Evaluable.
public class Usuario implements Evaluable {

    // Si una compra usa más del 30% del dinero libre, se considera "con riesgo"
    public static final double LIMITE_RIESGO = 0.30; // <-- NUEVO

    private static int contador = 0;

    private final int id;
    private String nombre;
    private String correo;
    private double dineroInicial;
    private double saldoActual;
    private ArrayList<Transaccion> transacciones;
    private ArrayList<Categoria> categorias;
    private ArrayList<Meta> metas;
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
        this.metas = new ArrayList<>();
        this.presupuesto = new PresupuestoMensual(0);
        this.categorias.add(new Categoria("General", false, 0)); // el UML pide al menos 1 categoría
    }

    public void agregarTransaccion(Transaccion t) {
        transacciones.add(t);
        calcularSaldo();
    }

    // Registra el gasto y descuenta la reserva de su categoría.
    // Si la reserva se consume por completo, elimina la categoría y devuelve true.
    public boolean agregarGasto(Gasto gasto) {
        agregarTransaccion(gasto);
        Categoria categoria = gasto.getCategoriaGasto();
        if (categoria.getMontoReservado() > 0) {
            categoria.consumirReserva(gasto.getMonto());
            if (categoria.getMontoReservado() == 0) {
                categorias.remove(categoria);
                return true;
            }
        }
        return false;
    }

    public double calcularSaldo() {
        double saldo = dineroInicial;
        for (Transaccion t : transacciones) {
            // Polimorfismo: cada transacción sabe cómo afecta al saldo (Ingreso suma, Gasto
            // y Ahorro restan)
            saldo = saldo + t.getEfectoEnSaldo();
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

    public void agregarCategoria(Categoria categoria) {
        categorias.add(categoria);
    }

    public void agregarMeta(Meta meta) {
        metas.add(meta);
    }

    // Abonar = sumar a la meta + restar del saldo (se registra como un Ahorro)
    public void abonarAMeta(Meta meta, double monto) {
        meta.agregarAhorro(monto);
        agregarTransaccion(new Ahorro(monto, "Ahorro para " + meta.getNombre()));
    }

    // throws: avisa que este método puede lanzar un error.
    // throw new: lanza el error cuando el monto es mayor al saldo.
    public void validarSaldo(double monto) throws SaldoInsuficienteException {
        if (monto > saldoActual) {
            throw new SaldoInsuficienteException(
                    "No te alcanza. Tienes S/ " + saldoActual + " y necesitas S/ " + monto);
        }
    }

    // Igual que validarSaldo, pero respeta el dinero reservado para las categorías
    // esenciales
    public void validarDineroLibre(double monto) throws SaldoInsuficienteException {
        double libre = calcularDineroLibre();
        if (monto > libre) {
            throw new SaldoInsuficienteException(
                    "No puedes ahorrar eso: tu dinero libre es S/ " + libre
                            + " (el resto está reservado para tus necesidades).");
        }
    }

    // Dinero libre = saldo - lo que aún queda reservado en las categorías
    // esenciales activas
    public double calcularDineroLibre() {
        double pendiente = 0;
        for (Categoria c : categorias) {
            if (c.isEsencial() && c.isActiva()) {
                pendiente = pendiente + c.getMontoReservado();
            }
        }
        double libre = calcularSaldo() - pendiente;
        if (libre < 0) {
            libre = 0;
        }
        return Math.round(libre * 100.0) / 100.0;
    }

    // Método de la interfaz Evaluable: decide si una compra es viable
    @Override
    public String evaluar(double monto) {
        double libre = calcularDineroLibre();
        if (monto > libre) {
            return "NO VIABLE";
        } else if (monto > libre * LIMITE_RIESGO) {
            return "VIABLE CON RIESGO";
        } else {
            return "VIABLE";
        }
    }

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
        calcularSaldo(); // al cambiar el dinero inicial se recalcula el saldo
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

    public ArrayList<Meta> getMetas() {
        return metas;
    }

    public PresupuestoMensual getPresupuesto() {
        return presupuesto;
    }
}
