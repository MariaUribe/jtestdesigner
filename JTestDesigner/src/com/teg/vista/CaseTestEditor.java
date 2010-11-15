/*
 * CaseTestEditor.java
 *
 * Created on Oct 28, 2010, 3:24:27 PM
 */
package com.teg.vista;

import com.teg.dominio.Argumento;
import com.teg.dominio.AssertTest;
import com.teg.dominio.Metodo;
import com.teg.dominio.Retorno;
import com.teg.logica.GenericObjectEditor;
import com.teg.logica.GetWidgetValues;
import com.teg.logica.XmlManager;
import com.teg.util.SwingUtils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author maya
 */
public class CaseTestEditor extends javax.swing.JInternalFrame {

    private ArrayList<Method> metodos = new ArrayList<Method>();
    private ArrayList<DefaultCellEditor> editores = new ArrayList<DefaultCellEditor>();
    private ArrayList<Metodo> metodosGuardados = new ArrayList<Metodo>();
    private ArrayList<Metodo> metodosTest = new ArrayList<Metodo>();
    private Integer contMetodos = 0;
    private static int varId = 1;
    private Class tipoVarRetorno;
    private String valorFila;
    private String actualNameMethod;
    private JTable tablaArgumentos;
    private Inicio inicio;

    /** Creates new form CaseTestEditor */
    @SuppressWarnings("LeakingThisInConstructor")
    public CaseTestEditor(ArrayList<Method> metodos, Inicio inicio) {
        initComponents();
        this.metodos = metodos;
        this.inicio = inicio;


        javax.swing.plaf.InternalFrameUI ifu = this.getUI();
        ((javax.swing.plaf.basic.BasicInternalFrameUI) ifu).setNorthPane(null);
    }

    public void cargarMetodos() {
        int panelHeight = this.panel.getHeight();

        for (Method method : metodos) {
            panelHeight = panelHeight + 25;
            addMethodPanel(method.getName(), panelHeight);
        }
    }

    public Method getActualMethod() {
        Method m = null;
        for (Method method : metodos) {
            if (method.getName().equals(actualNameMethod)) {
                m = method;
            }

        }
        return m;
    }

    public void addMethodPanel(String methodName, int panelHeight) {

        List<JTextField> components = SwingUtils.getDescendantsOfType(JTextField.class, this.panel, true);
        JTextField lastTextField = null;
        final JTextField metodoTest;

        if (!components.isEmpty()) {
            lastTextField = components.get(components.size() - 1);
        }

        if (lastTextField != null) {

            Point point = lastTextField.getLocation();
            int pointY = point.y + 40;

            metodoTest = new JTextField();

            metodoTest.setName("metodo:" + methodName);
            metodoTest.setLocation(point.x, pointY);
            metodoTest.setSize(192, 27);
            metodoTest.setText(methodName);
            metodoTest.setVisible(Boolean.TRUE);


            this.panel.add(metodoTest);

            if ((pointY > scroll.getHeight()) || (pointY == scroll.getHeight())) {

                Dimension dmnsn = new Dimension(panel.getWidth(), panelHeight);
                panel.setPreferredSize(dmnsn);
                scroll.setViewportView(panel);
                panel.repaint();
            }
        } else {

            Point point = new Point(12, 12);

            metodoTest = new JTextField();
            metodoTest.setName("metodo:" + methodName);
            metodoTest.setLocation(point);
            metodoTest.setSize(192, 27);
            metodoTest.setText(methodName);
            metodoTest.setVisible(Boolean.TRUE);
            panel.add(metodoTest);
            panel.repaint();
        }

        metodoTest.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (!metodoTest.isEnabled() == false)
                {
                cargarTablaArgumentos(metodoTest.getText());
                actualNameMethod = metodoTest.getText();
                }


            }
        });
    }

    public Method getMethodSelected(String text) {
        Method method = null;

        for (Method method1 : metodos) {
            if (text.equals(method1.getName())) {
                method = method1;
            }
        }
        return method;
    }

    public void cargarAssert(Class metodoRetorno) {
        assertVariables.removeAllItems();
        if (metodoRetorno.isPrimitive()) {

            assertVariables.setSelectedItem("Var" + varId);
            System.out.println("Retorno primitivo");

        } else {
            Field[] fields = metodoRetorno.getDeclaredFields();
            for (Field field : fields) {

                assertVariables.addItem(metodoRetorno.getSimpleName()
                        + "." + field.getName());
            }
            System.out.println("Retorna algo complejo");
            assertVariables.setSelectedItem("Seleccione el campo a evaluar");

        }
        assertVariables.repaint();


    }

    public Method getMetodoGuardado(String nombre) {

        Method metodo = null;
        for (Method method : metodos) {
            if (method.getName().equals(nombre)) {
                metodo = method;
            }
        }

        return metodo;



    }

    public void cargarComboItemsComplex(JComboBox combo, Class argument) {


        Method method;

        for (Metodo metodo : metodosGuardados) {

            method = getMetodoGuardado(metodo.getNombre());

            Class retorno = method.getReturnType();

            if (retorno.getName().equals(argument.getName())) {
                combo.addItem(metodo.getRetorno().getNombreVariable());
            }

        }


    }

    public void cargarComboItemsPrimitive(JComboBox combo, Class parameter)
    {
        Method method;

        for (Metodo metodo : metodosGuardados)
        {

            method = getMetodoGuardado(metodo.getNombre());
            Class retorno = method.getReturnType();

            if (retorno.getDeclaredFields().length != 0)
            {

            for (Field field : retorno.getDeclaredFields()) {

                if (field.getType().getSimpleName().equals(parameter.getName()))
                    combo.addItem(metodo.getRetorno().getNombreVariable()+ "." + field.getName());
            }

            }else
                
            {
                if (retorno.getName().equals(parameter.getName()))
                    combo.addItem(metodo.getRetorno().getNombreVariable());
            }

        }
    }

    /*if (!metodosGuardados.isEmpty())
    {
    for (Metodo metodo : metodosGuardados)
    {
    Method metod = null;
    String metodoRetorno = metodo.getNombre();
    for (Method method : metodos)
    {
    if (metodoRetorno.equals(method.getName()))
    metod = method;
    Class a = metod.getReturnType();


    }

    }
    }*/
    @SuppressWarnings("empty-statement")
    public void cargarTablaArgumentos(String text) {

        for (Component component : panelTablaArgumentos.getComponents())
        {
            if (!component.getClass().getName().equals("javax.swing.JLabel"))
            {
                panelTablaArgumentos.remove(component);
            }

        }
        //panelTablaArgumentos.removeAll();


        editores.clear();
        Method metodo;

        metodo = getMethodSelected(text);
        final Class[] parameterTypes = metodo.getParameterTypes();

        for (int i = 0; i < parameterTypes.length; i++) {

            final Class argument = parameterTypes[i];
            //int lastPoint = parameterTypes[i].getName().lastIndexOf(".");
            if (!argument.isPrimitive()) {
                JComboBox combo = new JComboBox();
                cargarComboItemsComplex(combo, argument);
                combo.addPopupMenuListener(new PopupMenuListener() {

                    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    }

                    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                     /* try {
                            Object getInstance = argument.newInstance();
                           Field[] fields = argument.getDeclaredFields();
                            System.out.println(argument.getName());
                            for (Field field : fields) {
                              if (field.getType().getSimpleName().equals("Motor"))
                              {
                                  field.set(field, field.getType().newInstance());
                                  //field.getType().newInstance();
                              }
                             //field.set(field, field.getType().newInstance());
                            }
                            GetWidgetValues valores = new GetWidgetValues();
                        GenericObjectEditor objectEditor = new GenericObjectEditor();

                        JComboBox cb = (JComboBox)e.getSource();
                        String selectedOption = (String)cb.getSelectedItem();

                        if (selectedOption.equals("Crear: "+ argument.getName())){

                            objectEditor.runEditor(valores, getInstance);

                        }

                        System.out.println(valores.getInstanceWidget());
                        } catch (InstantiationException ex) {
                            Logger.getLogger(CaseTestEditor.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(CaseTestEditor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        JComboBox cb = (JComboBox) e.getSource();
                        String selected = (String) cb.getSelectedItem();
                        if (selected.equals("Crear: " + argument.getName())) {
                        dialogoValores = new GetDialogValues();
                        NewDialog dialogo = new NewDialog(ini, true, dialogoValores);

                        dialogo.setDefaultCloseOperation(DISPOSE_ON_CLOSE);




                        dialogo.setCamposClase(argument.getDeclaredFields());
                        dialogo.LlenarTabla();

                        dialogo.setVisible(true);

                        }*/
                    }

                    public void popupMenuCanceled(PopupMenuEvent e) {
                    }
                });

                combo.setEditable(true);
                combo.addItem("Crear: " + parameterTypes[i].getName());
                DefaultCellEditor editor = new DefaultCellEditor(combo);
                editores.add(editor);
            } else {
                JComboBox combo = new JComboBox();
                cargarComboItemsPrimitive(combo, argument);
                combo.setEditable(true);

                DefaultCellEditor editor = new DefaultCellEditor(combo);
                editores.add(editor);
            }

        }

        tipoVarRetorno = metodo.getReturnType();
        cargarAssert(tipoVarRetorno);
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Argumento", "Valor"}, parameterTypes.length);
        tablaArgumentos = new JTable(model) {

            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                int modelColumn = super.convertColumnIndexToModel(column);
                if (modelColumn == 1 && row < parameterTypes.length) {
                    return editores.get(row);
                } else {
                    return super.getCellEditor(row, column);
                }

            }
        };


        panelTablaArgumentos.add(tablaArgumentos);
        Border line = BorderFactory.createLineBorder(Color.black);
        tablaArgumentos.setBorder(line);
        tablaArgumentos.setSelectionMode(0);
        tablaArgumentos.setGridColor(Color.black);
        tablaArgumentos.setSize(new Dimension(450, 150));
        tablaArgumentos.setLocation(40, 40);


        for (int i = 0; i < parameterTypes.length; i++) {


            tablaArgumentos.setValueAt(parameterTypes[i].getName(), i, 0);
        }


        panelTablaArgumentos.repaint();

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelInicial = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaVariables = new javax.swing.JTable();
        panelMetodoInfo = new javax.swing.JPanel();
        panelTablaArgumentos = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        panelAssert = new javax.swing.JPanel();
        lbAssertVariables = new javax.swing.JLabel();
        assertVariables = new javax.swing.JComboBox();
        lbAssertCondiciones = new javax.swing.JLabel();
        assertCondiciones = new javax.swing.JComboBox();
        lbResultadoAssert = new javax.swing.JLabel();
        resultadoAssert = new javax.swing.JTextField();
        assertMensaje = new javax.swing.JTextField();
        lbAssertMensaje = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        scroll = new javax.swing.JScrollPane();
        panel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        labelTest = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        btGenerar = new javax.swing.JButton();

        setTitle("Case Test Editor");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Historial de Variables", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 12))); // NOI18N

        tablaVariables.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Variable", "Metodo", "Tipo Retorno"
            }
        ));
        jScrollPane2.setViewportView(tablaVariables);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelMetodoInfo.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        panelTablaArgumentos.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Argumentos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 12))); // NOI18N

        jLabel1.setText("Argumento");

        jLabel2.setText("Valor");

        javax.swing.GroupLayout panelTablaArgumentosLayout = new javax.swing.GroupLayout(panelTablaArgumentos);
        panelTablaArgumentos.setLayout(panelTablaArgumentosLayout);
        panelTablaArgumentosLayout.setHorizontalGroup(
            panelTablaArgumentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaArgumentosLayout.createSequentialGroup()
                .addGap(123, 123, 123)
                .addComponent(jLabel1)
                .addGap(146, 146, 146)
                .addComponent(jLabel2)
                .addContainerGap(135, Short.MAX_VALUE))
        );
        panelTablaArgumentosLayout.setVerticalGroup(
            panelTablaArgumentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaArgumentosLayout.createSequentialGroup()
                .addGroup(panelTablaArgumentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addContainerGap(211, Short.MAX_VALUE))
        );

        panelAssert.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Assert", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 12))); // NOI18N

        lbAssertVariables.setText("Variable: ");

        assertVariables.setEditable(true);
        assertVariables.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                assertVariablesActionPerformed(evt);
            }
        });

        lbAssertCondiciones.setText("Condicion: ");

        assertCondiciones.setFont(new java.awt.Font("Calibri", 1, 12));
        assertCondiciones.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Elige una opcion..", "Igual", "No Igual", "Nulo", "No Nulo", "Verdadero", "Falso", " " }));
        assertCondiciones.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                assertCondicionesPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        assertCondiciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                assertCondicionesActionPerformed(evt);
            }
        });

        lbResultadoAssert.setText("Resultado: ");
        lbResultadoAssert.setEnabled(false);

        resultadoAssert.setEnabled(false);

        assertMensaje.setText("jTextField1");

        lbAssertMensaje.setText("Mensaje :");

        javax.swing.GroupLayout panelAssertLayout = new javax.swing.GroupLayout(panelAssert);
        panelAssert.setLayout(panelAssertLayout);
        panelAssertLayout.setHorizontalGroup(
            panelAssertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAssertLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAssertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbAssertCondiciones)
                    .addComponent(lbAssertVariables)
                    .addComponent(lbAssertMensaje))
                .addGap(18, 18, 18)
                .addGroup(panelAssertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(assertMensaje)
                    .addComponent(assertCondiciones, 0, 0, Short.MAX_VALUE)
                    .addComponent(assertVariables, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                .addComponent(lbResultadoAssert)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resultadoAssert, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        panelAssertLayout.setVerticalGroup(
            panelAssertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAssertLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(panelAssertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbAssertVariables)
                    .addComponent(assertVariables, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelAssertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbAssertCondiciones)
                    .addComponent(assertCondiciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resultadoAssert, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbResultadoAssert))
                .addGap(18, 18, 18)
                .addGroup(panelAssertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbAssertMensaje)
                    .addComponent(assertMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(53, Short.MAX_VALUE))
        );

        jButton4.setText("Guardar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelMetodoInfoLayout = new javax.swing.GroupLayout(panelMetodoInfo);
        panelMetodoInfo.setLayout(panelMetodoInfoLayout);
        panelMetodoInfoLayout.setHorizontalGroup(
            panelMetodoInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMetodoInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMetodoInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelMetodoInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(panelAssert, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panelTablaArgumentos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        panelMetodoInfoLayout.setVerticalGroup(
            panelMetodoInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMetodoInfoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelTablaArgumentos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelAssert, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Metodos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12))); // NOI18N

        scroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panel.setPreferredSize(new java.awt.Dimension(298, 287));

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 327, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 285, Short.MAX_VALUE)
        );

        scroll.setViewportView(panel);

        jLabel3.setIcon(new javax.swing.ImageIcon("/Users/danielbello/Pictures/greenArrowDown.jpeg")); // NOI18N
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        jLabel4.setIcon(new javax.swing.ImageIcon("/Users/danielbello/Pictures/greenArrowUp.jpeg")); // NOI18N
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Documentacion (HTML)", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 12))); // NOI18N

        labelTest.setText("jLabel1");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(labelTest)
                .addContainerGap(182, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(labelTest)
                .addContainerGap(471, Short.MAX_VALUE))
        );

        jButton3.setText("Dependencias");

        jButton2.setText("Nuevo");

        btGenerar.setText("Generar");
        btGenerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btGenerarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelInicialLayout = new javax.swing.GroupLayout(panelInicial);
        panelInicial.setLayout(panelInicialLayout);
        panelInicialLayout.setHorizontalGroup(
            panelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInicialLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(43, 43, 43)
                .addComponent(panelMetodoInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(panelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelInicialLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58))
                    .addGroup(panelInicialLayout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btGenerar, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        panelInicialLayout.setVerticalGroup(
            panelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInicialLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelMetodoInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelInicialLayout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelInicialLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(panelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btGenerar)
                            .addComponent(jButton3)
                            .addComponent(jButton2))))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addComponent(panelInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void assertCondicionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_assertCondicionesActionPerformed
}//GEN-LAST:event_assertCondicionesActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        Method method = this.getActualMethod();
        Metodo metodo = new Metodo();
        metodo.setNombre(method.getName());
        metodo.setClase(method.getClass().getName());
        metodo.setClaseSimpleName(method.getClass().getSimpleName());
        Retorno retorno = new Retorno(method.getReturnType().getName(), "Var"+ varId);
        varId++;
        metodo.setRetorno(retorno);
        AssertTest assertion = new AssertTest(assertMensaje.getText(),
                assertVariables.getSelectedItem().toString(), 
                assertCondiciones.getSelectedItem().toString());

        if (assertCondiciones.getSelectedItem().toString().equals("Igual") ||
                assertCondiciones.getSelectedItem().equals("No Igual"))
        {
            assertion.setValorAssert(resultadoAssert.getText());
        }

        metodo.setAssertLinea(assertion);

        ArrayList<Argumento> argumentos = new ArrayList<Argumento>();
        
        Argumento argumento;
        
        for (int i= 0; i < tablaArgumentos.getRowCount(); i++) {
            argumento = new Argumento();
            argumento.setTipo((String) tablaArgumentos.getValueAt(i, 0));
            argumento.setValor((String)tablaArgumentos.getValueAt(i, 1));
            argumentos.add(argumento);
            
        }
        metodo.setArgumentos(argumentos);

        metodosGuardados.add(metodo);

        tablaVariables.removeAll();
       
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tablaVariables.getModel();

        
        //for (Metodo metodo1 : metodosGuardados)
        //{
           Vector objects = new Vector();
            objects.add(metodo.getRetorno().getNombreVariable());
            objects.add(metodo.getNombre());
            objects.add(metodo.getRetorno().getRetorno());
            model.addRow(objects);


        



        for (Component component : panel.getComponents())
        {
            if (component.getClass().getName().equals("javax.swing.JTextField"))
            {
                JTextField textField = (JTextField)component;
                if (textField.getText().equals(metodo.getNombre()))
                    textField.setEnabled(false);
            }
        }


        //Argumento argumento









        /*XmlManager xmlManager = new XmlManager();

        Method method = this.getActualMethod();

        contMetodos += 1;

        metodosTest = this.agregarMetodo(method, metodosTest, contMetodos);

        for (Metodo metodo : metodosTest) {
            System.out.println("\nMETODOS TEST: " + metodo.getNombre() + ", " + metodo.getRetorno().getNombreVariable());
            ArrayList<Argumento> argumentos = metodo.getArgumentos();
            for (Argumento argumento : argumentos) {
                System.out.println("arg: " + argumento.getNombre() + ", " + argumento.getValor());
            }
        }*/

//        xmlManager.crearCasoPrueba("pruebaAnimal", metodosTest);

    }//GEN-LAST:event_jButton4ActionPerformed

    private void assertVariablesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_assertVariablesActionPerformed
    }//GEN-LAST:event_assertVariablesActionPerformed

    private void assertCondicionesPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_assertCondicionesPopupMenuWillBecomeInvisible
        if (assertCondiciones.getSelectedItem().equals("Igual")
                || assertCondiciones.getSelectedItem().equals("No Igual")) {
            lbResultadoAssert.setEnabled(true);
            resultadoAssert.setEnabled(true);
        } else {
            lbResultadoAssert.setEnabled(false);
            resultadoAssert.setEnabled(false);
        }
    }//GEN-LAST:event_assertCondicionesPopupMenuWillBecomeInvisible

    private void btGenerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btGenerarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btGenerarActionPerformed

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        // TODO add your handling code here:


        for (int i = 0; i < panel.getComponents().length; i++) {
            JTextField text = (JTextField)panel.getComponent(i);
            if (text.getText().equals(actualNameMethod))
            {
                if (i!=0)
                {
                    int pos = i;
                    pos--;
                    JTextField textAntes = new JTextField();
                    textAntes = (JTextField) panel.getComponent(pos);
                    String textAntesValue = textAntes.getText();
                    textAntes.setText(actualNameMethod);
                    text.setText(textAntesValue);

                }
            }

        }


    }//GEN-LAST:event_jLabel4MouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        // TODO add your handling code here:

        for (int i = 0; i < panel.getComponents().length; i++) {

            JTextField text = (JTextField)panel.getComponent(i);
            if (text.getText().equals(actualNameMethod))
            {
                if (i < panel.getComponents().length)
                {
                    //int pos = i;
                    //pos++;
                    JTextField textDespues = (JTextField) panel.getComponent(i+1);
                    //JTextField temp = new JTextField();
                    //temp = text;
                    text.setText(textDespues.getText());
                    textDespues.setText(actualNameMethod);

                   break;
                    
                    

                }
            }
            

        }
    }//GEN-LAST:event_jLabel3MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox assertCondiciones;
    private javax.swing.JTextField assertMensaje;
    private javax.swing.JComboBox assertVariables;
    private javax.swing.JButton btGenerar;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelTest;
    private javax.swing.JLabel lbAssertCondiciones;
    private javax.swing.JLabel lbAssertMensaje;
    private javax.swing.JLabel lbAssertVariables;
    private javax.swing.JLabel lbResultadoAssert;
    private javax.swing.JPanel panel;
    private javax.swing.JPanel panelAssert;
    private javax.swing.JPanel panelInicial;
    private javax.swing.JPanel panelMetodoInfo;
    private javax.swing.JPanel panelTablaArgumentos;
    private javax.swing.JTextField resultadoAssert;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JTable tablaVariables;
    // End of variables declaration//GEN-END:variables

    public ArrayList<Metodo> agregarMetodo(Method method, ArrayList<Metodo> metodosTest, Integer cont) {

        XmlManager xmlManager = new XmlManager();

        metodosTest = xmlManager.agregarMetodoALista(metodosTest, method, cont, this.getArgumentos(method), new AssertTest("miMensaje1", "var1.x", "AssertNotNull"));

        return metodosTest;
    }

    public ArrayList getArgumentos(Method method) {
        Integer cont = 1;

        ArrayList<Argumento> argumentos = new ArrayList<Argumento>();

        Class[] parametros = method.getParameterTypes();

        for (Class clazz : parametros) {
            argumentos.add(new Argumento("arg" + cont, clazz.getName(), tablaArgumentos.getValueAt(cont - 1, 1).toString()));
            cont++;
        }
        return argumentos;
    }

    

   

   
}
    

