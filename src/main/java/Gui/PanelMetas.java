/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Gui;

import java.awt.Component;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import Clases.Meta;
import Clases.SaldoInsuficienteException;
import Clases.Usuario;
/**
 *
 * @author elizabet
 */
public class PanelMetas extends javax.swing.JPanel {

    private Usuario usuario;
    private Runnable alCambiar;
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[] { "Meta", "Objetivo (S/)", "Ahorrado (S/)", "Falta (S/)", "Progreso" }, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    /**
     * Creates new form PanelMetasVista
     */
    public PanelMetas() {
        initComponents();
    }
    
    public PanelMetas(Usuario usuario, Runnable alCambiar) {
        initComponents();
        this.usuario = usuario;
        this.alCambiar = alCambiar;

        tabla.setModel(modelo);
        tabla.setRowHeight(24);
        tabla.getColumnModel().getColumn(4).setCellRenderer(new BarraRenderer());
    }

    public void actualizar() {
        modelo.setRowCount(0);
        for (Meta m : usuario.getMetas()) {
            modelo.addRow(new Object[] { m.getNombre(),
                    String.format("%.2f", m.getMontoObjetivo()),
                    String.format("%.2f", m.getMontoAhorrado()),
                    String.format("%.2f", m.calcularFaltante()),
                    (int) Math.round(m.calcularProgreso()) });
        }
    }

    private void crearMeta() {
        JTextField txtNombre = new JTextField(15);
        JTextField txtObjetivo = new JTextField(10);
        Double[] objetivo = new Double[1];

        boolean ok = Util.pedirDatos(this, "Crear meta de ahorro",
                new Object[] { "¿Para qué quieres ahorrar? (ej: Laptop, Viaje):", txtNombre,
                        "¿Cuánto necesitas juntar? (S/):", txtObjetivo },
                () -> {
                    if (txtNombre.getText().trim().isEmpty()) {
                        Util.error(this, "El nombre de la meta no puede estar vacío.");
                        return false;
                    }
                    objetivo[0] = Util.leerMonto(this, txtObjetivo.getText(), "Objetivo", false);
                    return objetivo[0] != null;
                });
        if (!ok) {
            return;
        }
        usuario.agregarMeta(new Meta(txtNombre.getText().trim(), objetivo[0]));
        alCambiar.run();
    }

    private void abonar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            Util.error(this, "Primero selecciona una meta de la tabla.");
            return;
        }
        Meta meta = usuario.getMetas().get(fila);
        if (meta.estaCumplida()) {
            Util.info(this, "Esa meta ya está cumplida. ¡Felicidades!");
            return;
        }

        JTextField txtMonto = new JTextField(10);
        Double[] monto = new Double[1];

        boolean ok = Util.pedirDatos(this, "Abonar a \"" + meta.getNombre() + "\"",
                new Object[] { String.format("Monto a abonar (S/) - faltan S/ %.2f:", meta.calcularFaltante()),
                        txtMonto },
                () -> {
                    monto[0] = Util.leerMonto(this, txtMonto.getText(), "Monto", false);
                    if (monto[0] == null) {
                        return false;
                    }
                    try {
                        usuario.validarDineroLibre(monto[0]);
                    } catch (SaldoInsuficienteException e) {
                        Util.error(this, e.getMessage());
                        return false;
                    }
                    if (monto[0] > meta.calcularFaltante()) {
                        Util.error(this, String.format("A esta meta solo le faltan S/ %.2f. Abona como máximo eso.",
                                meta.calcularFaltante()));
                        return false;
                    }
                    return true;
                });
        if (!ok) {
            return;
        }

        usuario.abonarAMeta(meta, monto[0]);
        alCambiar.run();

        if (meta.estaCumplida()) {
            Util.info(this, "¡FELICIDADES! Cumpliste tu meta \"" + meta.getNombre() + "\".");
        } else {
            Util.info(this, String.format("Abono registrado. Te faltan S/ %.2f para tu meta (%.0f%% completado).",
                    meta.calcularFaltante(), meta.calcularProgreso()));
        }
    }

    // Dibuja una barra de progreso dentro de la celda de la tabla
    private static class BarraRenderer extends JProgressBar implements TableCellRenderer {
        BarraRenderer() {
            super(0, 100);
            setStringPainted(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object valor, boolean sel, boolean foco,
                int fila, int columna) {
            int porcentaje = (valor instanceof Integer) ? (Integer) valor : 0;
            setValue(Math.min(porcentaje, 100));
            setString(porcentaje + "%");
            return this;
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

        btnNueva = new javax.swing.JButton();
        btnAbonar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setMinimumSize(new java.awt.Dimension(790, 440));
        setPreferredSize(new java.awt.Dimension(790, 440));

        btnNueva.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnNueva.setText("Crear meta");
        btnNueva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevaActionPerformed(evt);
            }
        });

        btnAbonar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnAbonar.setText("Abonar a la meta seleccionada");
        btnAbonar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbonarActionPerformed(evt);
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
        tabla.setMinimumSize(new java.awt.Dimension(790, 440));
        tabla.setPreferredSize(new java.awt.Dimension(790, 440));
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
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnNueva)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAbonar)
                        .addContainerGap(422, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNueva)
                    .addComponent(btnAbonar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevaActionPerformed
        crearMeta();
    }//GEN-LAST:event_btnNuevaActionPerformed

    private void btnAbonarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbonarActionPerformed
        abonar();
    }//GEN-LAST:event_btnAbonarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbonar;
    private javax.swing.JButton btnNueva;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables
}
