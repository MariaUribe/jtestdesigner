/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * InstanceForm.java
 *
 * Created on Dec 6, 2010, 11:59:28 PM
 */

package com.teg.vista;

import com.teg.logica.WidgetObjectLoading;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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

    private javax.swing.JPanel buttonPanel;

    private javax.swing.JPanel objectContainer;


    /** Creates new form InstanceForm */
    public InstanceForm(Object instance, String dataPath,WidgetObjectLoading listObject) {
        listWidget = listObject;
        instanceInspect = instance;
        path = dataPath;
        initComponents2();
      
        
        
    }

    public InstanceForm()
    {
        initComponents();
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

    private void initComponents2() {

        objectContainer = new javax.swing.JPanel();
        buttonPanel = new javax.swing.JPanel();
        buttonCancelar = new javax.swing.JButton();
        buttonGuardar = new javax.swing.JButton();

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

        setLayout(new BorderLayout());

        objectContainer.setLayout(new BorderLayout());

        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(buttonGuardar);
        buttonPanel.add(buttonCancelar);


        getContentPane().add(objectContainer, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setTitle("Editor de Objetos Genericos");
        setSize(500, 500);
    }

    public void  getObject()
    {
        ArrayList<Object> objects = new ArrayList<Object>();
        objects.add(metawidget.getToInspect());
        listWidget.setObject(objects);

    }

    private void buttonGuardarActionPerformed(java.awt.event.ActionEvent evt) {

        Object instance = metawidget.getToInspect();
       

        // y aqui en vez de imprimir por consola podrian serializar el
        // objeto con Xstream para utilizarlo como datos de entrada
        // en la prueba
        System.out.println(instance.toString());
        this.dispose();


       
    }

    private void buttonCancelarActionPerformed(java.awt.event.ActionEvent evt) {
    }

    public void Visible()
    {
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
        setSize(new java.awt.Dimension(500, 500));

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
