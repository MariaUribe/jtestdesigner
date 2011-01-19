/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * InstanceArrayForm.java
 *
 * Created on Jan 11, 2011, 3:24:34 PM
 */

package com.teg.vista;

import com.teg.logica.WidgetObjectLoading;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
 * @author danielbello
 */
public class InstanceArrayForm extends javax.swing.JFrame {

     private javax.swing.JButton buttonCancelar;
    private javax.swing.JButton buttonGuardar;
    private javax.swing.JButton buttonCrearOtro;
    private javax.swing.JPanel buttonPanel;
    private SwingMetawidget metawidget;
    private WidgetObjectLoading widget;
   private ArrayList<Object> listaObjetos = new ArrayList<Object>();
    private javax.swing.JTextField valorString;
    
    private javax.swing.JPanel objectContainer;
    private javax.swing.JLabel labelString;
    private Class claseComponente;
    private Inicio inicio;
    private String pathFile;
    

    /** Creates new form InstanceArrayForm */
    public InstanceArrayForm() {
        initComponents();
    }

    InstanceArrayForm(Class arrayComponente, String path, WidgetObjectLoading listWidget, Inicio ini) {

        pathFile = path;

        metawidget = new SwingMetawidget();

        inicio = ini;

        widget = listWidget;
        
        claseComponente = arrayComponente;
        
           
            initComponentsText();
       
            
           
           
            
            
        
    }

    InstanceArrayForm(Object object, String path, WidgetObjectLoading listWidget, Inicio inicio) {

        widget = listWidget;
        initComponentsObject();
    }

    

    private void initComponentsObject()
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

        buttonCrearOtro.setText("Crear");
        buttonCrearOtro.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCrearOtroActionPerformed(evt);
            }
        });

        setLayout(new BorderLayout());

        objectContainer.setLayout(new BorderLayout());
        

        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(buttonGuardar);
        buttonPanel.add(buttonCancelar);
        buttonPanel.add(buttonCrearOtro);


        getContentPane().add(objectContainer, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setTitle("Editor de Objetos Genericos");
        setSize(500, 500);

    }

    private void initComponentsText(){

        objectContainer = new javax.swing.JPanel();
        buttonPanel = new javax.swing.JPanel();
        buttonCancelar = new javax.swing.JButton();
        buttonGuardar = new javax.swing.JButton();
        buttonCrearOtro = new javax.swing.JButton();
        valorString = new javax.swing.JTextField();
        labelString = new javax.swing.JLabel();
        valorString.setSize(new Dimension(100,20));
        valorString.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        valorString.setLocation(50, 50);
        labelString.setSize(new Dimension(20,20));
        labelString.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        labelString.setText("Este campo es de valor tipo: "+ claseComponente.getName());
        labelString.setLocation(70, 70);


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

        buttonCrearOtro.setText("Crear");
        buttonCrearOtro.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCrearOtroStringActionPerformed(evt);
            }
        });

        setLayout(new BorderLayout());

        objectContainer.setLayout(new BorderLayout());
        objectContainer.add(valorString);
        objectContainer.add(labelString);

        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(buttonGuardar);
        buttonPanel.add(buttonCancelar);
        buttonPanel.add(buttonCrearOtro);


        getContentPane().add(objectContainer, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setTitle("Editor de Objetos Genericos");
        setSize(500, 500);

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
                || clase.getName().equals("java.lang.Boolean")) {
            verificado = true;
        }

        return verificado;
    }

    private void InspectObject(Object instance) {

        metawidget = new SwingMetawidget();

        // asociamor al metawidget la instancia que va a manejar el "binding" de propiedades
        metawidget.addWidgetProcessor(new BeansBindingProcessor(
                new BeansBindingProcessorConfig().setUpdateStrategy(UpdateStrategy.READ_WRITE)));

        CompositeInspectorConfig inspectorConfig = null;

        try {
            XmlInspectorConfig xmlConfig = new XmlInspectorConfig();

            File file = new File(pathFile + "/" + "metawidgetData.xml");


            xmlConfig.setInputStream(new FileInputStream(new File(pathFile + "/" + "metawidgetData.xml")));
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

    private Object getNuevoObjeto()
    {


        Object nuevoObjeto = null;
        try {
            nuevoObjeto = instanciarNuevoObjeto(claseComponente);
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

    private void buttonGuardarActionPerformed(java.awt.event.ActionEvent evt) {
        listaObjetos.add(metawidget.getToInspect());
        widget.setArreglo(listaObjetos.toArray());
        this.dispose();

     }

    private void buttonGuardarStringActionPerformed(java.awt.event.ActionEvent evt){
        if (claseComponente.getName().equals("java.lang.Integer")){
            Integer integer = new Integer(Integer.parseInt(valorString.getText()));
        }else{
            if (claseComponente.getName().equals("java.lang.Float")){
                Float floatable = new Float(Float.parseFloat(valorString.getText()));
            }else{
                if (claseComponente.getName().equals("java.lang.Character")){
                    Character character = new Character(valorString.getText().toCharArray()[0]);
                }else{
                    if (claseComponente.getName().equals("java.lang.Byte")){
                        Byte byter = new Byte(Byte.parseByte(valorString.getText()));
                    }else{
                        if (claseComponente.getName().equals("java.lang.Double")){
                            Double doubler = new Double(Double.parseDouble(valorString.getText()));
                        }else{
                            if (claseComponente.getName().equals("java.lang.Boolean")){
                                Boolean booleano = Boolean.parseBoolean(valorString.getText());
                            }
                        }
                    }
                }
            }

        }

    }



     private void buttonCrearOtroActionPerformed(java.awt.event.ActionEvent evt){

         listaObjetos.add(metawidget.getToInspect());
         objectContainer.removeAll();
         Object object = getNuevoObjeto();
         this.repaint();
         InspectObject(object);
     }

     private void buttonCrearOtroStringActionPerformed(java.awt.event.ActionEvent evt){

         listaObjetos.add(labelString.getText());
         labelString.setText("");
     }

     private void buttonCancelarActionPerformed(java.awt.event.ActionEvent evt){
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
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
