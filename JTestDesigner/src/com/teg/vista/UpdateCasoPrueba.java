/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * UpdateCasoPrueba.java
 *
 * Created on Jan 18, 2011, 5:16:58 PM
 */
package com.teg.vista;

import com.teg.logica.CodeGenerator;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JEditorPane;
import javax.swing.text.EditorKit;
import javax.swing.text.StyledEditorKit;

import org.apache.commons.io.FileUtils;

/**
 *
 * @author maya
 */
public class UpdateCasoPrueba extends javax.swing.JInternalFrame {

    private Inicio inicio;
    private ArrayList<File> jars;
    private String nombreCasoPrueba;
    private File fileJava;
    private CodeGenerator cg = new CodeGenerator();

    /** Creates new form UpdateCasoPrueba */
    public UpdateCasoPrueba(Inicio inicio, String nombreCasoPrueba) {
        initComponents();
        this.inicio = inicio;
        this.jars = cg.getLibJars(nombreCasoPrueba);
        this.nombreCasoPrueba = nombreCasoPrueba;
        this.fileJava = inicio.getJava(nombreCasoPrueba);
        myInits();
        this.setStyle();
        this.setJavaCode(this.fileJava.getPath());
        //this.setJavaCode("/Users/maya/micaso/src/com/codeGeneratorTest/micaso.java");
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        run = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        beanCode = new javax.swing.JEditorPane();
        volver = new javax.swing.JButton();
        compilar = new javax.swing.JButton();
        ejecutar = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        run.setText("Guardar");
        run.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runActionPerformed(evt);
            }
        });

        beanCode.setContentType("text/x-java");
        beanCode.setText("public void miMetodo(){}");
        jScrollPane2.setViewportView(beanCode);

        volver.setText("Volver");
        volver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                volverActionPerformed(evt);
            }
        });

        compilar.setText("Compilar");
        compilar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compilarActionPerformed(evt);
            }
        });

        ejecutar.setText("Ejecutar");
        ejecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ejecutarActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 626, Short.MAX_VALUE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(volver)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 243, Short.MAX_VALUE)
                        .add(run)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(compilar)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(ejecutar)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(volver)
                    .add(ejecutar)
                    .add(compilar)
                    .add(run))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void runActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runActionPerformed
        //compilar y correr el .java
        try {
            String nuevoCodigo = beanCode.getText();
            FileUtils.writeStringToFile(fileJava, nuevoCodigo);
        } catch (IOException ex) {
            Logger.getLogger(UpdateCasoPrueba.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_runActionPerformed

    private void volverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_volverActionPerformed

        this.inicio.updateToPrincipal(this);

    }//GEN-LAST:event_volverActionPerformed

    private void compilarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compilarActionPerformed
        
        String rutaJava = cg.getRutaJava(nombreCasoPrueba);
        String rutaClass = cg.getRutaClass(nombreCasoPrueba);

        File file = new File(rutaJava);
        try {
            String javaContent = FileUtils.readFileToString(file);
            System.out.println("JAVA CONTENT: ");
            System.out.println(javaContent);
        } catch (IOException ex) {
            Logger.getLogger(UpdateCasoPrueba.class.getName()).log(Level.SEVERE, null, ex);
        }

        cg.compileTest(jars, rutaJava, rutaClass);
        
    }//GEN-LAST:event_compilarActionPerformed

    private void ejecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ejecutarActionPerformed
        
        String rutaClass = cg.getRutaClass(nombreCasoPrueba);
        String rutaResultados = cg.getRutaResultados(nombreCasoPrueba);
        Class clase = cg.setClassPath(rutaClass, jars, nombreCasoPrueba);

        cg.runTest(clase, rutaResultados, nombreCasoPrueba);
        
    }//GEN-LAST:event_ejecutarActionPerformed

    private void setJavaCode(String rutaJava) {
        try {
            File file = new File(rutaJava);
            String javaContent = FileUtils.readFileToString(file);
            beanCode.setText(javaContent);
            beanCode.setContentType("text/x-java");
        } catch (IOException ex) {
            Logger.getLogger(UpdateCasoPrueba.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setStyle() {
        beanCode.setEditable(true);
        beanCode.setContentType("text/x-java");
        EditorKit kit = JEditorPane.createEditorKitForContentType("text/x-java");
        if (kit == null) {
            kit = new StyledEditorKit();
        }
        beanCode.setEditorKit(kit);
        beanCode.setEditorKitForContentType("text/x-java", kit);
        kit.install(beanCode);
    }

    public final void myInits() {
        javax.swing.plaf.InternalFrameUI ifu = this.getUI();
        ((javax.swing.plaf.basic.BasicInternalFrameUI) ifu).setNorthPane(null);

        int w2 = this.getSize().width;
        int h2 = this.getSize().height;
        this.inicio.setSize(new Dimension(w2, h2));

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        int w = this.inicio.getSize().width;
        int h = this.inicio.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;

        this.inicio.setLocation(x, y);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane beanCode;
    private javax.swing.JButton compilar;
    private javax.swing.JButton ejecutar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton run;
    private javax.swing.JButton volver;
    // End of variables declaration//GEN-END:variables
}
