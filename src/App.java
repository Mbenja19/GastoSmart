import java.util.ArrayList;
import java.util.Scanner;

import Clases.Categoria;
import Clases.Consejos;
import Clases.Gasto;
import Clases.Ingreso;
import Clases.Meta;
import Clases.PresupuestoMensual;
import Clases.SaldoInsuficienteException;
import Clases.Transaccion;
import Clases.Usuario;

// Clase principal: contiene el main, muestra el menú y conecta al usuario con las demás clases.
// Los métodos son static porque main es static.
public class App {
    // Scanner: clase de Java que lee lo que el usuario escribe por teclado
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("==================================");
        System.out.println("  GASTOSMART - Control de gastos");
        System.out.println("==================================");

        // Los datos del usuario se piden al ejecutar el programa
        String nombre = leerTexto("Tu nombre: ");
        String correo = leerTexto("Tu correo: ");
        Usuario usuario = new Usuario(nombre, correo);
        usuario.setDineroInicial(leerNumeroNoNegativo("Dinero con el que empiezas: S/ "));
        usuario.getPresupuesto().modificarMonto(leerNumeroNoNegativo("Presupuesto máximo para este mes: S/ "));

        System.out.println("\n¡Hola, " + usuario.getNombre() + "! Ya puedes empezar.");

        // El menú se repite (do-while) hasta que el usuario elige 0
        int opcion;
        do {
            mostrarMenu(usuario);
            opcion = leerOpcion("Elige una opción: ");
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
                case 9:
                    crearMeta(usuario);
                    break;
                case 10:
                    abonarAMeta(usuario);
                    break;
                case 11:
                    verMetas(usuario);
                    break;
                case 12:
                    usarAsistente(usuario);
                    break;
                case 0:
                    System.out.println("¡Hasta pronto, " + usuario.getNombre() + "!");
                    break;
                default:
                    System.out.println("Esa opción no existe.");
            }
        } while (opcion != 0);
    }

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
        System.out.println("9. Crear una meta de ahorro");
        System.out.println("10. Abonar a una meta de ahorro");
        System.out.println("11. Ver mis metas de ahorro");
        System.out.println("12. Asistente de compra: ¿puedo comprar esto?");
        System.out.println("0. Salir");
    }

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
        // Una categoría desactivada no permite registrar gastos
        if (!categoria.isActiva()) {
            System.out.println("Esa categoría está desactivada. Actívala primero (opción 8).");
            return;
        }

        double monto = leerNumero("Monto del gasto: S/ ");
        if (monto <= 0) {
            System.out.println("El monto debe ser mayor a 0.");
            return;
        }
        // try/catch: atrapa el error SaldoInsuficienteException para que el programa no
        // se caiga
        try {
            usuario.validarSaldo(monto);
        } catch (SaldoInsuficienteException e) {
            System.out.println(e.getMessage());
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

        // No permite categorías repetidas
        for (Categoria c : usuario.getCategorias()) {
            if (c.getNombre().equalsIgnoreCase(nombre)) {
                System.out.println("Esa categoría ya existe.");
                return;
            }
        }

        boolean esencial = leerSiNo("¿Es una necesidad (esencial)?");
        double reservado = leerNumeroNoNegativo("Monto que quieres reservar para ella: S/ ");
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

    public static void crearMeta(Usuario usuario) {
        String nombre = leerTexto("¿Para qué quieres ahorrar? (ej: Laptop, Viaje): ");
        double objetivo = leerNumero("¿Cuánto necesitas juntar? S/ ");
        if (objetivo <= 0) {
            System.out.println("La meta debe ser mayor a 0.");
            return;
        }
        usuario.agregarMeta(new Meta(nombre, objetivo));
        System.out.printf("Meta creada: %s por S/ %.2f. ¡Tú puedes!%n", nombre, objetivo);
    }

    public static void abonarAMeta(Usuario usuario) {
        if (usuario.getMetas().isEmpty()) {
            System.out.println("Todavía no tienes metas. Crea una con la opción 9.");
            return;
        }

        System.out.println("\n¿A qué meta quieres abonar?");
        Meta meta = elegirMeta(usuario);
        if (meta == null) {
            return;
        }
        if (meta.estaCumplida()) {
            System.out.println("Esa meta ya está cumplida. ¡Felicidades!");
            return;
        }

        double monto = leerNumero("Monto a abonar: S/ ");
        if (monto <= 0) {
            System.out.println("El monto debe ser mayor a 0.");
            return;
        }
        try {
            usuario.validarSaldo(monto);
        } catch (SaldoInsuficienteException e) {
            System.out.println(e.getMessage());
            return;
        }
        if (monto > meta.calcularFaltante()) {
            System.out.printf("A esta meta solo le faltan S/ %.2f. Abona como máximo eso.%n",
                    meta.calcularFaltante());
            return;
        }

        usuario.abonarAMeta(meta, monto);

        if (meta.estaCumplida()) {
            System.out.println("¡FELICIDADES! Cumpliste tu meta \"" + meta.getNombre() + "\".");
        } else {
            System.out.printf("Abono registrado. Te faltan S/ %.2f para tu meta (%.0f%% completado).%n",
                    meta.calcularFaltante(), meta.calcularProgreso());
        }
        System.out.printf("Saldo actual: S/ %.2f%n", usuario.getSaldoActual());
    }

    public static void verMetas(Usuario usuario) {
        System.out.println("\n--- MIS METAS DE AHORRO ---");
        if (usuario.getMetas().isEmpty()) {
            System.out.println("Todavía no tienes metas. Crea una con la opción 9.");
            return;
        }
        for (int i = 0; i < usuario.getMetas().size(); i++) {
            Meta m = usuario.getMetas().get(i);
            System.out.println((i + 1) + ". " + m);
            System.out.println("   " + dibujarBarra(m.calcularProgreso()));
        }
    }

    // Asistente de compra: dice si una compra es viable según el dinero libre del
    // usuario
    public static void usarAsistente(Usuario usuario) {
        String articulo = leerTexto("¿Qué quieres comprar?: ");
        double precio = leerNumero("¿Cuánto cuesta? S/ ");
        if (precio <= 0) {
            System.out.println("El precio debe ser mayor a 0.");
            return;
        }

        String resultado = usuario.evaluar(precio); // método de la interfaz Evaluable
        double libre = usuario.calcularDineroLibre();

        System.out.println("\n--- ASISTENTE DE COMPRA ---");
        System.out.println("Quieres comprar         : " + articulo);
        System.out.printf("Precio                  : S/ %.2f%n", precio);
        System.out.printf("Tu saldo actual         : S/ %.2f%n", usuario.getSaldoActual());
        System.out.printf("Dinero libre            : S/ %.2f  (lo que queda sin tocar tus necesidades)%n", libre);
        System.out.println("Resultado               : " + resultado);

        switch (resultado) {
            case "VIABLE":
                System.out.printf("Después de comprarlo te quedarían S/ %.2f libres.%n", libre - precio);
                break;
            case "VIABLE CON RIESGO":
                System.out.printf(
                        "Puedes pagarlo, pero usarías más del %.0f%% de tu dinero libre. Te quedarían S/ %.2f.%n",
                        Usuario.LIMITE_RIESGO * 100, libre - precio);
                break;
            default:
                System.out.printf("Te faltan S/ %.2f de dinero libre para comprarlo sin descuidar tus necesidades.%n",
                        precio - libre);
        }

        System.out.println("Consejo: " + Consejos.consejoAleatorio()); // consejo al azar (clase Random)
    }

    // Dibuja una barra de progreso: [#####-----] 50%
    public static String dibujarBarra(double porcentaje) {
        int llenos = (int) Math.round(Math.min(porcentaje, 100) / 10);
        return "[" + "#".repeat(llenos) + "-".repeat(10 - llenos) + "] " + Math.round(porcentaje) + "%";
    }

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
        int numero = leerOpcion("Número de la categoría: ");
        if (numero < 1 || numero > usuario.getCategorias().size()) {
            System.out.println("Ese número de categoría no existe.");
            return null;
        }
        return usuario.getCategorias().get(numero - 1);
    }

    public static Meta elegirMeta(Usuario usuario) {
        for (int i = 0; i < usuario.getMetas().size(); i++) {
            System.out.println((i + 1) + ". " + usuario.getMetas().get(i));
        }
        int numero = leerOpcion("Número de la meta: ");
        if (numero < 1 || numero > usuario.getMetas().size()) {
            System.out.println("Ese número de meta no existe.");
            return null;
        }
        return usuario.getMetas().get(numero - 1);
    }

    // Validación de entrada: no deja que el texto quede vacío
    public static String leerTexto(String mensaje) {
        String texto = "";
        while (texto.isEmpty()) {
            System.out.print(mensaje);
            texto = sc.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("No puede estar vacío.");
            }
        }
        return texto;
    }

    // parseDouble convierte el texto en número. Si escribe letras, lanza un error
    // y el try/catch lo atrapa para volver a preguntar (el programa no se cae)
    public static double leerNumero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                String texto = sc.nextLine().trim().replace(',', '.');
                return Double.parseDouble(texto);
            } catch (NumberFormatException e) {
                System.out.println("Escribe un número válido (ejemplo: 25.50).");
            }
        }
    }

    // Igual que leerNumero, pero no acepta números negativos
    public static double leerNumeroNoNegativo(String mensaje) {
        double numero = leerNumero(mensaje);
        while (numero < 0) {
            System.out.println("No puede ser negativo.");
            numero = leerNumero(mensaje);
        }
        return numero;
    }

    // Igual que leerNumero, pero solo acepta enteros: se usa para elegir opciones
    // de una lista
    public static int leerOpcion(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Opción no válida. Escribe solo el número de una de las opciones que se muestran.");
            }
        }
    }

    // Solo acepta S o N; si escribe otra cosa, vuelve a preguntar
    public static boolean leerSiNo(String mensaje) {
        while (true) {
            String respuesta = leerTexto(mensaje + " (s/n): ").toLowerCase();
            if (respuesta.equals("s")) {
                return true;
            } else if (respuesta.equals("n")) {
                return false;
            }
            System.out.println("Responde solo con S o N.");
        }
    }
}
