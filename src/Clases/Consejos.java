package Clases;

import java.util.Random;

// Entrega un consejo al azar usando la clase Random.
// Su método es static: se usa sin crear objetos (Consejos.consejoAleatorio()).
public class Consejos {
    private static final String[] LISTA = {
            "Espera 24 horas antes de una compra que no planeaste: si todavía la quieres, vuelve a evaluarla.",
            "Regla 50/30/20: 50% para necesidades, 30% para gustos y 20% para ahorrar.",
            "Anota todos tus gastos, incluso los pequeños: los gastos hormiga suman más de lo que crees.",
            "Antes de comprar, pregúntate: ¿lo necesito o solo lo quiero?",
            "Ahorra primero y gasta después: separa tu ahorro apenas recibas tu dinero.",
            "Compara precios en al menos 3 lugares antes de comprar algo caro."
    };

    private static final Random random = new Random();

    public static String consejoAleatorio() {
        int posicion = random.nextInt(LISTA.length); // número al azar entre 0 y LISTA.length - 1
        return LISTA[posicion];
    }
}
