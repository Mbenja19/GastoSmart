package Clases;

// Interfaz: es un contrato. Obliga a quien la implemente (Usuario) a escribir el método evaluar.
public interface Evaluable {
    String evaluar(double monto); // devuelve "VIABLE", "VIABLE CON RIESGO" o "NO VIABLE"
}
