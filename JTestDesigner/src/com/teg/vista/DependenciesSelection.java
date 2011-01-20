/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DependenciesSelection.java
 *
 * Created on Jan 18, 2011, 12:42:00 PM
 */

package com.teg.vista;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 *
 * @author danielbello
 */
public class DependenciesSelection extends javax.swing.JFrame {

    ArrayList<Method> metodosSeleccion = new ArrayList<Method>();
    /** Creates new form DependenciesSelection */
    public DependenciesSelection() {
        initComponents();
    }

    DependenciesSelection(ArrayList<Method> metodos, Inicio inicio) {

        this.metodosSeleccion = metodos;

        initComponents();

        ArrayList<Method> metodosSet = getMetodosSet(metodos);

        panelSeleccion.setListData(metodosSet.toArray());

    }

    private ArrayList<Method> getMetodosSet(ArrayList<Method> metodos){


        ArrayList<Method> dependenciasMetodos = new ArrayList<Method>();

        for (Method method : metodos) {

            if (method.getName().startsWith("set") == true && method.getParameterTypes().length == 1
                    && method.getReturnType().getName().equals("void")){

                dependenciasMetodos.add(method);
            }
        }
        return dependenciasMetodos;
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        panelSeleccion = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        checkDependencias = new javax.swing.JCheckBox();
        btSiguiente = new javax.swing.JButton();
        btAtras = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setViewportView(panelSeleccion);

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel1.setText("Se han identificado dependencias en los siguientes metodos :");

        jLabel2.setText("Seleccione los metodos a los que desea aplicar dependencias");

        checkDependencias.setText("No deseo aplicar dependencias");
        checkDependencias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkDependenciasActionPerformed(evt);
            }
        });

        btSiguiente.setText("Siguiente..");
        btSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSiguienteActionPerformed(evt);
            }
        });

        btAtras.setText("Atras..");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(69, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel2)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 376, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(77, 77, 77))
            .add(layout.createSequentialGroup()
                .add(77, 77, 77)
                .add(checkDependencias)
                .addContainerGap(227, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(btAtras)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 302, Short.MAX_VALUE)
                .add(btSiguiente)
                .addContainerGap())
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(99, Short.MAX_VALUE)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 345, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(87, 87, 87))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(31, 31, 31)
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 157, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(checkDependencias)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 35, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btSiguiente)
                    .add(btAtras))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void checkDependenciasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkDependenciasActionPerformed
        // TODO add your handling code here:
        if (checkDependencias.isSelected() == true)
        {
            panelSeleccion.setEnabled(false);

            btSiguiente.setEnabled(false);
        
        }else{

            if (checkDependencias.isSelected() == false){

                panelSeleccion.setEnabled(true);

                btSiguiente.setEnabled(true);
            }
        }
    }//GEN-LAST:event_checkDependenciasActionPerformed

    private ArrayList<Method> getMetodosSeleccionados(Object[] lista){

        ArrayList<Method> methods = new ArrayList<Method>();

        for (Object object : lista) {

            String nombreMetodo = object.toString();

            for (Method method : metodosSeleccion){

                if (method.getName().equals(nombreMetodo)){

                    methods.add(method);
                    
                }
            }
        }
        
        
        return null;
    }

    private void btSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSiguienteActionPerformed
        // TODO add your handling code here:
        if (checkDependencias.isSelected() == false){

            Object[] lista = panelSeleccion.getSelectedValues();

            ArrayList<Method> metodosSeleccionados = getMetodosSeleccionados(lista);

            DependenciesEditor editorDependencias = new DependenciesEditor(metodosSeleccionados);

            editorDependencias.setVisible(true);
        }
    }//GEN-LAST:event_btSiguienteActionPerformed

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAtras;
    private javax.swing.JButton btSiguiente;
    private javax.swing.JCheckBox checkDependencias;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList panelSeleccion;
    // End of variables declaration//GEN-END:variables

}
