package Clases;

import java.util.ArrayList;

public class Usuario {
    private static int contador = 0;
    private int id;
    private String nombre;
    private String correo;
    private ArrayList<Transaccion> transacciones;

    public Usuario(String nombre, String correo) {
        contador++;
        this.id = contador;
        this.nombre = nombre;
        this.correo = correo;
        this.transacciones = new ArrayList<Transaccion>();
    }

    public void agregarTransaccion(Transaccion t) {
        transacciones.add(t);
    }

    public void mostrarTransacciones() {
        for(int i = 0; i < transacciones.size(); i++) {
            System.out.println(transacciones.get(i));
        }
    }

    public double calcularSaldo() {
        double saldo = 0;

        for(int i = 0; i < transacciones.size(); i++) {
            saldo += transacciones.get(i).getEfectoEnSaldo();
        }

        return saldo;
    }

    @Override
    public String toString() {
        return "Usuario: " + nombre + "\nCorreo: " + correo;
    }

    


}
