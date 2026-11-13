package Clases;

import java.time.LocalDate;

// Hija de Transaccion: representa dinero que sale. Pertenece a una Categoria.
public class Gasto extends Transaccion {

    private Categoria categoriaGasto;

    // Sobrecarga de constructores: uno recibe la categoría y el otro no
    public Gasto(double monto, String descripcion, Categoria categoria) {
        super(monto, LocalDate.now(), descripcion, categoria.getNombre());
        this.categoriaGasto = categoria;
    }

    // Constructor 2: sin categoría (usa "General")
    public Gasto(double monto, String descripcion) {
        this(monto, descripcion, new Categoria("General", false, 0)); // si no se indica, usa "General"
    }

    public Categoria getCategoriaGasto() {
        return categoriaGasto;
    }

    // Cambia el objeto Y el nombre que guarda el padre, para que no se desfasen
    public void setCategoriaGasto(Categoria categoriaGasto) {
        this.categoriaGasto = categoriaGasto;
        setCategoria(categoriaGasto.getNombre());
    }

    @Override
    public double getEfectoEnSaldo() {
        return -getMonto(); // un gasto resta del saldo
    }

    @Override
    public String getTipo() {
        return "GASTO";
    }
}