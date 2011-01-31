/*
 * InstanceForm.java
 *
 * Created on Dec 6, 2010, 11:59:28 PM
 */
package com.teg.vista;

import com.teg.logica.ClassLoading;
import com.teg.logica.WidgetObjectLoading;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * @author danielbello
 */
public class InstanceForm extends javax.swing.JFrame {

    private Object instanceInspect;
    private String path;
    private WidgetObjectLoading listWidget = new WidgetObjectLoading();
    private SwingMetawidget metawidget = new SwingMetawidget();
    private javax.swing.JButton buttonCancelar;
    private javax.swing.JButton buttonGuardar;
    private javax.swing.JButton buttonCrearOtro;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JPanel objectContainer;
    private Method metodoActual;
    private Inicio inicio;
    private String casoPrueba;
    private Class clase;
    private ArrayList<Class> clasesJar;
    private ClassLoading classLoader = new ClassLoading();
    private int objId;

    /** Creates new form InstanceForm */
    public InstanceForm(Object instance, String dataPath, WidgetObjectLoading listObject, Method metodo, Inicio inicio, int objId) {
        listWidget = listObject;

        instanceInspect = instance;

        path = dataPath;

        metodoActual = metodo;

        this.inicio = inicio;

        this.casoPrueba = inicio.getNombreCasoPrueba();

        this.objId = objId;

        initComponents2();

    }

    public InstanceForm() {
        initComponents();
    }
    public InstanceForm(ArrayList<Class> clasesJars, String path, WidgetObjectLoading listObject, Inicio inicio, int objId){

       

        this.path = path;

        this.listWidget = listObject;

        this.inicio = inicio;

        this.objId = objId;

       

       // initComponentsInterface();

    }

    

    public void InspectObject(Object instance) {
        // asociamor al metawidget la instancia que va a manejar el "binding" de propiedades
        metawidget.addWidgetProcessor(new BeansBindingProcessor(
                new BeansBindingProcessorConfig().setUpdateStrategy(UpdateStrategy.READ_WRITE)));

        CompositeInspectorConfig inspectorConfig = null;

        try {
            XmlInspectorConfig xmlConfig = new XmlInspectorConfig();

            File file = new File(path + "/" + "metawidgetData.xml");
           // System.out.println(file.exists());

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

    private void initComponents2() {

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

        buttonCrearOtro.setText("Crear");
        buttonCrearOtro.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCrearOtroActionPerformed(evt);
            }
        });

        setLayout(new BorderLayout());

        objectContainer.setLayout(new FlowLayout(FlowLayout.CENTER));

        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(buttonGuardar);
        buttonPanel.add(buttonCancelar);
        buttonPanel.add(buttonCrearOtro);


        getContentPane().add(objectContainer, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setTitle("Editor de Objetos Genericos");
        setSize(500, 500);
    }

    public void getObject() {
        ArrayList<Object> objects = new ArrayList<Object>();
        objects.add(metawidget.getToInspect());
        listWidget.setObject(objects);

    }

    private void buttonCrearOtroActionPerformed(java.awt.event.ActionEvent evt) {
        objectContainer.removeAll();
        metawidget.removeAll();
        this.repaint();
        Label label = new Label();
        label.setText("olaa");
        label.setSize(new Dimension(50, 50));
        label.setLocation(50, 50);
        objectContainer.add(label);
        //InspectObject(instanceInspect);
    }

    private void buttonGuardarActionPerformed(java.awt.event.ActionEvent evt) {

        listWidget.setGuardado(true);

        Object instance = metawidget.getToInspect();

        Class claseJar = this.loadClass(instance.getClass());

        this.crearXML(claseJar, instance, casoPrueba);

        this.dispose();
    }

    public void crearXML(Class claseJar, Object instance, String casoPrueba) {
        objId++;
        
        try {
            File casoPruebaFile = new File(System.getProperty("user.home")
                    + System.getProperty("file.separator") + casoPrueba
                    + System.getProperty("file.separator"));

            File metadata = new File(casoPruebaFile.getPath()
                    + System.getProperty("file.separator") + "metadata"
                    + System.getProperty("file.separator"));

            FileOutputStream fos = new FileOutputStream(metadata.getPath()
                    + System.getProperty("file.separator") + "object" + objId + ".xml");

            XStream xstream = new XStream(new DomDriver());

           // xstream.alias("" + claseJar.getName() + "", instance.getClass());
            xstream.toXML(instance, fos);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(InstanceForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Class loadClass(Class miClase) {
        Class claseJar = null;

        for (File jar : inicio.getJarsRuta()) {
            try {
                claseJar = classLoader.getClassDetail(jar.getPath(), miClase.getName());
            } catch (MalformedURLException ex) {
                Logger.getLogger(InstanceForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(InstanceForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return claseJar;
    }

    private void buttonCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    public void Visible() {

        Type[] genericParameterTypes = metodoActual.getGenericParameterTypes();

        for (Type genericParameterType : genericParameterTypes) {
            if (genericParameterType instanceof ParameterizedType) {
                ParameterizedType aType = (ParameterizedType) genericParameterType;
                Type[] parameterArgTypes = aType.getActualTypeArguments();
                for (Type parameterArgType : parameterArgTypes) {
                    Class parameterArgClass = (Class) parameterArgType;
                    //System.out.println("parameterArgClass = " + parameterArgClass);
                }
            }
        }
        InspectObject(instanceInspect);
        this.setVisible(true);
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
        setTitle("Instance Editor");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 588, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 385, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
