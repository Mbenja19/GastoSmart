import Clases.Gasto;
import Clases.Ingreso;
import Clases.Transaccion;
import Clases.Usuario;

public class App {
    public static void main(String[] args) throws Exception {

        //Se crea usuario para almacenar las transacciones
        Usuario user1 = new Usuario("Luis", "luis@gmail.com");

       Ingreso ingreso1 = new Ingreso(1000, "Mesada de octubre");
        Gasto gasto1 = new Gasto(25.5, "Menú universitario", "Comida");
        Gasto gasto2 = new Gasto(10, "Fotocopias");   // categoría "General"

        user1.agregarTransaccion(ingreso1);
        user1.agregarTransaccion(gasto1);
        user1.agregarTransaccion(gasto2);

        System.out.println(user1);
        System.out.println("\nTransacciones: ");
        user1.mostrarTransacciones();

        System.out.println("\nSaldo: S/ " + user1.calcularSaldo());
        System.out.println();

        // Probamos un setter
        gasto2.setCategoria("Estudios");
        System.out.println("Después de cambiar la categoría:");
        System.out.println(gasto2);

        // Polimorfismo: una variable de tipo Transaccion puede guardar un Gasto
        Transaccion t = gasto1;
        System.out.println("Efecto en el saldo: " + t.getEfectoEnSaldo());
        System.out.println("El id de gasto1 es: " + gasto1.getId());
    }
}
