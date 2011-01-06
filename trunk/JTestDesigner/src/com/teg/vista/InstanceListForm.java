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

import com.teg.logica.WidgetObjectLoading;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.JTextField;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
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

    private ArrayList<Object> instanceInspect;


    private String path;

    private WidgetObjectLoading listWidget = new WidgetObjectLoading();

    private SwingMetawidget metawidget = new SwingMetawidget();

    private Collection coleccion;

    private Map mapa;

    private Class clase;

    private Object instanceClass;

    private javax.swing.JButton buttonCancelar;

    private javax.swing.JButton buttonGuardar;

    private javax.swing.JButton buttonCrearOtro;

    private javax.swing.JPanel buttonPanel;

    private javax.swing.JPanel objectContainer;

    private javax.swing.JPanel keyContainer;

    private javax.swing.JTextField keyField;

    /** Creates new form InstanceListForm */
    public InstanceListForm() {
        initComponents();
    }

    public InstanceListForm(ArrayList<Object> instance, String dataPath,WidgetObjectLoading listObject, Class argumento)
    {
        instanceInspect = instance;

        path = dataPath;

        listWidget = listObject;

        clase = argumento;

        String argumentoClase = argumentoColeccion(clase);

        if (argumentoClase.equals("java.util.List"))
        {
            obtenerListaColeccion(clase);
            instanceClass = instance.get(0);
        }
        else
        {
            if (argumentoClase.equals("ava.util.Set"))
            {
                obtenerSetColeccion(clase);
                instanceClass = instance.get(0);
            }
            else
            {
                if (argumentoClase.equals("java.util.Queue"))
                {
                    obtenerQueueColeccion(clase);
                    instanceClass = instance.get(0);
                }
                else
                {
                    if (argumentoClase.equals("java.util.Map"))
                    {
                        obtenerMapaColeccion(clase);
                        instanceClass = instance.get(1);
                        initComponentsMap();

                    }
                }
            }
        }

       
       

    }

    public void InspectObject(Object instance)
    {
       // asociamor al metawidget la instancia que va a manejar el "binding" de propiedades
        metawidget.addWidgetProcessor(new BeansBindingProcessor(
                new BeansBindingProcessorConfig().setUpdateStrategy(UpdateStrategy.READ_WRITE)));

        CompositeInspectorConfig inspectorConfig = null;

        try {
            XmlInspectorConfig xmlConfig = new XmlInspectorConfig();

            File file = new File(path + "/" + "metawidgetData.xml");
            System.out.println(file.exists());

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

        metawidget.setToInspect(instance);

        objectContainer.add(metawidget);
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
    public void visibles()
    {

        InspectObject(instanceInspect);
        this.setVisible(true);

    }



    private String argumentoColeccion(Class clase)
    {
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

    private void obtenerListaColeccion(Class argumento)
    {

        if (argumento.getName().equals("java.util.ArrayList"))
        {
            coleccion = new ArrayList();
        }
        else
        {
            if (argumento.getName().equals("java.util.LinkedList"))
            {
                coleccion = new LinkedList();
            }
        }



    }

    private void obtenerSetColeccion(Class argumento)
    {

         if (argumento.getName().equals("java.util.HashSet"))
                {
                    coleccion = new HashSet();
                }
                else
                {
                    if (argumento.getName().equals("java.util.TreeSet"))
                    {
                        coleccion = new TreeSet();
                    }
                    else
                    {
                        if (argumento.getName().equals("java.util.LinkedHashSet"))
                        {
                            coleccion = new LinkedHashSet();
                        }
                    }
                }

    }

    private void obtenerQueueColeccion(Class argumento)
    {

    }

    private void obtenerMapaColeccion(Class argumento)
    {
        if (argumento.getName().equals("java.util.HashMap"))
        {

            mapa = new HashMap();

        }
        else
        {
            if (argumento.getName().equals("java.util.TreeMap"))
            {
                mapa = new TreeMap();
            }
            else {
                if (argumento.getName().equals("java.util.LinkedHashMap"))
                {
                    mapa = new LinkedHashMap();
                }
            }
        }
    }

    public ArrayList getObjectList()
    {
        coleccion = new ArrayList();
        return null;
    }

    


    private void initComponentsMap() {

        objectContainer = new javax.swing.JPanel();
        buttonPanel = new javax.swing.JPanel();
        keyContainer = new javax.swing.JPanel();
        buttonCancelar = new javax.swing.JButton();
        buttonGuardar = new javax.swing.JButton();
        buttonCrearOtro = new javax.swing.JButton();
        keyField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);

        buttonPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        objectContainer.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        buttonCancelar.setText("Cancelar");
        buttonCancelar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelarActionPerformed(evt);
            }
        });

        buttonGuardar.setText("Guardar");
        buttonGuardar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonGuardarMapaActionPerformed(evt);
            }
        });

        buttonCrearOtro.setText("Crear otro Objeto");
        buttonCrearOtro.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCrearOtroMapaActionPerformed(evt);
            }
        });

        setLayout(new BorderLayout());

        objectContainer.setLayout(new BorderLayout());

        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        keyContainer.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(buttonGuardar);
        buttonPanel.add(buttonCancelar);
        buttonPanel.add(buttonCrearOtro);
        keyContainer.add(keyField);

        getContentPane().add(keyContainer, BorderLayout.NORTH);
        getContentPane().add(objectContainer, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setTitle("Editor de Objetos Genericos");
        setSize(500, 500);
        
    }

    private void initComponentsCollection()
    {
        objectContainer = new javax.swing.JPanel();
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

        buttonGuardar.setText("Guardar");
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

        objectContainer.setLayout(new BorderLayout());

        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(buttonGuardar);
        buttonPanel.add(buttonCancelar);
        buttonPanel.add(buttonCrearOtro);


        getContentPane().add(objectContainer, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setTitle("Editor de Objetos Genericos");
        setSize(500, 500);
    }

    private void buttonCrearOtroActionPerformed(java.awt.event.ActionEvent evt)
    {
        objectContainer.removeAll();
        InspectObject(instanceInspect);


    }

     private void buttonCrearOtroMapaActionPerformed(java.awt.event.ActionEvent evt)
    {
        objectContainer.removeAll();
        InspectObject(instanceInspect);


    }

    private void buttonGuardarActionPerformed(java.awt.event.ActionEvent evt) {

        Object instanceInspected = metawidget.getToInspect();


        // y aqui en vez de imprimir por consola podrian serializar el
        // objeto con Xstream para utilizarlo como datos de entrada
        // en la prueba

        this.dispose();



    }

     private void buttonGuardarMapaActionPerformed(java.awt.event.ActionEvent evt) {

        Object instanceInspected = metawidget.getToInspect();


        // y aqui en vez de imprimir por consola podrian serializar el
        // objeto con Xstream para utilizarlo como datos de entrada
        // en la prueba

        this.dispose();



    }


    private void buttonCancelarActionPerformed(java.awt.event.ActionEvent evt) {
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
