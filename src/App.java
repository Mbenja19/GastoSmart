import java.util.ArrayList;
import java.util.Scanner;

import Clases.Categoria;
import Clases.Gasto;
import Clases.Ingreso;
import Clases.PresupuestoMensual;
import Clases.Transaccion;
import Clases.Usuario;

public class App {
    // Un solo Scanner para todo el programa
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("   GASTOSMART - Control de gastos");
        System.out.println("=================================");

        // 1) Los datos del usuario se piden al ejecutar el programa
        String nombre = leerTexto("Tu nombre: ");
        String correo = leerTexto("Tu correo: ");
        Usuario usuario = new Usuario(nombre, correo);
        usuario.setDineroInicial(leerNumero("Dinero con el que empiezas: S/ "));
        usuario.getPresupuesto().modificarMonto(leerNumero("Presupuesto máximo para este mes: S/ "));

        System.out.println("\n¡Hola, " + usuario.getNombre() + "! Ya puedes empezar.");

        // 2) Menú que se repite hasta que el usuario elige 0
        int opcion;
        do {
            mostrarMenu(usuario);
            opcion = (int) leerNumero("Elige una opción: ");

            switch (opcion) {
                case 1:
                    registrarIngreso(usuario);
                    break;
                case 2:
                    registrarGasto(usuario);
                    break;
                case 3:
                    System.out.println("\n--- MOVIMIENTOS ---");
                    usuario.mostrarTransaccion();
                    break;
                case 4:
                    verResumen(usuario);
                    break;
                case 5:
                    agregarCategoria(usuario);
                    break;
                case 6:
                    System.out.println("\n--- CATEGORÍAS ---");
                    mostrarCategorias(usuario);
                    break;
                case 7:
                    filtrarPorCategoria(usuario);
                    break;
                case 8:
                    cambiarEstadoCategoria(usuario);
                    break;
                case 0:
                    System.out.println("¡Hasta pronto, " + usuario.getNombre() + "!");
                    break;
                default:
                    System.out.println("Esa opción no existe.");
            }
        } while (opcion != 0);
    }

    // ------------------------------------------------------------
    // MENÚ
    // ------------------------------------------------------------
    public static void mostrarMenu(Usuario usuario) {
        System.out.printf("%n========== MENÚ (saldo: S/ %.2f) ==========%n", usuario.getSaldoActual());
        System.out.println("1. Registrar ingreso");
        System.out.println("2. Registrar gasto");
        System.out.println("3. Ver movimientos");
        System.out.println("4. Ver saldo y presupuesto del mes");
        System.out.println("5. Agregar categoría");
        System.out.println("6. Ver categorías");
        System.out.println("7. Filtrar movimientos por categoría");
        System.out.println("8. Activar o desactivar una categoría");
        System.out.println("0. Salir");
    }

    // ------------------------------------------------------------
    // OPCIONES DEL MENÚ
    // ------------------------------------------------------------
    public static void registrarIngreso(Usuario usuario) {
        double monto = leerNumero("Monto del ingreso: S/ ");
        if (monto <= 0) {
            System.out.println("El monto debe ser mayor a 0.");
            return;
        }
        String descripcion = leerTexto("Descripción: ");
        usuario.agregarTransaccion(new Ingreso(monto, descripcion));
        System.out.printf("Ingreso registrado. Saldo actual: S/ %.2f%n", usuario.getSaldoActual());
    }

    public static void registrarGasto(Usuario usuario) {
        System.out.println("\n¿En qué categoría fue el gasto?");
        Categoria categoria = elegirCategoria(usuario);
        if (categoria == null) {
            return;
        }
        if (!categoria.isActiva()) {
            System.out.println("Esa categoría está desactivada. Actívala primero (opción 8).");
            return;
        }

        double monto = leerNumero("Monto del gasto: S/ ");
        if (monto <= 0) {
            System.out.println("El monto debe ser mayor a 0.");
            return;
        }
        if (monto > usuario.getSaldoActual()) {
            System.out.printf("No te alcanza: tienes S/ %.2f y quieres gastar S/ %.2f.%n",
                    usuario.getSaldoActual(), monto);
            return;
        }

        String descripcion = leerTexto("Descripción: ");
        usuario.agregarTransaccion(new Gasto(monto, descripcion, categoria));
        System.out.printf("Gasto registrado. Saldo actual: S/ %.2f%n", usuario.getSaldoActual());

        // Aviso si se pasó del presupuesto del mes
        double restante = usuario.getPresupuesto().calcularRestante(usuario.getTransacciones());
        if (restante < 0) {
            System.out.printf("¡CUIDADO! Superaste tu presupuesto mensual por S/ %.2f%n", Math.abs(restante));
        }
    }

    public static void verResumen(Usuario usuario) {
        PresupuestoMensual p = usuario.getPresupuesto();
        ArrayList<Transaccion> lista = usuario.getTransacciones();

        System.out.println("\n--- RESUMEN ---");
        System.out.printf("Saldo actual            : S/ %.2f%n", usuario.calcularSaldo());
        System.out.printf("Presupuesto del mes     : S/ %.2f%n", p.getMontoMaximo());
        System.out.printf("Gastado este mes        : S/ %.2f%n", p.calcularTotalGastos(lista));
        System.out.printf("Te queda del presupuesto: S/ %.2f%n", p.calcularRestante(lista));
    }

    public static void agregarCategoria(Usuario usuario) {
        String nombre = leerTexto("Nombre de la categoría: ");

        // Evitar categorías repetidas
        for (Categoria c : usuario.getCategorias()) {
            if (c.getNombre().equalsIgnoreCase(nombre)) {
                System.out.println("Esa categoría ya existe.");
                return;
            }
        }

        boolean esencial = leerSiNo("¿Es una necesidad (esencial)?");
        double reservado = leerNumero("Monto que quieres reservar para ella: S/ ");
        usuario.agregarCategoria(new Categoria(nombre, esencial, reservado));
        System.out.println("Categoría agregada.");
    }

    public static void filtrarPorCategoria(Usuario usuario) {
        System.out.println("\n¿De qué categoría quieres ver los movimientos?");
        Categoria categoria = elegirCategoria(usuario);
        if (categoria == null) {
            return;
        }
        ArrayList<Transaccion> lista = usuario.filtrarPorCategoria(categoria);
        if (lista.isEmpty()) {
            System.out.println("No hay movimientos en " + categoria.getNombre() + ".");
            return;
        }
        System.out.println("\n--- MOVIMIENTOS DE " + categoria.getNombre().toUpperCase() + " ---");
        for (Transaccion t : lista) {
            System.out.println(t);
        }
    }

    public static void cambiarEstadoCategoria(Usuario usuario) {
        System.out.println("\n¿Qué categoría quieres activar o desactivar?");
        Categoria categoria = elegirCategoria(usuario);
        if (categoria == null) {
            return;
        }
        if (categoria.isActiva()) {
            categoria.desactivar();
            System.out.println(categoria.getNombre() + " ahora está DESACTIVADA.");
        } else {
            categoria.activar();
            System.out.println(categoria.getNombre() + " ahora está ACTIVA.");
        }
    }

    // ------------------------------------------------------------
    // MÉTODOS AUXILIARES
    // ------------------------------------------------------------

    // Muestra las categorías numeradas: 1, 2, 3...
    public static void mostrarCategorias(Usuario usuario) {
        ArrayList<Categoria> categorias = usuario.getCategorias();
        for (int i = 0; i < categorias.size(); i++) {
            System.out.println((i + 1) + ". " + categorias.get(i));
        }
    }

    // Deja elegir una categoría por su número. Devuelve null si el número no es
    // válido.
    public static Categoria elegirCategoria(Usuario usuario) {
        mostrarCategorias(usuario);
        int numero = (int) leerNumero("Número de la categoría: ");
        if (numero < 1 || numero > usuario.getCategorias().size()) {
            System.out.println("Ese número de categoría no existe.");
            return null;
        }
        return usuario.getCategorias().get(numero - 1);
    }

    // Lee un texto y no deja que quede vacío
    public static String leerTexto(String mensaje) {
        String texto = "";
        while (texto.isEmpty()) {
            System.out.print(mensaje);
            texto = sc.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("  No puede estar vacío.");
            }
        }
        return texto;
    }

    // Lee un número. Si el usuario escribe letras, avisa y vuelve a preguntar
    public static double leerNumero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                String texto = sc.nextLine().trim().replace(',', '.');
                return Double.parseDouble(texto);
            } catch (NumberFormatException e) {
                System.out.println("  Escribe un número válido (ejemplo: 25.50).");
            }
        }
    }

    // Pregunta sí/no. Devuelve true si la respuesta empieza con "s"
    public static boolean leerSiNo(String mensaje) {
        String respuesta = leerTexto(mensaje + " (s/n): ").toLowerCase();
        return respuesta.startsWith("s");
    }
}
