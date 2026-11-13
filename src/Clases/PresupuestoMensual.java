package Clases;

import java.time.LocalDate;
import java.util.ArrayList;

// Tope de gasto del mes. Calcula cuánto se gastó este mes y cuánto queda.
public class PresupuestoMensual {
    private double montoMaximo;

    public PresupuestoMensual(double montoMaximo) {
        this.montoMaximo = montoMaximo;
    }

    public void modificarMonto(double nuevoMonto) {
        this.montoMaximo = nuevoMonto;
    }

    public double calcularTotalGastos(ArrayList<Transaccion> transacciones) {
        LocalDate hoy = LocalDate.now();
        double total = 0;
        for (Transaccion t : transacciones) {
            boolean esGasto = t instanceof Gasto; // instanceof: ¿este objeto es un Gasto?
            boolean esDeEsteMes = t.getFecha().getMonthValue() == hoy.getMonthValue()
                    && t.getFecha().getYear() == hoy.getYear();
            if (esGasto && esDeEsteMes) {
                total = total + t.getMonto();
            }
        }
        return Math.round(total * 100.0) / 100.0; // Math.round: redondea a 2 decimales
    }

    public double calcularRestante(ArrayList<Transaccion> transacciones) {
        double restante = montoMaximo - calcularTotalGastos(transacciones);
        return Math.round(restante * 100.0) / 100.0;
    }

    public double getMontoMaximo() {
        return montoMaximo;
    }
}
