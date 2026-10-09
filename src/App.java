import Clases.Gasto;
import Clases.Ingreso;
import Clases.Transaccion;

public class App {
    public static void main(String[] args) throws Exception {
       Ingreso ingreso1 = new Ingreso(1000, "Mesada de octubre");
        Gasto gasto1 = new Gasto(25.5, "Menú universitario", "Comida");
        Gasto gasto2 = new Gasto(10, "Fotocopias");   // categoría "General"

        System.out.println(ingreso1);
        System.out.println(gasto1);
        System.out.println(gasto2);

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
