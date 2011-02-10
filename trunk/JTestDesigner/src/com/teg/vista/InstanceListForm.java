/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * InstanceListForm.java
 *
 * Created on 20/12/2010, 11:25:35 AM
 */
package com.teg.vista;

import com.teg.dominio.ColeccionInstancia;

import com.teg.logica.WidgetObjectLoading;

import com.thoughtworks.xstream.XStream;

import com.thoughtworks.xstream.io.xml.DomDriver;

import java.awt.BorderLayout;

import java.awt.Dimension;

import java.awt.FlowLayout;

import java.awt.Font;

import java.awt.GridBagConstraints;

import java.awt.GridBagLayout;

import java.awt.Insets;

import java.awt.Point;

import java.awt.event.KeyEvent;

import java.io.File;

import java.io.FileInputStream;

import java.io.FileNotFoundException;

import java.io.FileOutputStream;

import java.io.IOException;

import java.lang.reflect.Field;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.Method;

import java.util.ArrayList;

import java.util.Collection;

import java.util.HashSet;

import java.util.LinkedHashSet;

import java.util.LinkedList;

import java.util.TreeSet;

import java.util.logging.Level;

import java.util.logging.Logger;

import javax.swing.JList;

import javax.swing.JTabbedPane;

import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;

import org.jdom.JDOMException;

import org.metawidget.inspector.composite.CompositeInspector;

import org.metawidget.inspector.composite.CompositeInspectorConfig;

import org.metawidget.inspector.iface.Inspector;

import org.metawidget.inspector.propertytype.PropertyTypeInspector;

import org.metawidget.inspector.xml.XmlInspector;

import org.metawidget.inspector.xml.XmlInspectorConfig;

import org.metawidget.swing.SwingMetawidget;

import org.metawidget.swing.layout.GridBagLayoutConfig;

import org.metawidget.swing.layout.TabbedPaneLayoutDecorator;

import org.metawidget.swing.layout.TabbedPaneLayoutDecoratorConfig;

import org.metawidget.swing.widgetprocessor.binding.beansbinding.BeansBindingProcessor;

import org.metawidget.swing.widgetprocessor.binding.beansbinding.BeansBindingProcessorConfig;


/**
 *
 * @author Daniel
 */
public class InstanceListForm extends javax.swing.JFrame {

    private  ArrayList<Class> clasesJars;

    private com.teg.util.SwingDialog dialogoColeccion;

    private ArrayList<Class> clasesColeccion;

    private ArrayList<Class> clasesGenericos;

    private String path;

    private WidgetObjectLoading listWidget = new WidgetObjectLoading();

    private SwingMetawidget metawidget;

    private Collection coleccion;

    private org.jdom.Document docXml;

    private Class clase;

    private Object instanceClass;

    private javax.swing.JButton buttonCancelar;

    private javax.swing.JButton buttonGuardar;

    private javax.swing.JButton buttonCrearOtro;

    private javax.swing.JPanel buttonPanel;

    private javax.swing.JPanel objectContainer = new javax.swing.JPanel();

    private javax.swing.JTextField textField;

    private boolean esPrimitivo;

    private Inicio inicio;

    private String casoPrueba;

    private int coleccionId;

    private javax.swing.JTabbedPane tabPanel;

    private javax.swing.JPanel panelSeleccion;
    
    private javax.swing.JList listaSeleccionColeccion;

    private javax.swing.JList listaSeleccionObjeto;

    private javax.swing.JButton aceptarSeleccion;

    private javax.swing.JButton cancelarSeleccion;

    private com.teg.dominio.ColeccionInstancia coleccionInstancia;

    public InstanceListForm(Object instance, String dataPath, WidgetObjectLoading listObject, Class argumento, Inicio inicio, int coleccionId) {
        instanceClass = instance;

        path = dataPath;

        listWidget = listObject;

        clase = argumento;

        

        setLista(clase);

        this.inicio = inicio;

        this.casoPrueba = inicio.getNombreCasoPrueba();

        this.coleccionId = coleccionId;

        initComponentsCollection();

    }

    /** Creates new form InstanceListForm */
    public InstanceListForm() {
        initComponents();
    }

    InstanceListForm(Class clasePrimitiva, WidgetObjectLoading listObject, Class argumento, int coleccionId) {

        this.listWidget = listObject;

        this.clase = argumento;

        this.coleccionId = coleccionId;

        setLista(clase);

        initComponentesString();

    }

    InstanceListForm(ArrayList<Class> clasesJar, String path, WidgetObjectLoading listWidget, Class argument, Inicio inicio, int coleccionId) {

        this.clasesJars = clasesJar;

        this.path = path;

        this.listWidget = listWidget;

        clase = argument;

        this.inicio = inicio;

        this.coleccionId = coleccionId;      

        setLista(clase);

        initComponentesGeneric();
      
    }

    InstanceListForm(ColeccionInstancia coleccion ,ArrayList<Class> clasesColeccion,
            ArrayList<Class> obtenerGenericos, ArrayList<Class> obtenerClasesJars,
            String path, WidgetObjectLoading listWidget, Inicio inicio, int coleccionId) {


        this.clasesJars = obtenerClasesJars;

        this.path = path;

        this.coleccionInstancia = coleccion;

        this.listWidget = listWidget;

        this.clasesGenericos = obtenerGenericos;

        this.clasesColeccion = clasesColeccion;

        this.inicio = inicio;

        this.coleccionId = coleccionId;

        if (clasesGenericos.isEmpty() == true){

            initGenericEmpty();

        }else
        {

            initGenericFull();
        }


    }

    private void setLista(Class argumentoClase){

        if (java.util.List.class.isAssignableFrom(argumentoClase)) {

            obtenerListaColeccion(argumentoClase);

        } else {

            if (java.util.Set.class.isAssignableFrom(argumentoClase)) {

                obtenerSetColeccion(argumentoClase);

            } else {

                if (java.util.Queue.class.isAssignableFrom(argumentoClase)) {

                    obtenerQueueColeccion(argumentoClase);
                }
            }
        }

    }

    private boolean verificarDato(Class clase) {

        boolean verificado = false;
        if (clase.getName().equals("java.lang.Integer")

                || clase.getName().equals("java.lang.Float")

                || clase.getName().equals("java.lang.Double")

                || clase.getName().equals("java.lang.Long")

                || clase.getName().equals("java.lang.Short")

                || clase.getName().equals("java.lang.Byte")

                || clase.getName().equals("java.lang.Character")

                || clase.getName().equals("java.lang.String")

                || clase.getName().equals("java.lang.Boolean")

                || clase.isPrimitive() == true) {

            verificado = true;
        }

        return verificado;
    }

    public void InspectObject(Object instance) {

        metawidget = new SwingMetawidget();

        // asociamor al metawidget la instancia que va a manejar el "binding" de propiedades
        metawidget.addWidgetProcessor(new BeansBindingProcessor(
                new BeansBindingProcessorConfig().setUpdateStrategy(UpdateStrategy.READ_WRITE)));

        CompositeInspectorConfig inspectorConfig = null;

        try {
            XmlInspectorConfig xmlConfig = new XmlInspectorConfig();

            File file = new File(path + "/" + "metawidgetData.xml");


            xmlConfig.setInputStream(new FileInputStream(new File(path + "/" + "metawidgetData.xml")));
            PropertyTypeInspector inspector = new PropertyTypeInspector();

            inspectorConfig = new CompositeInspectorConfig().setInspectors(
                    new Inspector[]{new PropertyTypeInspector(),
                        new XmlInspector(xmlConfig)});

        } catch (FileNotFoundException ex) {
        }

        GridBagLayoutConfig nestedLayoutConfig = new GridBagLayoutConfig().setNumberOfColumns(2);

        nestedLayoutConfig.setRequiredAlignment(2);

        TabbedPaneLayoutDecoratorConfig layoutConfig = new TabbedPaneLayoutDecoratorConfig().setLayout(
                new org.metawidget.swing.layout.GridBagLayout(nestedLayoutConfig));

        metawidget.setMetawidgetLayout(new TabbedPaneLayoutDecorator(layoutConfig));

        metawidget.setInspector(new CompositeInspector(inspectorConfig));

        metawidget.setPreferredSize(new java.awt.Dimension(450, 450));

        metawidget.setToInspect(instance);



        objectContainer.add(metawidget);

        objectContainer.validate();


    }

    private void setTextField(){

        textField = new javax.swing.JTextField();

        textField.setPreferredSize(new Dimension(100,50));

        textField.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;

        c.gridy = 2;

        c.anchor = GridBagConstraints.CENTER;

        objectContainer.add(textField, c);

    }

    private void initComponentesString() {

        buttonPanel = new javax.swing.JPanel();

        textField = new javax.swing.JTextField();

        buttonCancelar = new javax.swing.JButton();

        buttonGuardar = new javax.swing.JButton();

        buttonCrearOtro = new javax.swing.JButton();

        textField.setPreferredSize(new Dimension(100, 50));

        textField.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        textField.setMaximumSize(new Dimension(100, 100));

        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);

        buttonPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        objectContainer.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        buttonCancelar.setText("Cancelar");

        buttonCancelar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelarActionPerformed(evt);
            }
        });

        buttonGuardar.setText("Guardar coleccion");

        buttonGuardar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonGuardarStringActionPerformed(evt);
            }
        });

        buttonCrearOtro.setText("Crear otro Objeto");

        buttonCrearOtro.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {

                buttonCrearOtroStringActionPerformed(evt);

            }
        });

        setLayout(new BorderLayout());

        objectContainer.setLayout(new FlowLayout(FlowLayout.CENTER));

        objectContainer.add(textField);

        textField.setLocation(new Point(100, 100));

        textField.setVisible(true);

        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        buttonPanel.add(buttonGuardar);

        buttonPanel.add(buttonCancelar);

        buttonPanel.add(buttonCrearOtro);

        getContentPane().add(objectContainer, BorderLayout.CENTER);

        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setTitle("Editor de Colecciones");

        setSize(500, 500);


    }

    private void llenarListaColeccionSeleccion(javax.swing.JList lista){
        lista.setListData(clasesColeccion.toArray());
    }

    private void llenarListaJarSeleccion(javax.swing.JList lista){

       ArrayList<Class> clasesLista = (ArrayList<Class>) clasesJars.clone();

       clasesLista.add(java.lang.Byte.class);

       clasesLista.add(java.lang.Integer.class);

       clasesLista.add(java.lang.String.class);

       clasesLista.add(java.lang.Short.class);

       clasesLista.add(java.lang.Double.class);

       clasesLista.add(java.lang.Float.class);

       clasesLista.add(java.lang.Boolean.class);

       clasesLista.add(java.lang.Character.class);

       lista.setListData(clasesLista.toArray());

    }

    private void initComponentesGeneric(){

        tabPanel = new javax.swing.JTabbedPane();

        panelSeleccion = new javax.swing.JPanel(false);

        panelSeleccion.setLayout(new GridBagLayout());

        panelSeleccion.setAutoscrolls(true);

        GridBagConstraints c = new GridBagConstraints();

        listaSeleccionColeccion = new javax.swing.JList();

        listaSeleccionColeccion.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        listaSeleccionColeccion.setSize(new Dimension(100,100));

        listaSeleccionColeccion.setMaximumSize(new Dimension(1000,1000));

        listaSeleccionColeccion.setLayoutOrientation(JList.HORIZONTAL_WRAP);

        listaSeleccionColeccion.setVisibleRowCount(-1);

        javax.swing.JScrollPane listScroller = new javax.swing.JScrollPane(listaSeleccionColeccion);

        listScroller.setPreferredSize(new Dimension(250, 300));
        
        llenarListaJarSeleccion(listaSeleccionColeccion);

        c.gridx = 0;

        c.gridy = 0;

        c.insets = new Insets(10,20,0,5);
               
        c.anchor = GridBagConstraints.CENTER;
      
        panelSeleccion.add(listScroller, c);


        aceptarSeleccion = new javax.swing.JButton();

        aceptarSeleccion.setText("Aceptar Seleccion..");

        aceptarSeleccion.setFont(new Font("Calibri",Font.BOLD,12));

        aceptarSeleccion.setSize(new Dimension(20,20));

        aceptarSeleccion.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptarSeleccionActionPerformed(evt);
            }
        });

        c.gridx = 0;

        c.gridy = 1;

        c.anchor = GridBagConstraints.WEST;
        
        panelSeleccion.add(aceptarSeleccion, c);

        cancelarSeleccion = new javax.swing.JButton();

        cancelarSeleccion.setText("Cancelar Seleccion..");

        cancelarSeleccion.setEnabled(false);

        cancelarSeleccion.setSize(new Dimension(20,20));

        cancelarSeleccion.setFont(new Font("Calibri",Font.BOLD,12));

        cancelarSeleccion.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarSeleccionActionPerformed(evt);
            }
        });

        c.gridx = 2;

        c.gridy = 1;

        c.anchor = GridBagConstraints.EAST;
       
        panelSeleccion.add(cancelarSeleccion, c );

        objectContainer = new javax.swing.JPanel(false);

        objectContainer.setLayout(new FlowLayout(FlowLayout.CENTER));

        objectContainer.setAutoscrolls(true);

        buttonPanel = new javax.swing.JPanel();

        buttonCancelar = new javax.swing.JButton();

        buttonGuardar = new javax.swing.JButton();

        buttonGuardar.setEnabled(false);

        buttonCrearOtro = new javax.swing.JButton();

        buttonCrearOtro.setEnabled(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);

        buttonPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        buttonCancelar.setText("Cancelar");

        buttonCancelar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelarActionPerformed(evt);
            }
        });

        buttonGuardar.setText("Guardar Coleccion");

        buttonGuardar.setEnabled(false);

        buttonGuardar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonGuardarGenericoActionPerformed(evt);
            }
        });

        buttonCrearOtro.setText("Crear Otro Objeto");

        buttonCrearOtro.setEnabled(false);

        buttonCrearOtro.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCrearOtroGenericoActionPerformed(evt);
            }
        });

        setLayout(new BorderLayout());

        tabPanel.addTab("Seleccion", panelSeleccion);

        tabPanel.setMnemonicAt(0, KeyEvent.VK_1);

        tabPanel.addTab("Objeto",objectContainer);

        tabPanel.setMnemonicAt(1, KeyEvent.VK_2);

        tabPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        buttonPanel.add(buttonGuardar);

        buttonPanel.add(buttonCancelar);

        buttonPanel.add(buttonCrearOtro);

        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(tabPanel, BorderLayout.CENTER);

        setTitle("Editor de Colecciones");

        setSize(500, 500);
    }

    private void initGenericEmpty(){

         tabPanel = new javax.swing.JTabbedPane();

        panelSeleccion = new javax.swing.JPanel(false);

        panelSeleccion.setLayout(new GridBagLayout());

        panelSeleccion.setAutoscrolls(true);

        GridBagConstraints c = new GridBagConstraints();

        listaSeleccionColeccion = new javax.swing.JList();

        listaSeleccionColeccion.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        listaSeleccionColeccion.setSize(new Dimension(100,100));

        listaSeleccionColeccion.setMaximumSize(new Dimension(1000,1000));

        listaSeleccionColeccion.setLayoutOrientation(JList.HORIZONTAL_WRAP);

        listaSeleccionColeccion.setVisibleRowCount(-1);

        javax.swing.JScrollPane listaColeccionScroller = new javax.swing.JScrollPane(listaSeleccionColeccion);

        listaColeccionScroller.setPreferredSize(new Dimension(200, 300));

        llenarListaColeccionSeleccion(listaSeleccionColeccion);

        c.gridx = 0;

        c.gridy = 0;

        c.insets = new Insets(10,20,0,5);

        c.anchor = GridBagConstraints.CENTER;

        panelSeleccion.add(listaColeccionScroller, c);

        listaSeleccionObjeto = new javax.swing.JList();

        listaSeleccionObjeto.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        listaSeleccionObjeto.setSize(new Dimension(100,100));

        listaSeleccionObjeto.setMaximumSize(new Dimension(1000,1000));

        listaSeleccionObjeto.setLayoutOrientation(JList.HORIZONTAL_WRAP);

        listaSeleccionObjeto.setVisibleRowCount(-1);

        javax.swing.JScrollPane listaObjetoScroller = new javax.swing.JScrollPane(listaSeleccionObjeto);

        listaObjetoScroller.setPreferredSize(new Dimension(200, 300));

        llenarListaJarSeleccion(listaSeleccionObjeto);

        c.gridx = 1;

        c.gridy = 0;

        c.insets = new Insets(10,20,0,5);

        c.anchor = GridBagConstraints.CENTER;

        panelSeleccion.add(listaObjetoScroller, c);


        aceptarSeleccion = new javax.swing.JButton();

        aceptarSeleccion.setText("Aceptar Seleccion..");

        aceptarSeleccion.setFont(new Font("Calibri",Font.BOLD,12));

        aceptarSeleccion.setSize(new Dimension(20,20));

        aceptarSeleccion.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptarSeleccionColeccionActionPerformed(evt);
            }
        });

        c.gridx = 0;

        c.gridy = 1;

        c.anchor = GridBagConstraints.WEST;

        panelSeleccion.add(aceptarSeleccion, c);

        cancelarSeleccion = new javax.swing.JButton();

        cancelarSeleccion.setText("Cancelar Seleccion..");

        cancelarSeleccion.setSize(new Dimension(20,20));

        cancelarSeleccion.setFont(new Font("Calibri",Font.BOLD,12));

        cancelarSeleccion.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarSeleccionActionPerformed(evt);
            }
        });

        c.gridx = 2;

        c.gridy = 1;

        c.anchor = GridBagConstraints.EAST;

        panelSeleccion.add(cancelarSeleccion, c );

        objectContainer = new javax.swing.JPanel(false);

        objectContainer.setLayout(new FlowLayout(FlowLayout.CENTER));

        objectContainer.setAutoscrolls(true);

        buttonPanel = new javax.swing.JPanel();

        buttonCancelar = new javax.swing.JButton();

        buttonGuardar = new javax.swing.JButton();

        buttonGuardar.setEnabled(false);

        buttonCrearOtro = new javax.swing.JButton();

        buttonCrearOtro.setEnabled(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);

        buttonPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        buttonCancelar.setText("Cancelar");

        buttonCancelar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelarActionPerformed(evt);
            }
        });

        buttonGuardar.setText("Guardar Coleccion");

        buttonGuardar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonGuardarActionPerformed(evt);
            }
        });

        buttonCrearOtro.setText("Crear Otro Objeto");

        buttonCrearOtro.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCrearOtroActionPerformed(evt);
            }
        });

        setLayout(new BorderLayout());

        tabPanel.addTab("Seleccion", panelSeleccion);

        tabPanel.setMnemonicAt(0, KeyEvent.VK_1);

        tabPanel.addTab("Objeto",objectContainer);

        tabPanel.setMnemonicAt(1, KeyEvent.VK_2);

        tabPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        buttonPanel.add(buttonGuardar);

        buttonPanel.add(buttonCancelar);

        buttonPanel.add(buttonCrearOtro);

        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(tabPanel, BorderLayout.CENTER);

        setTitle("Editor de Colecciones");

        setSize(700, 700);

    }

    private void initGenericFull(){

         tabPanel = new javax.swing.JTabbedPane();

        panelSeleccion = new javax.swing.JPanel(false);

        panelSeleccion.setLayout(new GridBagLayout());

        panelSeleccion.setAutoscrolls(true);

        GridBagConstraints c = new GridBagConstraints();

        listaSeleccionColeccion = new javax.swing.JList();

        listaSeleccionColeccion.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        listaSeleccionColeccion.setSize(new Dimension(100,100));

        listaSeleccionColeccion.setMaximumSize(new Dimension(1000,1000));

        listaSeleccionColeccion.setLayoutOrientation(JList.HORIZONTAL_WRAP);

        listaSeleccionColeccion.setVisibleRowCount(-1);

        listaSeleccionColeccion.setListData(clasesColeccion.toArray());

        javax.swing.JScrollPane listaColeccionScroller = new javax.swing.JScrollPane(listaSeleccionColeccion);

        listaColeccionScroller.setPreferredSize(new Dimension(250, 300));

        c.gridx = 0;

        c.gridy = 0;

        c.insets = new Insets(10,20,0,5);

        c.anchor = GridBagConstraints.CENTER;

        panelSeleccion.add(listaColeccionScroller, c);


        aceptarSeleccion = new javax.swing.JButton();

        aceptarSeleccion.setText("Aceptar Seleccion..");

        aceptarSeleccion.setFont(new Font("Calibri",Font.BOLD,12));

        aceptarSeleccion.setSize(new Dimension(20,20));

        aceptarSeleccion.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptarSeleccionColeccionActionPerformed(evt);
            }
        });

        c.gridx = 0;

        c.gridy = 1;

        c.anchor = GridBagConstraints.WEST;

        panelSeleccion.add(aceptarSeleccion, c);

        cancelarSeleccion = new javax.swing.JButton();

        cancelarSeleccion.setText("Cancelar Seleccion..");

        cancelarSeleccion.setSize(new Dimension(20,20));

        cancelarSeleccion.setFont(new Font("Calibri",Font.BOLD,12));

        cancelarSeleccion.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarSeleccionActionPerformed(evt);
            }
        });

        c.gridx = 2;

        c.gridy = 1;

        c.anchor = GridBagConstraints.EAST;

        panelSeleccion.add(cancelarSeleccion, c );

        objectContainer = new javax.swing.JPanel(false);

        objectContainer.setLayout(new GridBagLayout());

        objectContainer.setAutoscrolls(true);

        buttonPanel = new javax.swing.JPanel();

        buttonCancelar = new javax.swing.JButton();

        buttonGuardar = new javax.swing.JButton();

        buttonGuardar.setEnabled(false);

        buttonCrearOtro = new javax.swing.JButton();

        buttonCrearOtro.setEnabled(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);

        buttonPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        buttonCancelar.setText("Cancelar");

        buttonCancelar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelarActionPerformed(evt);
            }
        });

        buttonGuardar.setText("Guardar Coleccion");

        buttonGuardar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonGuardarGenericoActionPerformed(evt);
            }
        });

        buttonCrearOtro.setText("Crear Otro Objeto");

        buttonCrearOtro.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCrearOtroGenericoActionPerformed(evt);
            }
        });

        setLayout(new BorderLayout());

        tabPanel.addTab("Seleccion", panelSeleccion);

        tabPanel.setMnemonicAt(0, KeyEvent.VK_1);

        tabPanel.addTab("Objeto",objectContainer);

        tabPanel.setMnemonicAt(1, KeyEvent.VK_2);

        tabPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        buttonPanel.add(buttonGuardar);

        buttonPanel.add(buttonCancelar);

        buttonPanel.add(buttonCrearOtro);

        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(tabPanel, BorderLayout.CENTER);

        setTitle("Editor de Colecciones");

        setSize(700, 700);
    }

    private void aceptarSeleccionColeccionActionPerformed(java.awt.event.ActionEvent evt){

         buttonGuardar.setEnabled(true);

                buttonCrearOtro.setEnabled(true);

        if (clasesGenericos.isEmpty() == true) {

            Class coleccionInstance = (Class) listaSeleccionColeccion.getSelectedValue();

            clase = (Class) listaSeleccionObjeto.getSelectedValue();

            

            setLista(coleccionInstance);



            if (verificarDato(clase) == true) {

                esPrimitivo = true;

                setTextField();

               

            } else {
                try {


                    esPrimitivo = false;

                    Object object = getInstance(clase);

                    InspectObject(object);

                } catch (InstantiationException ex) {

                    Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);


                } catch (IllegalAccessException ex) {

                    Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);

                } catch (NoSuchMethodException ex) {


                    Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);

                } catch (IllegalArgumentException ex) {

                    Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);

                } catch (InvocationTargetException ex) {

                    Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);

                } catch (JDOMException ex) {

                    Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);

                } catch (IOException ex) {

                    Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);

                }


            }

        } else {
            
            Class coleccionInstance = (Class) listaSeleccionColeccion.getSelectedValue();

            clase = clasesGenericos.get(0);
            

           

            setLista(coleccionInstance);

            if (verificarDato(clase) == true) {

                esPrimitivo = true;

                setTextField();

                buttonGuardar.setEnabled(true);

                buttonCrearOtro.setEnabled(true);

            } else {
                try {

                    buttonGuardar.setEnabled(true);

                    buttonCrearOtro.setEnabled(true);

                    esPrimitivo = false;

                    Object object = getInstance(clase);

                    InspectObject(object);

                } catch (InstantiationException ex) {

                    Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);


                } catch (IllegalAccessException ex) {

                    Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);

                } catch (NoSuchMethodException ex) {


                    Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);

                } catch (IllegalArgumentException ex) {

                    Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);

                } catch (InvocationTargetException ex) {

                    Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);

                } catch (JDOMException ex) {

                    Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);

                } catch (IOException ex) {

                    Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);

                }


            }


        }

        aceptarSeleccion.setEnabled(false);

    }

     private void buttonCrearOtroGenericoActionPerformed(java.awt.event.ActionEvent evt) {

         if (esPrimitivo == true){

             coleccion.add(textField.getText());

             textField.setText("");

         }else{

             coleccion.add(metawidget.getToInspect());

        objectContainer.removeAll();

        Object newObject = getNuevoObjetoGenerico(clase);

        this.repaint();

        InspectObject(newObject);


         }


     }

     private void buttonGuardarGenericoActionPerformed(java.awt.event.ActionEvent evt){

         if (esPrimitivo == true){

             coleccion.add(textField.getText());

         }else
         {
             coleccion.add(metawidget.getToInspect());

         }

         listWidget.setColeccion(coleccion);

         this.dispose();

     }



    private void initComponentsCollection() {
        //objectContainer = new javax.swing.JPanel();
        buttonPanel = new javax.swing.JPanel();

        buttonCancelar = new javax.swing.JButton();

        buttonGuardar = new javax.swing.JButton();

        buttonCrearOtro = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);

        buttonPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        objectContainer.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        buttonCancelar.setText("Cancelar");

        buttonCancelar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelarActionPerformed(evt);
            }
        });

        buttonGuardar.setText("Guardar Coleccion");

        buttonGuardar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonGuardarActionPerformed(evt);
            }
        });

        buttonCrearOtro.setText("Crear otro Objeto");

        buttonCrearOtro.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {

                buttonCrearOtroActionPerformed(evt);

            }
        });

        setLayout(new BorderLayout());

        objectContainer.setLayout(new FlowLayout(FlowLayout.CENTER));

        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        buttonPanel.add(buttonGuardar);

        buttonPanel.add(buttonCancelar);

        buttonPanel.add(buttonCrearOtro);


        getContentPane().add(objectContainer, BorderLayout.CENTER);

        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setTitle("Editor de Colecciones");

        setSize(500, 500);


    }

    private void setTipoDatoColeccion(Class claseTipo){

        coleccionInstancia.setTipoDatoColeccion(claseTipo.getName());

    }

    private void aceptarSeleccionActionPerformed(java.awt.event.ActionEvent evt) {

       clase = (Class) listaSeleccionColeccion.getSelectedValue();

       buttonGuardar.setEnabled(true);

       buttonCrearOtro.setEnabled(true);

       setTipoDatoColeccion(clase);

        if (verificarDato(clase) == true){

            esPrimitivo = true;

            setTextField();


        }else{
            try {              

                esPrimitivo = false;

                Object object = getInstance(clase);

                InspectObject(object);

            } catch (InstantiationException ex) {

                Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);


            } catch (IllegalAccessException ex) {

                Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);

            } catch (NoSuchMethodException ex) {


                Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);

            } catch (IllegalArgumentException ex) {

                Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);

            } catch (InvocationTargetException ex) {

                Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);

            } catch (JDOMException ex) {

                Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);

            } catch (IOException ex) {

                Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);

            }


        }



       aceptarSeleccion.setEnabled(false);


    }
    private void cancelarSeleccionActionPerformed(java.awt.event.ActionEvent evt) {

        dialogoColeccion = new com.teg.util.SwingDialog();

        int opcion = dialogoColeccion.advertenciaDialog("Se perderan todos los objetos guardados, Desea continuar ?", this);     

        if (opcion == 0){

            this.coleccion.clear();

            aceptarSeleccion.setEnabled(true);

            buttonGuardar.setEnabled(false);

            buttonCrearOtro.setEnabled(false);
        }
    }

    private void buttonCrearOtroStringActionPerformed(java.awt.event.ActionEvent evt) {

        coleccion.add(textField.getText());

        textField.setText("");
    }

    private void buttonCrearOtroActionPerformed(java.awt.event.ActionEvent evt) {

        coleccion.add(metawidget.getToInspect());

        objectContainer.removeAll();

        Object newObject = getNuevoObjeto();

        this.repaint();

        InspectObject(newObject);


    }

    public void Visible() {


        InspectObject(instanceClass);
        this.setVisible(true);

    }

    private String argumentoColeccion(Class clase) {
        String claseColeccion = "";
        Class[] interfaces = clase.getInterfaces();

        for (Class class1 : interfaces) {

            if (class1.getName().equals("java.util.Map")

                    || class1.getName().equals("java.util.Set")

                    || class1.getName().equals("java.util.List")

                    || class1.getName().equals("java.util.Queue")) {

                claseColeccion = class1.getName();

            }
        }

        return claseColeccion;
    }

    private void obtenerListaColeccion(Class generico) {

        if (generico.getName().equals("java.util.ArrayList")) {

            coleccion = new ArrayList();

        } else {

            if (generico.getName().equals("java.util.LinkedList")) {

                coleccion = new LinkedList();

            }else{

                if(generico.getName().equals("javax.management.AttributeList")){

                    coleccion = new javax.management.AttributeList();

                }else{

                    if(generico.getName().equals("java.util.concurrent.CopyOnWriteArrayList")){

                        coleccion = new java.util.concurrent.CopyOnWriteArrayList();
                    }else

                    {

                        if(generico.getName().equals("javax.management.Relation.RoleList")){

                            coleccion = new javax.management.relation.RoleList();

                        }else{

                            if (generico.getName().equals("javax.management.Relation.RoleUnresolvedList")){

                                coleccion = new javax.management.relation.RoleUnresolvedList();

                            }else{

                                if(generico.getName().equals("java.util.Stack")){

                                    coleccion = new java.util.Stack();

                                }else{

                                    if (generico.getName().equals("java.util.Vector")){

                                        coleccion = new java.util.Vector();
                                    }
                                }

                            }
                        }
                    }
                }


            }


        }



    }

    private void buttonCancelarActionPerformed(java.awt.event.ActionEvent evt) {

        this.dispose();
    }

    private void obtenerSetColeccion(Class argumento) {

        if (argumento.getName().equals("java.util.HashSet")) {

            coleccion = new HashSet();

        } else {

            if (argumento.getName().equals("java.util.TreeSet")) {

                coleccion = new TreeSet();

            } else {

                if (argumento.getName().equals("java.util.LinkedHashSet")) {

                    coleccion = new LinkedHashSet();

                } else {

                    if (argumento.getName().equals("java.util.concurrent.ConcurrentSkipListSet")) {

                        coleccion = new java.util.concurrent.ConcurrentSkipListSet();

                    } else if (argumento.getName().equals("java.util.concurrent.CopyOnWriteArraySet")) {

                        coleccion = new java.util.concurrent.CopyOnWriteArraySet();

                    } else {

                        if (argumento.getName().equals("javax.print.attribute.standard.JobStateReasons")) {

                            coleccion = new javax.print.attribute.standard.JobStateReasons();
                        }
                    }
                }
            }
        }

    }

    private void obtenerQueueColeccion(Class argumento) {

        if (argumento.getName().equals("java.util.PriorityQueue")) {

           coleccion = new java.util.PriorityQueue();

        } else {

            if (argumento.getName().equals("java.util.concurrent.SynchronousQueue")) {

                coleccion = new java.util.concurrent.SynchronousQueue();

            } else {

                if (argumento.getName().equals("java.util.PriorityBlockingQueue")) {

                    coleccion = new java.util.PriorityQueue();
                } else {

                    if (argumento.getName().equals("java.util.LinkedList")) {

                        coleccion = new java.util.LinkedList();
                    } else {

                        if (argumento.getName().equals("java.util.concurrent.LinkedBlockingQueue")) {

                            coleccion = new java.util.concurrent.LinkedBlockingDeque();
                        } else {

                            if (argumento.getName().equals("java.util.concurrent.DelayQueue")) {

                                coleccion = new java.util.concurrent.DelayQueue();

                            } else {

                                if (argumento.getName().equals("java.util.concurrent.ConcurrentLinkedQueue")) {

                                    coleccion = new java.util.concurrent.ConcurrentLinkedQueue();

                                } else {


                                    if (argumento.getName().equals("java.util.ArrayDeque")) {

                                        coleccion = new java.util.ArrayDeque();

                                    }

                                }

                            }

                        }

                    }

                }

            }
        }
    }
  
    public void getColeccion() {

        listWidget.setColeccion(coleccion);
    }

    public void instanciaCampos(Object claseInstancia) throws InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {


        Field[] campos = claseInstancia.getClass().getDeclaredFields();

        for (Field field : campos) {

            boolean flag = false;

            if (!field.getType().isPrimitive()

                    && verificarDato(field.getType()) == false) {

                Method[] metodosClase = claseInstancia.getClass().getDeclaredMethods();

                for (Method method : metodosClase) {

                    if (method.getParameterTypes().length > 0) {

                        if (method.getParameterTypes()[0].getName().equals(field.getType().getName())

                                && (method.getReturnType().getName() == null ? "void" == null : method.getReturnType().getName().equals("void"))

                                && flag == false) {

                            Object campoInstance = field.getType().newInstance();

                            claseInstancia.getClass().getMethod(method.getName(), field.getType()).invoke(claseInstancia, campoInstance);

                            flag = true;

                            instanciaCampos(campoInstance);

                        }
                    }
                }
            }
        }
    }

    public Object instanciarNuevoObjeto(Class clase) throws InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, JDOMException, IOException {



        Object claseInstancia = clase.newInstance();

        Field[] campos = clase.getDeclaredFields();

        for (Field field : campos) {

            boolean flag = false;

            if (!field.getType().isPrimitive()

                    && verificarDato(field.getType()) == false) {

                Method[] metodosClase = clase.getDeclaredMethods();

                for (Method method : metodosClase) {

                    if (method.getParameterTypes().length == 1

                            && method.getParameterTypes()[0].getName().equals(field.getType().getName())

                            && (method.getReturnType().getName() == null ? "void" == null : method.getReturnType().getName().equals("void"))

                            && flag == false) {

                        Object campoInstance = field.getType().newInstance();

                        clase.getMethod(method.getName(), field.getType()).invoke(claseInstancia, campoInstance);

                        flag = true;

                        instanciaCampos(campoInstance);
                    }
                }
            }
        }


        return claseInstancia;
    }

    private Object getNuevoObjetoGenerico(Class nuevaClase){

        Object nuevoObjeto = null;

        try {

            nuevoObjeto = instanciarNuevoObjeto(nuevaClase);

        } catch (InstantiationException ex) {

            Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {

            Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);

        } catch (NoSuchMethodException ex) {

            Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IllegalArgumentException ex) {

            Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);

        } catch (InvocationTargetException ex) {

            Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);

        } catch (JDOMException ex) {

            Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {

            Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);

        }

        return nuevoObjeto;

    }

    private Object getNuevoObjeto() {
        Class nuevaClase = instanceClass.getClass();

        Object nuevoObjeto = null;
        try {
            nuevoObjeto = instanciarNuevoObjeto(nuevaClase);
        } catch (InstantiationException ex) {
            Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JDOMException ex) {
            Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(InstanceListForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        return nuevoObjeto;
    }

    private void buttonGuardarStringActionPerformed(java.awt.event.ActionEvent evt) {

        coleccion.add(textField.getText());

        listWidget.setColeccion(coleccion);



        this.dispose();
    }

    private void buttonGuardarActionPerformed(java.awt.event.ActionEvent evt) {

        coleccion.add(metawidget.getToInspect());

        this.crearXML(coleccion, casoPrueba);

        this.dispose();

    }

     public void deepInstantiate(Object claseInstancia, org.jdom.Element raiz) throws InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {

        raiz.addContent("\n");

        org.jdom.Element entidad = getEntity(claseInstancia.getClass());

        raiz.addContent(entidad);

        Field[] campos = claseInstancia.getClass().getDeclaredFields();

        for (Field field : campos) {

            boolean flag = false;

            if (!field.getType().isPrimitive() && verificarDato(field.getType()) == false) {

                Method[] metodosClase = claseInstancia.getClass().getDeclaredMethods();

                for (Method method : metodosClase) {

                    if (method.getParameterTypes().length > 0) {

                        if (method.getParameterTypes()[0].getName().equals(field.getType().getName())
                                && (method.getReturnType().getName() == null ? "void" == null : method.getReturnType().getName().equals("void"))
                                && flag == false) {

                            Object campoInstance = field.getType().newInstance();

                            claseInstancia.getClass().getMethod(method.getName(), field.getType()).invoke(claseInstancia, campoInstance);

                            flag = true;

                            deepInstantiate(campoInstance, raiz);

                        }
                    }
                }
            }
        }
    }

    public org.jdom.Element getEntity(Class clase) {

        org.jdom.Element entidad = new org.jdom.Element("entity");

        org.jdom.Attribute tipoEntidad = new org.jdom.Attribute("type", clase.getName());

        entidad.setAttribute(tipoEntidad);

        entidad.addContent("\n \t");

        Field[] fields = clase.getDeclaredFields();

        for (Field field : fields) {

            org.jdom.Element prop = new org.jdom.Element("property");

            org.jdom.Attribute atr = new org.jdom.Attribute("name", field.getName());

            org.jdom.Attribute atrSeccion = new org.jdom.Attribute("section", clase.getSimpleName());

            ArrayList<org.jdom.Attribute> listaAtributosProperty = new ArrayList<org.jdom.Attribute>();

            if (!field.getType().isPrimitive() && verificarDato(field.getType()) == false) {

                org.jdom.Attribute atr2 = new org.jdom.Attribute("section", clase.getSimpleName());

                org.jdom.Attribute atr3 = new org.jdom.Attribute("type", field.getType().getName());

                listaAtributosProperty.add(atr);

                listaAtributosProperty.add(atr2);

                listaAtributosProperty.add(atr3);

                prop.setAttributes(listaAtributosProperty);

                entidad.addContent("\n \t");

            } else {

                listaAtributosProperty.add(atr);

                listaAtributosProperty.add(atrSeccion);

                prop.setAttributes(listaAtributosProperty);

                entidad.addContent("\n \t");

            }

            entidad.addContent(prop);

            entidad.addContent("\n");
        }

        return entidad;
    }

    public Object getInstance(Class clase) throws InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, JDOMException, IOException {

        org.jdom.Element raiz = new org.jdom.Element("inspection-result");

        raiz.addContent("\n");

        org.jdom.Element entidad = getEntity(clase);

        raiz.addContent(entidad);

        Object claseInstancia = clase.newInstance();

        Field[] campos = clase.getDeclaredFields();

        for (Field field : campos) {

            boolean flag = false;

            if (!field.getType().isPrimitive() && verificarDato(field.getType()) == false) {

                Method[] metodosClase = clase.getDeclaredMethods();

                for (Method method : metodosClase) {

                    if (method.getParameterTypes().length == 1
                            && method.getParameterTypes()[0].getName().equals(field.getType().getName())
                            && (method.getReturnType().getName() == null ? "void" == null : method.getReturnType().getName().equals("void"))
                            && flag == false) {

                        Object campoInstance = field.getType().newInstance();

                        clase.getMethod(method.getName(), field.getType()).invoke(claseInstancia, campoInstance);

                        flag = true;

                        deepInstantiate(campoInstance, raiz);
                    }
                }
            }
        }

        docXml = new org.jdom.Document(raiz);

        crearMetawidgetMetadata(docXml);

        return claseInstancia;

    }

     public void crearMetawidgetMetadata(org.jdom.Document docXml) throws JDOMException, IOException {

        try {

            org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();

            FileOutputStream file = new FileOutputStream(inicio.getDirectorioCasoPrueba().getPath() + "/" + "metawidgetData.xml");

            out.output(docXml, file);

            file.flush();

            file.close();

            out.output(docXml, System.out);

        } catch (Exception e) {
        }
    }

    public void crearXML(Collection coleccion, String casoPrueba) {
        try {
            coleccionId++;
            File casoPruebaFile = new File(System.getProperty("user.home")
                    + System.getProperty("file.separator") + casoPrueba
                    + System.getProperty("file.separator"));

            File metadata = new File(casoPruebaFile.getPath()
                    + System.getProperty("file.separator") + "metadata"
                    + System.getProperty("file.separator"));

            FileOutputStream fos;

            fos = new FileOutputStream(metadata.getPath()
                    + System.getProperty("file.separator")
                    + "coleccion" + coleccionId + ".xml");

            XStream xstream = new XStream(new DomDriver());
            
          //  xstream.alias("" + instanceClass.getClass().getSimpleName().toLowerCase() + "", instanceClass.getClass());
            xstream.toXML(coleccion, fos);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(InstanceForm.class.getName()).log(Level.SEVERE, null, ex);
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
