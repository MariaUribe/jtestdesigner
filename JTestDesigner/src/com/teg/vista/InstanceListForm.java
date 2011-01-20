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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    private ArrayList<Object> instanceInspect;
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
    private javax.swing.JSpinner spinnerField = new javax.swing.JSpinner();

    public InstanceListForm(ArrayList<Object> instance, String dataPath, WidgetObjectLoading listObject, Class argumento) {
        instanceInspect = instance;

        path = dataPath;



        listWidget = listObject;

        clase = argumento;
        

        boolean initCollection = false;

        String argumentoClase = argumentoColeccion(clase);

        if (argumentoClase.equals("java.util.List")) {
            obtenerListaColeccion(clase);
            instanceClass = instance.get(0);
           
        } else {
            if (argumentoClase.equals("ava.util.Set")) {
                obtenerSetColeccion(clase);
                instanceClass = instance.get(0);
               
            } else {
                if (argumentoClase.equals("java.util.Queue")) {
                    obtenerQueueColeccion(clase);
                    instanceClass = instance.get(0);
                
                
                    
                }
            }
        }

            initComponentsCollection();
        
    }

    /** Creates new form InstanceListForm */
    public InstanceListForm() {
        initComponents();
    }

    InstanceListForm(Class clasePrimitiva, WidgetObjectLoading listObject, Class argumento) {

        listWidget = listObject;

        clase = argumento;

        String argumentoClase = argumentoColeccion(clase);

        if (argumentoClase.equals("java.util.List")) {
            obtenerListaColeccion(clase);


        } else {
            if (argumentoClase.equals("ava.util.Set")) {
                obtenerSetColeccion(clase);


            } else {
                if (argumentoClase.equals("java.util.Queue")) {
                    obtenerQueueColeccion(clase);




                }
            }
        }

        initComponentesString();
            
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

        metawidget.setToInspect(instance);

        

        objectContainer.add(metawidget);
        objectContainer.validate();

        //this.repaint();

        
    }

    

    private void initComponentesString() {

         buttonPanel = new javax.swing.JPanel();
         textField = new javax.swing.JTextField();
        buttonCancelar = new javax.swing.JButton();
        buttonGuardar = new javax.swing.JButton();
        buttonCrearOtro = new javax.swing.JButton();
        textField.setSize(new Dimension(100,100));
        
        textField.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        textField.setMaximumSize(new Dimension(100,100));

       // textField.setSize(new Dimension(50, 50));
        //textField.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        //textField.setLocation(150, 150);


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
        textField.setLocation(new Point(20,20));
        textField.setVisible(true);
        
       

        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(buttonGuardar);
        buttonPanel.add(buttonCancelar);
        buttonPanel.add(buttonCrearOtro);


        getContentPane().add(objectContainer, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
         

        setTitle("Editor de Objetos Genericos");
        setSize(500, 500);


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

    private void buttonCrearOtroStringActionPerformed(java.awt.event.ActionEvent evt)  {

        coleccion.add(textField.getText());

        textField.setText("");
    }

     private void buttonCrearOtroActionPerformed(java.awt.event.ActionEvent evt)  {

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

    private void obtenerListaColeccion(Class argumento) {

        if (argumento.getName().equals("java.util.ArrayList")) {
            coleccion = new ArrayList();
        } else {
            if (argumento.getName().equals("java.util.LinkedList")) {
                coleccion = new LinkedList();
            }
        }



    }

    

  

     
     private void buttonCancelarActionPerformed(java.awt.event.ActionEvent evt){

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
                }
            }
        }

    }

    private void obtenerQueueColeccion(Class argumento) {
    }

    /*private void obtenerMapaColeccion(Class argumento) {
        if (argumento.getName().equals("java.util.HashMap")) {

            mapa = new HashMap();

        } else {
            if (argumento.getName().equals("java.util.TreeMap")) {
                mapa = new TreeMap();
            } else {
                if (argumento.getName().equals("java.util.LinkedHashMap")) {
                    mapa = new LinkedHashMap();

                }
            }
        }
    }*/

    public void getColeccion() {

        listWidget.setColeccion(coleccion);
    }

   

    public void instanciaCampos(Object claseInstancia) throws InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {


        Field[] campos = claseInstancia.getClass().getDeclaredFields();
        for (Field field : campos) {
            boolean flag = false;
            if (!field.getType().isPrimitive() &&
                    verificarDato(field.getType()) == false) {
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
            if (!field.getType().isPrimitive() &&
                    verificarDato(field.getType()) == false) {
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

    private Object getNuevoObjeto()
    {
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
      

        this.dispose();

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
