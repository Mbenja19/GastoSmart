/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Gui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import Clases.Categoria;
import Clases.Gasto;
import Clases.Ingreso;
import Clases.PresupuestoMensual;
import Clases.SaldoInsuficienteException;
import Clases.Transaccion;
import Clases.Usuario;
/**
 *
 * @author elizabet
 */
public class PanelMovimientos extends javax.swing.JPanel {

    private Usuario usuario;
    private Runnable alCambiar;
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[] { "#", "Tipo", "Categoría", "Fecha", "Monto (S/)", "Descripción" }, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private boolean actualizando = false;
    /**
     * Creates new form PanelMovimientosVista
     */
    public PanelMovimientos() {
        initComponents();
    }
    
    public PanelMovimientos(Usuario usuario, Runnable alCambiar) {
        initComponents();
        this.usuario = usuario;
        this.alCambiar = alCambiar;

        tabla.setModel(modelo);
        tabla.setAutoCreateRowSorter(true);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    public void actualizar() {
        actualizando = true;
        int seleccion = filtro.getSelectedIndex();
        filtro.removeAllItems();
        filtro.addItem("Todos los movimientos");
        for (Categoria c : usuario.getCategorias()) {
            filtro.addItem(c.getNombre());
        }
        filtro.setSelectedIndex(seleccion >= 0 && seleccion < filtro.getItemCount() ? seleccion : 0);
        actualizando = false;
        llenarTabla();
    }

    private void llenarTabla() {
        modelo.setRowCount(0);
        int indice = filtro.getSelectedIndex();
        List<Transaccion> lista;
        if (indice <= 0) {
            lista = usuario.getTransacciones();
        } else {
            lista = usuario.filtrarPorCategoria(usuario.getCategorias().get(indice - 1));
        }
        for (Transaccion t : lista) {
            modelo.addRow(new Object[] { t.getId(), t.getTipo(), t.getCategoria(), t.getFecha(),
                    String.format("%.2f", t.getMonto()), t.getDescripcion() });
        }
    }

    private void registrarIngreso() {
        JTextField txtMonto = new JTextField(12);
        JTextField txtDesc = new JTextField(20);
        Double[] monto = new Double[1];

        boolean ok = Util.pedirDatos(this, "Registrar ingreso",
                new Object[] { "Monto del ingreso (S/):", txtMonto, "Descripción:", txtDesc },
                () -> {
                    monto[0] = Util.leerMonto(this, txtMonto.getText(), "Monto", false);
                    if (monto[0] == null) {
                        return false;
                    }
                    if (txtDesc.getText().trim().isEmpty()) {
                        Util.error(this, "La descripción no puede estar vacía.");
                        return false;
                    }
                    return true;
                });
        if (!ok) {
            return;
        }
        usuario.agregarTransaccion(new Ingreso(monto[0], txtDesc.getText().trim()));
        alCambiar.run();
        Util.info(this, String.format("Ingreso registrado. Saldo actual: S/ %.2f", usuario.getSaldoActual()));
    }

    private void registrarGasto() {
        ArrayList<Categoria> activas = new ArrayList<>();
        for (Categoria c : usuario.getCategorias()) {
            if (c.isActiva()) {
                activas.add(c);
            }
        }
        if (activas.isEmpty()) {
            Util.error(this, "No tienes categorías activas. Activa una en la pestaña Categorías.");
            return;
        }

        JComboBox<Categoria> combo = new JComboBox<>(activas.toArray(new Categoria[0]));
        JTextField txtMonto = new JTextField(12);
        JTextField txtDesc = new JTextField(20);
        Double[] monto = new Double[1];

        boolean ok = Util.pedirDatos(this, "Registrar gasto",
                new Object[] { "Categoría:", combo, "Monto del gasto (S/):", txtMonto, "Descripción:", txtDesc },
                () -> {
                    monto[0] = Util.leerMonto(this, txtMonto.getText(), "Monto", false);
                    if (monto[0] == null) {
                        return false;
                    }
                    try {
                        usuario.validarSaldo(monto[0]);
                    } catch (SaldoInsuficienteException e) {
                        Util.error(this, e.getMessage());
                        return false;
                    }
                    if (txtDesc.getText().trim().isEmpty()) {
                        Util.error(this, "La descripción no puede estar vacía.");
                        return false;
                    }
                    return true;
                });
        if (!ok) {
            return;
        }

        Categoria categoria = (Categoria) combo.getSelectedItem();
        boolean reservaCompletada = usuario.agregarGasto(new Gasto(monto[0], txtDesc.getText().trim(), categoria));
        alCambiar.run();

        StringBuilder msg = new StringBuilder(
                String.format("Gasto registrado. Saldo actual: S/ %.2f", usuario.getSaldoActual()));
        boolean hayAviso = false;

        if (reservaCompletada) {
            msg.append("\n\n¡Reserva de \"").append(categoria.getNombre())
                    .append("\" completada! La categoría fue eliminada.");
        } else if (categoria.getMontoReservado() > 0) {
            msg.append(String.format("%n%nReserva de %s: te quedan S/ %.2f.",
                    categoria.getNombre(), categoria.getMontoReservado()));
        }

        PresupuestoMensual p = usuario.getPresupuesto();
        if (p.getMontoMaximo() > 0) {
            double restante = p.calcularRestante(usuario.getTransacciones());
            if (restante < 0) {
                msg.append(String.format("%n%n¡CUIDADO! Superaste tu presupuesto mensual por S/ %.2f.",
                        Math.abs(restante)));
                hayAviso = true;
            } else if (restante <= p.getMontoMaximo() * 0.20) {
                msg.append(String.format("%n%nAtención: te quedan solo S/ %.2f de tu presupuesto mensual.",
                        restante));
                hayAviso = true;
            }
        }

        if (hayAviso) {
            Util.aviso(this, msg.toString());
        } else {
            Util.info(this, msg.toString());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnIngreso = new javax.swing.JButton();
        btnGasto = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        filtro = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setMinimumSize(new java.awt.Dimension(790, 440));
        setPreferredSize(new java.awt.Dimension(790, 440));

        btnIngreso.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnIngreso.setText("Registrar Ingreso");
        btnIngreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngresoActionPerformed(evt);
            }
        });

        btnGasto.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnGasto.setText("Registrar Gasto");
        btnGasto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGastoActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Filtrar por categoría:");

        filtro.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        filtro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filtroActionPerformed(evt);
            }
        });

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(true);
        jScrollPane1.setViewportView(tabla);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnIngreso)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGasto)
                        .addGap(39, 39, 39)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(filtro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 229, Short.MAX_VALUE))
                    .addComponent(jScrollPane1)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnIngreso)
                    .addComponent(btnGasto)
                    .addComponent(jLabel1)
                    .addComponent(filtro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnIngresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngresoActionPerformed
        registrarIngreso();
    }//GEN-LAST:event_btnIngresoActionPerformed

    private void btnGastoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGastoActionPerformed
        registrarGasto();
    }//GEN-LAST:event_btnGastoActionPerformed

    private void filtroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filtroActionPerformed
        if (!actualizando) {
            llenarTabla();
        }
    }//GEN-LAST:event_filtroActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGasto;
    private javax.swing.JButton btnIngreso;
    private javax.swing.JComboBox<String> filtro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables
}
