package Clases;

// Excepción propia: se lanza cuando el saldo no alcanza. Hereda de Exception.
public class SaldoInsuficienteException extends Exception {
    public SaldoInsuficienteException(String mensaje) {
        super(mensaje); // le pasa el mensaje a Exception; se obtiene con getMessage()
    }
}
