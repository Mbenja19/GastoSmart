package Gui;

import java.awt.Component;
import java.util.function.BooleanSupplier;

import javax.swing.JOptionPane;

// Ayudas comunes: mensajes y validaciones (reemplazan a leerNumero, leerCorreo, etc. de App).
public class Util {

    public static void error(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void info(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "GastoSmart", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void aviso(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Atención", JOptionPane.WARNING_MESSAGE);
    }

    // Convierte el texto en número. Si no es válido muestra un error y devuelve null.
    public static Double leerMonto(Component padre, String texto, String campo, boolean permiteCero) {
        double valor;
        try {
            valor = Double.parseDouble(texto.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            error(padre, "En \"" + campo + "\" escribe un número válido (ejemplo: 25.50).");
            return null;
        }
        if (Double.isNaN(valor) || Double.isInfinite(valor)) {
            error(padre, "En \"" + campo + "\" escribe un número válido (ejemplo: 25.50).");
            return null;
        }
        if (valor < 0 || (!permiteCero && valor == 0)) {
            error(padre, permiteCero ? "\"" + campo + "\" no puede ser negativo."
                    : "\"" + campo + "\" debe ser mayor a 0.");
            return null;
        }
        return valor;
    }

    public static boolean esNombreValido(String nombre) {
        if (nombre.isEmpty()) {
            return false;
        }
        for (int i = 0; i < nombre.length(); i++) {
            char c = nombre.charAt(i);
            if (!Character.isLetter(c) && c != ' ') {
                return false;
            }
        }
        return true;
    }

    public static boolean esCorreoValido(String correo) {
        int arroba = correo.indexOf('@');
        int punto = correo.lastIndexOf('.');
        return !correo.contains(" ")
                && arroba > 0
                && arroba == correo.lastIndexOf('@')
                && punto > arroba + 1
                && punto < correo.length() - 1;
    }

    // Muestra un formulario. Si los datos no pasan la validación, vuelve a mostrarlo
    // con lo que el usuario ya había escrito. Devuelve false si cancela.
    public static boolean pedirDatos(Component padre, String titulo, Object[] campos, BooleanSupplier validar) {
        while (true) {
            int r = JOptionPane.showConfirmDialog(padre, campos, titulo,
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (r != JOptionPane.OK_OPTION) {
                return false;
            }
            if (validar.getAsBoolean()) {
                return true;
            }
        }
    }
}
