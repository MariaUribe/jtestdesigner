/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * InstanceMapForm.java
 *
 * Created on Jan 10, 2011, 3:47:28 PM
 */

package com.teg.vista;

import com.teg.logica.WidgetObjectLoading;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;

import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdom.*;
import org.jdom.JDOMException;
import org.jdom.output.XMLOutputter;
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
 * @author danielbello
 */
public class InstanceMapForm extends javax.swing.JFrame {

    private ArrayList<Class> instanceInspect;
    private String path;
    private WidgetObjectLoading listWidget = new WidgetObjectLoading();
    private SwingMetawidget metawidget;
    private SwingMetawidget secondMetawidget;

    private Map mapa;
    private int caso = 0;;
    
    private javax.swing.JTabbedPane tabPanel;
    private javax.swing.JPanel panelKey;
    private javax.swing.JPanel panelValue;
    private org.jdom.Document docXml;
    private Class clase;
    private Object instanceClass;
    private javax.swing.JButton buttonCancelar;
    private javax.swing.JButton buttonGuardar;
    private javax.swing.JButton buttonCrearOtro;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JTextField keyField;
    private javax.swing.JTextField valueField;
    private javax.swing.JPanel keyContainer;
    private boolean dobleInstance = false;
    

    /** Creates new form InstanceMapForm */
    public InstanceMapForm() {
        initComponents();
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
                || clase.isPrimitive() == true){
            verificado = true;
        }

        return verificado;
    }




    public InstanceMapForm(ArrayList<Class> classInstances, String path, WidgetObjectLoading listWidget, Class argument) throws InstantiationException
    {

        obtenerMapa(argument);
        initComponentesObject();
        instanceInspect = classInstances;
         if (verificarDato(instanceInspect.get(0)) == true &&
         verificarDato(instanceInspect.get(1))== true)
        {
             caso = 1;
           addKeyField();
           addValueField();
        }
        else{
            if (verificarDato(instanceInspect.get(0)) == false &&
                verificarDato(instanceInspect.get(1))== true)
            {
                try {
                    caso = 2;
                    Object object = getInstance(instanceInspect.get(0));
                    InspectObject(object, panelKey);
                    addValueField();


                } catch (IllegalAccessException ex) {
                    Logger.getLogger(InstanceMapForm.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchMethodException ex) {
                    Logger.getLogger(InstanceMapForm.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(InstanceMapForm.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(InstanceMapForm.class.getName()).log(Level.SEVERE, null, ex);
                } catch (JDOMException ex) {
                    Logger.getLogger(InstanceMapForm.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(InstanceMapForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
                if (verificarDato(instanceInspect.get(0)) == false &&
                verificarDato(instanceInspect.get(1))== false)
                {
                    if (instanceInspect.get(0).getName().equals(instanceInspect.get(1).getName()))
                    {
                    try {
                        caso = 3;
                        Object object = getInstance(instanceInspect.get(1));

                        InspectObject(object, panelKey);
                        InspectSecondObject(object, panelValue);
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(InstanceMapForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NoSuchMethodException ex) {
                        Logger.getLogger(InstanceMapForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(InstanceMapForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(InstanceMapForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (JDOMException ex) {
                        Logger.getLogger(InstanceMapForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(InstanceMapForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }
                    else
                    {
                    try {
                        caso = 4;
                        ArrayList<Object> instancias = getDobleInstance(instanceInspect.get(0), instanceInspect.get(1));
                        InspectObject(instancias.get(0), panelKey);
                        InspectSecondObject(instancias.get(1), panelValue);
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(InstanceMapForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NoSuchMethodException ex) {
                        Logger.getLogger(InstanceMapForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(InstanceMapForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(InstanceMapForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (JDOMException ex) {
                        Logger.getLogger(InstanceMapForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(InstanceMapForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }

                }
                else
                {
                if (verificarDato(instanceInspect.get(0)) == true &&
                        verificarDato(instanceInspect.get(1)) == false){
                    try {
                        caso = 5;
                        instanceClass = getInstance(instanceInspect.get(1));
                        InspectObject(instanceClass, panelValue);
                        addKeyField();
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(InstanceMapForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NoSuchMethodException ex) {
                        Logger.getLogger(InstanceMapForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(InstanceMapForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(InstanceMapForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (JDOMException ex) {
                        Logger.getLogger(InstanceMapForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(InstanceMapForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                }
        }

    }

    private void addKeyField(){

        keyField = new javax.swing.JTextField();
        keyField.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        keyField.setSize(new Dimension(100,20));
        keyField.setLocation(50,50);
        panelKey.add(keyField);

    }

    private void addValueField(){

        valueField = new javax.swing.JTextField();
        valueField.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        valueField.setSize(new Dimension(100,20));
        valueField.setLocation(50,50);
        panelValue.add(keyField);
        
    }


    private void initComponentesObject()
    {
        tabPanel = new javax.swing.JTabbedPane();
        panelKey = new javax.swing.JPanel();
        panelValue = new javax.swing.JPanel();

         buttonPanel = new javax.swing.JPanel();
        buttonCancelar = new javax.swing.JButton();
        buttonGuardar = new javax.swing.JButton();
        buttonCrearOtro = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);

        buttonPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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

        buttonCrearOtro.setText("Crear");
        buttonCrearOtro.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCrearOtroActionPerformed(evt);
            }
        });

        setLayout(new BorderLayout());

        tabPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        tabPanel.addTab("Clave", panelKey);
        tabPanel.addTab("Valor",panelValue);

        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(buttonGuardar);
        buttonPanel.add(buttonCancelar);
        buttonPanel.add(buttonCrearOtro);

        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        getContentPane().add(tabPanel, BorderLayout.CENTER);

        setTitle("Editor de Mapas");
        setSize(500, 500);


    }

    private void buttonGuardarActionPerformed(java.awt.event.ActionEvent evt) {

        if (caso == 1) {

            mapa.put(keyField.getText(), valueField.getText());

            keyField.setText("");

            valueField.setText("");

        } else {

            if (caso == 2) {

                mapa.put(metawidget.getToInspect(), valueField.getText());

                panelKey.removeAll();

                Object object = getNuevoObjeto(instanceInspect.get(0));

                valueField.setText("");

                this.repaint();

                InspectObject(object, panelKey);

            } else {

                if (caso == 3) {

                    mapa.put(metawidget.getToInspect(), secondMetawidget.getToInspect());

                    panelValue.removeAll();

                    panelKey.removeAll();

                    Object firstObj = getNuevoObjeto(instanceInspect.get(0));

                    Object secondObj = getNuevoObjeto(instanceInspect.get(1));

                    InspectObject(firstObj, panelKey);

                    InspectObject(secondObj, panelValue);

                } else {

                    if (caso == 4) {

                        mapa.put(metawidget.getToInspect(), secondMetawidget.getToInspect());

                        panelValue.removeAll();

                        panelKey.removeAll();

                        Object firstObj = getNuevoObjeto(instanceInspect.get(0));

                        Object secondObj = getNuevoObjeto(instanceInspect.get(1));

                        InspectObject(firstObj, panelKey);

                        InspectObject(secondObj, panelValue);

                    } else {

                        if (caso == 5) {

                            mapa.put(keyField.getText(), metawidget.getToInspect());

                            panelValue.removeAll();

                            Object object = getNuevoObjeto(instanceInspect.get(1));

                            keyField.setText("");

                            this.repaint();

                            InspectObject(object, panelValue);

                        }
                    }
                }
            }
        }

        listWidget.setMapa(mapa);

    }

    private void buttonCrearOtroActionPerformed(java.awt.event.ActionEvent evt) {


        if (caso == 1) {

            mapa.put(keyField.getText(), valueField.getText());

            keyField.setText("");

            valueField.setText("");

        } else {

            if (caso == 2) {

                mapa.put(metawidget.getToInspect(), valueField.getText());

                panelKey.removeAll();

                Object object = getNuevoObjeto(instanceInspect.get(0));

                valueField.setText("");

                this.repaint();

                InspectObject(object, panelKey);

            } else {

                if (caso == 3) {

                    mapa.put(metawidget.getToInspect(), secondMetawidget.getToInspect());

                    panelValue.removeAll();

                    panelKey.removeAll();

                    Object firstObj = getNuevoObjeto(instanceInspect.get(0));

                    Object secondObj = getNuevoObjeto(instanceInspect.get(1));

                    InspectObject(firstObj, panelKey);

                    InspectObject(secondObj, panelValue);

                } else {

                    if (caso == 4) {

                        mapa.put(metawidget.getToInspect(), secondMetawidget.getToInspect());

                        panelValue.removeAll();

                        panelKey.removeAll();

                        Object firstObj = getNuevoObjeto(instanceInspect.get(0));

                        Object secondObj = getNuevoObjeto(instanceInspect.get(1));

                        InspectObject(firstObj, panelKey);

                        InspectObject(secondObj, panelValue);

                    } else {

                        if (caso == 5) {

                            mapa.put(keyField.getText(), metawidget.getToInspect());

                            panelValue.removeAll();

                            Object object = getNuevoObjeto(instanceInspect.get(1));

                            keyField.setText("");

                            this.repaint();

                            InspectObject(object, panelValue);

                        }
                    }
                }
            }
        }

    }

     private void buttonCancelarActionPerformed(java.awt.event.ActionEvent evt){

         this.dispose();
     }

     public void crearMetawidgetMetadata(org.jdom.Document doc) throws JDOMException, IOException {

        try {
            XMLOutputter out = new XMLOutputter();
            FileOutputStream file = new FileOutputStream(path + "/" + "metawidgetData.xml");
            out.output(doc, file);
            file.flush();
            file.close();
            out.output(doc, System.out);
        } catch (Exception e) {
        }
    }

    public void deepInstantiate(Object claseInstancia, Element raiz) throws InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {

        raiz.addContent("\n");
        Element entidad = getEntity(claseInstancia.getClass());
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

    public Element getEntity(Class clase) {
        Element entidad = new Element("entity");
        Attribute tipoEntidad = new Attribute("type", clase.getName());

        entidad.setAttribute(tipoEntidad);
        entidad.addContent("\n \t");
        Field[] fields = clase.getDeclaredFields();
        for (Field field : fields) {
            Element prop = new Element("property");

            Attribute atr = new Attribute("name", field.getName());
            Attribute atrSeccion = new Attribute("section", clase.getSimpleName());
            ArrayList<Attribute> listaAtributosProperty = new ArrayList<Attribute>();

            if (verificarDato(field.getType()) == false) {

                Attribute atr2 = new Attribute("section", clase.getSimpleName());
                Attribute atr3 = new Attribute("type", field.getType().getName());

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



    private Object getInstance(Class clase) throws InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, JDOMException, IOException {


        Element raiz = new Element("inspection-result");
        raiz.addContent("\n");
        Element entidad = getEntity(clase);
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

    private void instanciaCampos(Object claseInstancia) throws InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {


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
                            instanciaCampos(campoInstance);

                        }
                    }
                }
            }
        }
    }

    private Object instanciarNuevoObjeto(Class clase) throws
            InstantiationException, IllegalAccessException,
            NoSuchMethodException, IllegalArgumentException,
            InvocationTargetException, JDOMException, IOException {



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
                        instanciaCampos(campoInstance);
                    }
                }
            }
        }


        return claseInstancia;
    }

    private Object getNuevoObjeto(Class clase)
    {


        Object nuevoObjeto = null;
        try {
            nuevoObjeto = instanciarNuevoObjeto(clase);
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

    private ArrayList<Object> getDobleInstance(Class firstClass, Class secondClass) throws InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, JDOMException, IOException{
        Element raiz = new Element("inspection-result");
        raiz.addContent("\n");
        Element entidad = getEntity(firstClass);
        raiz.addContent(entidad);
        raiz.addContent("\n");
        Element secondEntidad = getEntity(secondClass);
        raiz.addContent(secondEntidad);

        Object firstInstancia = firstClass.newInstance();

        Object secondInstancia = secondClass.newInstance();

        Field[] campos = firstClass.getDeclaredFields();
        for (Field field : campos) {
            boolean flag = false;
            if (verificarDato(field.getType()) == false) {
                Method[] metodosClase = firstClass.getDeclaredMethods();
                for (Method method : metodosClase) {

                    if (method.getParameterTypes().length == 1
                            && method.getParameterTypes()[0].getName().equals(field.getType().getName())
                            && (method.getReturnType().getName() == null ? "void" == null : method.getReturnType().getName().equals("void"))
                            && flag == false) {
                        Object campoInstance = field.getType().newInstance();
                        firstClass.getMethod(method.getName(), field.getType()).invoke(firstInstancia, campoInstance);
                        flag = true;
                        deepInstantiate(campoInstance, raiz);
                    }
                }
            }
        }

        Field[] secondCampos = secondClass.getDeclaredFields();

        for (Field field : secondCampos) {
            boolean flag = false;
            if (!field.getType().isPrimitive() && !field.getType().getName().equals("java.lang.String")
                    && !field.getType().getName().equals("java.lang.Integer")) {
                Method[] metodosClase = secondClass.getDeclaredMethods();
                for (Method method : metodosClase) {

                    if (method.getParameterTypes().length == 1
                            && method.getParameterTypes()[0].getName().equals(field.getType().getName())
                            && (method.getReturnType().getName() == null ? "void" == null : method.getReturnType().getName().equals("void"))
                            && flag == false) {
                        Object campoInstance = field.getType().newInstance();
                        secondClass.getMethod(method.getName(), field.getType()).invoke(secondInstancia, campoInstance);
                        flag = true;
                        deepInstantiate(campoInstance, raiz);
                    }
                }
            }
        }

         docXml = new org.jdom.Document(raiz);

        crearMetawidgetMetadata(docXml);

        ArrayList<Object> instancias = new ArrayList<Object>();
        instancias.add(firstInstancia);
        instancias.add(secondInstancia);

        return instancias;



    }

    private void InspectSecondObject(Object instance, javax.swing.JPanel panel){
        secondMetawidget = new SwingMetawidget();

        secondMetawidget.addWidgetProcessor(new BeansBindingProcessor(
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

        secondMetawidget.setMetawidgetLayout(new TabbedPaneLayoutDecorator(layoutConfig));

        secondMetawidget.setInspector(new CompositeInspector(inspectorConfig));

        secondMetawidget.setToInspect(instance);





        panel.add(secondMetawidget);
        panel.validate();
    }

    private void InspectObject(Object instance, javax.swing.JPanel panel) {

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

        metawidget.setToInspect(instance);

        

        

        panel.add(metawidget);
        panel.validate();

        //this.repaint();


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

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
    * @param args the command line arguments
    */
    public void Visible(){

        if (dobleInstance == true){

        }
    }

    private void obtenerMapa(Class argument) {
        if (argument.getName().equals("java.util.HashMap")){
            mapa = new HashMap();
        }
        else{
            if (argument.getName().equals("java.util.Hashtable")){
                mapa = new Hashtable();
            }
            else{
                if (argument.getName().equals("java.util.LinkedHashMap")){
                    mapa = new LinkedHashMap();
                }
                else{
                    if (argument.getName().equals("java.util.TreeMap")){
                        mapa = new TreeMap();
                    }
                    else{
                        if (argument.getName().equals("java.util.WeakHashMap")){
                            mapa = new WeakHashMap();
                        }
                    }

                }
            }
        }


    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
