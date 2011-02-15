/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DependenciesEditor.java
 *
 * Created on Jan 25, 2011, 9:16:10 PM
 */
package com.teg.vista;

import com.teg.dominio.CasoPrueba;
import com.teg.dominio.ClaseTest;
import com.teg.dominio.EscenarioPrueba;
import com.teg.dominio.Metodo;
import com.teg.dominio.MockObject;
import com.teg.logica.CodeManager;
import com.teg.logica.XmlManager;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.lang.reflect.Method;
import java.util.ArrayList;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.JEditorPane;
import javax.swing.text.EditorKit;
import javax.swing.text.StyledEditorKit;

import net.miginfocom.swing.MigLayout;

/**
 *
 * @author maya
 */
public class DependenciesEditor extends javax.swing.JInternalFrame {

    private ArrayList<Method> metodosSet = new ArrayList<Method>();
    private ArrayList<Method> metodosSetSeleccionados = new ArrayList<Method>();
    private ArrayList<MockObject> mockObjects = new ArrayList<MockObject>();
    private CasoPrueba casoPrueba = new CasoPrueba();
    private Inicio inicio;
    private JPanel scrollPaneContent;
    private JScrollPane scrollPane;

    /** Creates new form DependenciesEditor */
    public DependenciesEditor(ArrayList<Method> metodosSet, ArrayList<Method> metodosSetSeleccionados, Inicio inicio, CasoPrueba casoPrueba) {
        initComponents();
        this.inicio = inicio;
        this.metodosSet = metodosSet;
        this.metodosSetSeleccionados = metodosSetSeleccionados;
        this.casoPrueba = casoPrueba;
        this.myInits();
        this.paintFrame();
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

    public final void paintFrame() {

        contentPane.setLayout(new MigLayout("fill, flowy"));
        this.setContentPane(contentPane);

        scrollPaneContent = new JPanel(new MigLayout("fill, flowy"));
        scrollPane = new JScrollPane(scrollPaneContent);

        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);

        JButton atras = new JButton("Atras");
        atras.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                atrasButtonActionPerformed();
            }
        });

        JButton finalizar = new JButton("Finalizar");
        finalizar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                finalizarButtonActionPerformed();
            }
        });

        FlowLayout flowLayoutTitulo = new FlowLayout();
        flowLayoutTitulo.setAlignment(FlowLayout.LEFT);

        JLabel dependencias = new JLabel();
        dependencias.setText("DEPENDENCIAS");
        dependencias.setFont(new java.awt.Font("Lucida Grande", 1, 14));

        JPanel titulo = new JPanel();
        titulo.setLayout(new FlowLayout());
        titulo.add(dependencias);

        JPanel botonPanel = new JPanel();
        botonPanel.setPreferredSize(new Dimension(650, 300));
        botonPanel.setLayout(flowLayout);
        botonPanel.add(atras);
        botonPanel.add(finalizar);


        for (Method metodoSetSelected : metodosSetSeleccionados) {
            Class[] paramArreglo = metodoSetSelected.getParameterTypes();
            Class parametro = paramArreglo[0];

            String metodo = metodoSetSelected.getName() + "(" + parametro.getName() + ")";
            String objeto = parametro.getName() + " " + "jmock" + parametro.getSimpleName();

            String metodoObjeto = "<HTML><strong>Método: </strong>" + metodo + "<BR><BR><strong>Objeto: </strong>" + objeto + "<BR><BR></HTML>";
            String escenarios = "<HTML>Seleccione el escenario donde se introducirá el código:<BR><BR></HTML>";
            String codigo = "<HTML><BR>Código:<BR><BR></HTML>";

            MyComboBox myComboBox = new MyComboBox();
            MyEditorPane myEditorPane = new MyEditorPane();

            scrollPaneContent.add(new MyLabel(metodoObjeto, false));
            scrollPaneContent.add(new MyLabel(escenarios, true));
            scrollPaneContent.add(myComboBox);
            scrollPaneContent.add(new MyLabel(codigo, true));
            scrollPaneContent.add(new ScrollEditorPane(myEditorPane));
            scrollPaneContent.add(new MySpaceLabel());
        }

        contentPane.add(titulo);
        this.setPreferredSize(new Dimension(650, 700));
        add(scrollPane, "grow");
        contentPane.add(botonPanel);

        pack();
    }

    private void atrasButtonActionPerformed() {
        // volver a DependenciesSelection
        this.inicio.dependenciesEditorToSelection(this, metodosSet, casoPrueba);
    }

    private void finalizarButtonActionPerformed() {
        // generacion de codigo

        ArrayList<String> escenarios = new ArrayList<String>();
        ArrayList<String> codigos = new ArrayList<String>();

        Component[] componentes = scrollPaneContent.getComponents();

        for (Component componente : componentes) {

            if (componente.getName().equals("escenarios")) {
                JComboBox comboBox = (JComboBox) componente;
                escenarios.add(comboBox.getSelectedItem().toString());
                System.out.println("combobox: " + comboBox.getSelectedItem().toString());
            }

            if (componente.getName().equals("scroll")) {
                JScrollPane scrollPanel = (JScrollPane) componente;
                JViewport view = scrollPanel.getViewport();

                Component[] viewComponents = view.getComponents();

                for (Component component : viewComponents) {
                    if (component.getName().equals("codigo")) {
                        JEditorPane editor = (JEditorPane) component;
                        codigos.add(editor.getText());
                    }
                }
            }
        }

        int cont = 0;

        for (Method metodoSetSelected : metodosSetSeleccionados) {

            Class[] paramArreglo = metodoSetSelected.getParameterTypes();
            Class parametro = paramArreglo[0];
            String parametroCompleto = parametro.getName();
            String parametroSimple = parametro.getSimpleName();

            ClaseTest clase = new ClaseTest(parametroCompleto, parametroSimple);

            String nombreVar = "jmock" + parametro.getSimpleName();

            Metodo metodoSet = new Metodo(metodoSetSelected.getName(), clase);

            MockObject mockObject = new MockObject(metodoSet, nombreVar, escenarios.get(cont), codigos.get(cont));
            mockObjects.add(mockObject);

            cont++;
        }


        casoPrueba.setMockObjects(mockObjects);

        XmlManager xmlManager = new XmlManager();
        xmlManager.setInicio(inicio);
        xmlManager.crearCasoPrueba(this.inicio.getNombreCasoPrueba(), casoPrueba.getEscenariosPrueba(), mockObjects);
    }

    private class MyLabel extends JLabel {

        public MyLabel(String texto, boolean negritas) {

            setName("etiqueta");
            setText(texto);
            setVisible(true);

            if (negritas) {
                setFont(new java.awt.Font("Lucida Grande", 1, 13));
            }

            pack();
        }
    }

    private class MyComboBox extends JComboBox {

        public MyComboBox() {

            ArrayList<EscenarioPrueba> escenarios = casoPrueba.getEscenariosPrueba();

            setName("escenarios");
            for (EscenarioPrueba escenario : escenarios) {
                addItem(escenario.getNombre());
            }
            setPreferredSize(new Dimension(400, 20));
            setVisible(true);

            pack();
        }
    }

    private class MySpaceLabel extends JLabel {

        public MySpaceLabel() {

            setName("espacio");
            setText("<HTML><BR><BR></HTML>");
            setVisible(true);

            pack();
        }
    }

    private class ScrollEditorPane extends JScrollPane {

        public ScrollEditorPane(MyEditorPane editorPane) {

            setName("scroll");
            setViewportView(editorPane);

            editorPane.setEditable(true);
            editorPane.setContentType("text/x-java");
            EditorKit kit = JEditorPane.createEditorKitForContentType("text/x-java");
            if (kit == null) {
                kit = new StyledEditorKit();
            }
            editorPane.setEditorKit(kit);
            editorPane.setEditorKitForContentType("text/x-java", kit);
            kit.install(editorPane);

            editorPane.setText("// TODO add your handling code here:");
            pack();
        }
    }

    private class MyEditorPane extends JEditorPane {

        public MyEditorPane() {

            setName("codigo");
            setPreferredSize(new Dimension(480, 250));
            setContentType("text/x-java");
            setVisible(true);

            pack();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        contentPane = new javax.swing.JPanel();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        org.jdesktop.layout.GroupLayout contentPaneLayout = new org.jdesktop.layout.GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 628, Short.MAX_VALUE)
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 645, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(contentPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(contentPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contentPane;
    // End of variables declaration//GEN-END:variables
}
