/*
 * CaseTestEditor.java
 *
 * Created on Oct 28, 2010, 3:24:27 PM
 */
package com.teg.vista;

import com.teg.dominio.Argumento;
import com.teg.dominio.AssertTest;
import com.teg.dominio.Metodo;
//import com.teg.logica.GenericObjectEditor;
//import com.teg.logica.GetWidgetValues;
import com.teg.logica.XmlManager;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import org.jdom.*;
import org.jdom.output.*;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author maya
 */
public class CaseTestEditor extends javax.swing.JInternalFrame {

    private ArrayList<Method> metodos = new ArrayList<Method>();
    private ArrayList<DefaultCellEditor> editores = new ArrayList<DefaultCellEditor>();
    private ArrayList<Metodo> metodosGuardados = new ArrayList<Metodo>();
    private ArrayList<File> archivosJavaDoc = new ArrayList<File>();
    private static int varId = 0;
    private Class tipoVarRetorno;
    private String valorFila;
    private String actualNameMethod;
    private JTable tablaArgumentos;
    private Inicio inicio;
    private Document docXml;
    

    /** Creates new form CaseTestEditor */
    @SuppressWarnings("LeakingThisInConstructor")
    public CaseTestEditor(ArrayList<Method> metodos, Inicio inicio) {
        initComponents();
        this.metodos = metodos;
        this.inicio = inicio;
        this.inicio.getSeleccionarJar().setEnabled(false);
        archivosJavaDoc = inicio.getArchivosJavaDoc();
        javax.swing.plaf.InternalFrameUI ifu = this.getUI();
        ((javax.swing.plaf.basic.BasicInternalFrameUI) ifu).setNorthPane(null);
    }

    public void cargarMetodos() {
        // int panelHeight = this.panel.getHeight();
        ArrayList<Object> metodosoLista = new ArrayList<Object>();

        for (Method method : metodos) {
            // panelHeight = panelHeight + 25;
            metodosoLista.add(method.getName());


        }
        addMethodList(metodosoLista);
    }

    public Method getActualMethod() {
        Method metodo = null;
        for (Method method : metodos) {
            if (method.getName().equals(actualNameMethod)) {
                metodo = method;
            }
        }
        return metodo;
    }

   /* public void cargarJar() throws IOException
    {

        String jarName = "dataWidget.jar";
        File jarFile = new File(inicio.getDirectorioCasoPrueba().getPath(), jarName);
        byte[] buffer = new byte[1024];
        try {
            JarOutputStream outJar = new JarOutputStream(new FileOutputStream(jarFile));
            outJar.setLevel(Deflater.DEFAULT_COMPRESSION);

            //Escribiendo el primer fichero
            JarEntry jarEntry = new JarEntry("metawidgetData.xml");
            outJar.putNextEntry(jarEntry);

            // Leyendo los datos del fichero y escribiendolos en el Jar
            InputStream input = new FileInputStream(new File(inicio.getDirectorioCasoPrueba().getPath(),"metawidgetData.xml"));
            int count;
            while( ( count = input.read(buffer, 0, 1024 ) ) != -1 )
            {
                outJar.write(buffer, 0, count);
            }

            outJar.closeEntry();
               outJar.close();
        } catch ( IOException e ) {
            System.out.println( "Error escribiendo el jar " + e);
        }
    }*/

    public void crearMetawidgetMetadata(Document docXml) throws JDOMException, IOException
    {
        File verificar = new File(inicio.getDirectorioCasoPrueba() + "/" + "metawidgetData.xml");
        if (verificar.exists()) {
            SAXBuilder builder = new SAXBuilder(false);
            Document docRead = builder.build(inicio.getDirectorioCasoPrueba().getPath() + "/" + "metawidgetData.xml");
            Element raizWriter = docXml.getRootElement();
            docRead.removeContent();
            docRead.addContent(raizWriter);
            BufferedWriter fw = new BufferedWriter(new FileWriter(inicio.getDirectorioCasoPrueba().getPath() + "/" + "metawidgetData.xml"));
            XMLOutputter outputter = new XMLOutputter();
            outputter.output(docRead, fw);
            fw.close();



        } else {


            try {
                XMLOutputter out = new XMLOutputter();
                //path = inicio.getDirectorioCasoPrueba().getPath() + "/" + "metawidgetData.xml";
                FileOutputStream file = new FileOutputStream(inicio.getDirectorioCasoPrueba().getPath() + "/" + "metawidgetData.xml");
                out.output(docXml, file);
                file.flush();
                file.close();
                out.output(docXml, System.out);
            } catch (Exception e) {
            }
            //return path;


        }
    }

    public void addMethodList(ArrayList<Object> metodosLista) {

        listaMetodos.setListData(metodosLista.toArray());

        /*

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
        if (!metodoTest.isEnabled() == false) {
        cargarTablaArgumentos(metodoTest.getText());
        actualNameMethod = metodoTest.getText();
        Method metodoActual = getMethodSelected(actualNameMethod);
        javaDocPanel(metodoActual.getDeclaringClass().getSimpleName(), metodoActual.getName(), archivosJavaDoc);
        }
        }
        });*/
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
        int countVar = varId;
        countVar++;
        if (metodoRetorno.isPrimitive()) {

            assertVariables.setSelectedItem("var" + countVar);


        } else {
            assertVariables.addItem("var" + countVar);
            Field[] fields = metodoRetorno.getDeclaredFields();
            for (Field field : fields) {
                assertVariables.addItem("var" + countVar
                        + "." + field.getName());
            }

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
            } else {
                if (retorno.getDeclaredFields().length != 0) {
                    for (Field field : retorno.getDeclaredFields()) {
                        if (field.getType().getName().equals(argument.getName())) {
                            combo.addItem(metodo.getRetorno().getNombreVariable()
                                    + "." + field.getName());
                        }
                    }

                }

            }
        }
    }

    public void desahibilitarMetodosData() {
        for (Component component : panelTablaArgumentos.getComponents()) {
            component.setEnabled(false);
        }
        assertVariables.setEnabled(false);
        assertVariables.removeAllItems();
        assertCondiciones.setEnabled(false);
        resultadoAssert.setEnabled(false);
        assertMensaje.setEnabled(false);
        guardarBt.setEnabled(false);
    }

    public void habilitarMetodosData() {
        for (Component component : panelTablaArgumentos.getComponents()) {
            component.setEnabled(true);
        }
        assertVariables.setEnabled(true);
        assertCondiciones.setEnabled(true);
        resultadoAssert.setEnabled(true);
        assertMensaje.setEnabled(true);
        guardarBt.setEnabled(true);

    }

    public void cargarComboItemsPrimitive(JComboBox combo, Class parameter) {
        Method method;

        for (Metodo metodo : metodosGuardados) {

            method = getMetodoGuardado(metodo.getNombre());
            Class retorno = method.getReturnType();

            if (retorno.getDeclaredFields().length != 0) {

                for (Field field : retorno.getDeclaredFields()) {


                    if (field.getType().getName().equals(parameter.getName())) {
                        combo.addItem(metodo.getRetorno().getNombreVariable() + "." + field.getName());
                    }
                }




            } else {
                if (retorno.getName().equals(parameter.getName())) {
                    combo.addItem(metodo.getRetorno().getNombreVariable());
                }
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
    
    public void deepInstantiate(Object claseInstancia, Element raiz) throws InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException
    {

        raiz.addContent("\n");
        Element entidad = getEntity(claseInstancia.getClass());
        raiz.addContent(entidad);

            
        Field[] campos = claseInstancia.getClass().getDeclaredFields();
        for (Field field : campos) {
            boolean flag = false;
            if (!field.getType().isPrimitive() && !field.getType().getName().equals("java.lang.String") &&
                    !field.getType().getName().equals("java.lang.Integer"))
            {
                Method[] metodosClase = claseInstancia.getClass().getDeclaredMethods();
                for (Method method : metodosClase) {
                    if (method.getParameterTypes().length > 0)
                    {
                    if (method.getParameterTypes()[0].getName().equals(field.getType().getName()) &&
                            (method.getReturnType().getName() == null ? "void" == null : method.getReturnType().getName().equals("void"))
                            && flag ==false)
                    {
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


    public Element getEntity(Class clase)
    {
        //0212 4092381.
       // ArrayList<Attribute> listaAtributos = new ArrayList<Attribute>();
        Element entidad = new Element("entity");
        Attribute tipoEntidad = new Attribute("type", clase.getName());
       // Attribute seccion = new Attribute("section", clase.getSimpleName().toUpperCase());
        //listaAtributos.add(tipoEntidad);
        //listaAtributos.add(seccion);
        //entidad.setAttributes(listaAtributos);

        entidad.setAttribute(tipoEntidad);
        entidad.addContent("\n \t");
        Field[] fields = clase.getDeclaredFields();
        for (Field field : fields) {
            Element prop = new Element("property");

            Attribute atr = new Attribute("name", field.getName());



            ArrayList<Attribute> listaAtributosProperty = new ArrayList<Attribute>();
            if (!field.getType().isPrimitive() && !field.getType().getName().equals("java.lang.String") &&
                    !field.getType().getName().equals("java.lang.Integer"))
            {


                Attribute atr2 = new Attribute("section", field.getName().toUpperCase());
                Attribute atr3 = new Attribute ("type", field.getType().getName());

                listaAtributosProperty.add(atr);

                listaAtributosProperty.add(atr2);

                listaAtributosProperty.add(atr3);
                prop.setAttributes(listaAtributosProperty);

                entidad.addContent("\n \t");


            }
 else
            {

                listaAtributosProperty.add(atr);
                prop.setAttributes(listaAtributosProperty);

                entidad.addContent("\n \t");

            }

            entidad.addContent(prop);
            entidad.addContent("\n");
        }

        return entidad;
    }
    public Object getInstance(Class clase) throws InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, JDOMException, IOException
    {

        Element raiz = new Element("inspection-result");
        raiz.addContent("\n");
        Element entidad = getEntity(clase);
        raiz.addContent(entidad);



        Object claseInstancia = clase.newInstance();

        Field[] campos = clase.getDeclaredFields();
        for (Field field : campos) {
            boolean flag =false;
            if (!field.getType().isPrimitive() && !field.getType().getName().equals("java.lang.String") &&
                    !field.getType().getName().equals("java.lang.Integer"))
            {
                Method[] metodosClase = clase.getDeclaredMethods();
                 for (Method method : metodosClase) {

                    if (method.getParameterTypes().length == 1 &&
                            method.getParameterTypes()[0].getName().equals(field.getType().getName()) &&
                            (method.getReturnType().getName() == null ? "void" == null : method.getReturnType().getName().equals("void"))
                            && flag == false)
                    {
                        Object campoInstance = field.getType().newInstance();
                        clase.getMethod(method.getName(), field.getType()).invoke(claseInstancia, campoInstance);
                        flag = true;
                        deepInstantiate(campoInstance,raiz);
                    }
                }
            }
        }
        docXml = new Document(raiz);
        crearMetawidgetMetadata(docXml);


     return claseInstancia;

    }

    /*public void getJarDataWidget() throws IOException {
        File jarFIle = new File(inicio.getDirectorioCasoPrueba().getPath() + "/" + "dataWidget.jar");

        ClassPathReflective.addFile(jarFIle);

    }*/

    @SuppressWarnings("empty-statement")
    public void cargarTablaArgumentos(String text) {

        for (Component component : panelTablaArgumentos.getComponents()) {
            if (!component.getClass().getName().equals("javax.swing.JLabel")) {
                panelTablaArgumentos.remove(component);
            }
        }

        editores.clear();
        Method metodo;

        metodo = getMethodSelected(text);
        final Class[] parameterTypes = metodo.getParameterTypes();

        for (int i = 0; i < parameterTypes.length; i++) {

            final Class argument = parameterTypes[i];
            //int lastPoint = parameterTypes[i].getName().lastIndexOf(".");
            if (!argument.isPrimitive()) {
                final JComboBox combo = new JComboBox();
                cargarComboItemsComplex(combo, argument);
                combo.addPopupMenuListener(new PopupMenuListener() {

                    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                       
                    }

                    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                        String item = "";
                        JComboBox cb = new JComboBox();
                        cb = (JComboBox) e.getSource();
                        

                        item = cb.getSelectedItem().toString();
                         String[] cadena = item.split(":");
                         if (cadena[0].equals("Crear"))
                         {
                            try {

                                Object claseInstance = getInstance(argument);
                                //cargarJar();
                                //getJarDataWidget();
                                System.out.println(claseInstance.getClass().getName());
                               //cargarJar(crearMetawidgetMetadata());
                               
                               
                               InstanceForm editorInstance = new InstanceForm(claseInstance, inicio.getDirectorioCasoPrueba().getPath());
                                editorInstance.Visible();
                            } catch (JDOMException ex) {
                                Logger.getLogger(CaseTestEditor.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(CaseTestEditor.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InstantiationException ex) {
                                Logger.getLogger(CaseTestEditor.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IllegalAccessException ex) {
                                Logger.getLogger(CaseTestEditor.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (NoSuchMethodException ex) {
                                Logger.getLogger(CaseTestEditor.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IllegalArgumentException ex) {
                                Logger.getLogger(CaseTestEditor.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InvocationTargetException ex) {
                                Logger.getLogger(CaseTestEditor.class.getName()).log(Level.SEVERE, null, ex);
                            }
                         }
                        
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
        tablaArgumentos.setSize(new Dimension(410, 120));
        tablaArgumentos.setRowHeight(25);
        tablaArgumentos.setLocation(20, 40);

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

        popMenuMetodos = new javax.swing.JPopupMenu();
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
        guardarBt = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaMetodos = new javax.swing.JList();
        jButton3 = new javax.swing.JButton();
        newTestEscenario = new javax.swing.JButton();
        generar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaMetodosRegistrados = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();

        setTitle("Case Test Editor");

        panelInicial.setBackground(new java.awt.Color(153, 153, 153));

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
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(9, Short.MAX_VALUE))
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
                .addGap(76, 76, 76)
                .addComponent(jLabel1)
                .addGap(136, 136, 136)
                .addComponent(jLabel2)
                .addContainerGap(137, Short.MAX_VALUE))
        );
        panelTablaArgumentosLayout.setVerticalGroup(
            panelTablaArgumentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaArgumentosLayout.createSequentialGroup()
                .addGroup(panelTablaArgumentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addContainerGap(137, Short.MAX_VALUE))
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

        assertMensaje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                assertMensajeActionPerformed(evt);
            }
        });

        lbAssertMensaje.setText("Mensaje :");

        javax.swing.GroupLayout panelAssertLayout = new javax.swing.GroupLayout(panelAssert);
        panelAssert.setLayout(panelAssertLayout);
        panelAssertLayout.setHorizontalGroup(
            panelAssertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAssertLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(panelAssertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAssertLayout.createSequentialGroup()
                        .addGroup(panelAssertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbAssertCondiciones, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbAssertVariables, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelAssertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelAssertLayout.createSequentialGroup()
                                .addComponent(assertVariables, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5))
                            .addComponent(assertCondiciones, 0, 241, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAssertLayout.createSequentialGroup()
                        .addGroup(panelAssertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbAssertMensaje)
                            .addComponent(lbResultadoAssert, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelAssertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(assertMensaje, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                            .addComponent(resultadoAssert, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(67, 67, 67))
        );

        panelAssertLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {lbAssertCondiciones, lbAssertMensaje, lbAssertVariables, lbResultadoAssert});

        panelAssertLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {assertCondiciones, assertMensaje, assertVariables, resultadoAssert});

        panelAssertLayout.setVerticalGroup(
            panelAssertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAssertLayout.createSequentialGroup()
                .addGroup(panelAssertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(assertVariables, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbAssertVariables, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelAssertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(assertCondiciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbAssertCondiciones, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelAssertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(resultadoAssert, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbResultadoAssert, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelAssertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbAssertMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(assertMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        panelAssertLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {assertCondiciones, assertMensaje, assertVariables, lbAssertCondiciones, lbAssertMensaje, lbAssertVariables, lbResultadoAssert, resultadoAssert});

        guardarBt.setText("Guardar");
        guardarBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarBtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelMetodoInfoLayout = new javax.swing.GroupLayout(panelMetodoInfo);
        panelMetodoInfo.setLayout(panelMetodoInfoLayout);
        panelMetodoInfoLayout.setHorizontalGroup(
            panelMetodoInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMetodoInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMetodoInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMetodoInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelMetodoInfoLayout.createSequentialGroup()
                            .addComponent(panelAssert, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(18, 18, 18))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMetodoInfoLayout.createSequentialGroup()
                            .addComponent(guardarBt, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap()))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMetodoInfoLayout.createSequentialGroup()
                        .addComponent(panelTablaArgumentos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        panelMetodoInfoLayout.setVerticalGroup(
            panelMetodoInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMetodoInfoLayout.createSequentialGroup()
                .addComponent(panelTablaArgumentos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelAssert, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(guardarBt)
                .addGap(85, 85, 85))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Metodos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 12))); // NOI18N

        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        listaMetodos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listaMetodosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(listaMetodos);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(276, 276, 276)
                        .addComponent(jLabel3))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(157, 157, 157)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton3.setText("Dependencias");

        newTestEscenario.setText("New Test Escenario");
        newTestEscenario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newTestEscenarioActionPerformed(evt);
            }
        });

        generar.setText("Atras..");
        generar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generarActionPerformed(evt);
            }
        });

        jButton1.setText("Siguiente..");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Escenario de Prueba", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 12))); // NOI18N

        tablaMetodosRegistrados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Orden", "Metodo"
            }
        ));
        jScrollPane3.setViewportView(tablaMetodosRegistrados);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setText("Nombre Escenario de Prueba :");

        javax.swing.GroupLayout panelInicialLayout = new javax.swing.GroupLayout(panelInicial);
        panelInicial.setLayout(panelInicialLayout);
        panelInicialLayout.setHorizontalGroup(
            panelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInicialLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelInicialLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(panelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelInicialLayout.createSequentialGroup()
                                .addComponent(generar, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(newTestEscenario, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton3)
                                .addGap(18, 18, 18)
                                .addComponent(jButton1))
                            .addComponent(panelMetodoInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 513, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelInicialLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(23, 23, 23))
        );
        panelInicialLayout.setVerticalGroup(
            panelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInicialLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelInicialLayout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelInicialLayout.createSequentialGroup()
                        .addGroup(panelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelMetodoInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 453, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addGroup(panelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(generar, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newTestEscenario)
                    .addComponent(jButton3)
                    .addComponent(jButton1))
                .addGap(44, 44, 44))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelInicial, javax.swing.GroupLayout.DEFAULT_SIZE, 931, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 595, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void assertCondicionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_assertCondicionesActionPerformed
}//GEN-LAST:event_assertCondicionesActionPerformed

    private void guardarBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarBtActionPerformed

        Method method = this.getActualMethod();
        varId++;

        Metodo metodoActual = this.agregarMetodo(method, varId, this.getActualAssert(), this.getArgumentos(method));

        tablaVariables.removeAll();
        tablaMetodosRegistrados.removeAll();

        DefaultTableModel modelMetodos = new DefaultTableModel();
        modelMetodos = (DefaultTableModel) tablaMetodosRegistrados.getModel();

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tablaVariables.getModel();

        Vector objects = new Vector();
        objects.add(metodoActual.getRetorno().getNombreVariable());
        objects.add(metodoActual.getNombre());
        objects.add(metodoActual.getRetorno().getRetorno());
        model.addRow(objects);

        Vector metodosObj = new Vector();
        metodosObj.add(varId);
        metodosObj.add(metodoActual.getNombre());
        modelMetodos.addRow(metodosObj);

    }//GEN-LAST:event_guardarBtActionPerformed

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

    private void generarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generarActionPerformed

        XmlManager xmlManager = new XmlManager();

        xmlManager.crearCasoPrueba(this.inicio.getNombreCasoPrueba(), metodosGuardados);

    }//GEN-LAST:event_generarActionPerformed

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked

        for (int i = 0; i < listaMetodos.getComponents().length; i++) {
            JTextField text = (JTextField) listaMetodos.getComponent(i);
            if (text.getText().equals(actualNameMethod)) {
                if (i != 0) {
                    int pos = i;
                    pos--;
                    JTextField textAntes = new JTextField();
                    textAntes = (JTextField) listaMetodos.getComponent(pos);
                    String textAntesValue = textAntes.getText();
                    textAntes.setText(actualNameMethod);
                    text.setText(textAntesValue);
                }
            }
        }

    }//GEN-LAST:event_jLabel4MouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked

        for (int i = 0; i < listaMetodos.getComponents().length; i++) {

            JTextField text = (JTextField) listaMetodos.getComponent(i);

            if (text.getText().equals(actualNameMethod)) {
                if (i < listaMetodos.getComponents().length) {
                    JTextField textDespues = (JTextField) listaMetodos.getComponent(i + 1);
                    text.setText(textDespues.getText());
                    textDespues.setText(actualNameMethod);

                    break;
                }
            }
        }
    }//GEN-LAST:event_jLabel3MouseClicked

    private void assertMensajeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_assertMensajeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_assertMensajeActionPerformed

    private void listaMetodosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaMetodosMouseClicked

        popMenuMetodos.setVisible(false);
        popMenuMetodos.removeAll();

        String metodoNombre = (String) listaMetodos.getSelectedValue();

        if (evt.getButton() == MouseEvent.BUTTON3) {
            

            JMenuItem item = new JMenuItem();
            item.setText("Ver Javadoc");
            item.addMouseListener(new MouseListener() {

                public void mouseClicked(MouseEvent me) {
                    popMenuMetodos.setVisible(false);
                    Method metodoActual = getMethodSelected(actualNameMethod);

                    JavadocFrame javadoc = new JavadocFrame(archivosJavaDoc, metodoActual);
                    javadoc.setVisible(true);
                    
                }

                public void mousePressed(MouseEvent me) {

                }

                public void mouseReleased(MouseEvent me) {

                }

                public void mouseEntered(MouseEvent me) {

                }

                public void mouseExited(MouseEvent me) {

                }
            });
            popMenuMetodos.add(item);
            popMenuMetodos.setLocation(evt.getLocationOnScreen());
            popMenuMetodos.setVisible(true);
            


            
        } else {
            if (evt.getButton() == MouseEvent.BUTTON1) {

                boolean isIn = false;

                for (int i = 0; i < metodosGuardados.size(); i++) {
                    if (metodosGuardados.get(i).getNombre().equals(metodoNombre)) {
                        isIn = true;
                    }
                }

                if (isIn == true) {

                    cargarTablaArgumentos(metodoNombre);
                    desahibilitarMetodosData();




                } else {
                    habilitarMetodosData();
                    cargarTablaArgumentos(metodoNombre);
                    actualNameMethod = metodoNombre;
                }
            }
        }


    }//GEN-LAST:event_listaMetodosMouseClicked

    private void newTestEscenarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newTestEscenarioActionPerformed

        //if (!actualNameMethod.equals("")) {
          //  Method metodoActual = getMethodSelected(actualNameMethod);
            //javaDocPanel(metodoActual.getDeclaringClass().getSimpleName(), metodoActual.getName(), archivosJavaDoc);
        //}

        
        metodosGuardados.clear();
        tablaVariables.setModel(new DefaultTableModel(0,0));
        
        tablaMetodosRegistrados.setModel(new DefaultTableModel(0,0));
        assertVariables.removeAllItems();
        assertMensaje.setText("");
        resultadoAssert.setText("");
        for (Component component : panelTablaArgumentos.getComponents()) {
            if (!component.getClass().getName().equals("javax.swing.JLabel")) {
                panelTablaArgumentos.remove(component);
            }
            this.repaint();
        }

    }//GEN-LAST:event_newTestEscenarioActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox assertCondiciones;
    private javax.swing.JTextField assertMensaje;
    private javax.swing.JComboBox assertVariables;
    private javax.swing.JButton generar;
    private javax.swing.JButton guardarBt;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lbAssertCondiciones;
    private javax.swing.JLabel lbAssertMensaje;
    private javax.swing.JLabel lbAssertVariables;
    private javax.swing.JLabel lbResultadoAssert;
    private javax.swing.JList listaMetodos;
    private javax.swing.JButton newTestEscenario;
    private javax.swing.JPanel panelAssert;
    private javax.swing.JPanel panelInicial;
    private javax.swing.JPanel panelMetodoInfo;
    private javax.swing.JPanel panelTablaArgumentos;
    private javax.swing.JPopupMenu popMenuMetodos;
    private javax.swing.JTextField resultadoAssert;
    private javax.swing.JTable tablaMetodosRegistrados;
    private javax.swing.JTable tablaVariables;
    // End of variables declaration//GEN-END:variables

    public String setAssertCondition(String assertCondition) {
        String condicion = "";

        if (assertCondition.equals("Igual")) {
            condicion = "assertEquals";
        }

        if (assertCondition.equals("No Igual")) {
            condicion = "assertNotSame";
        }

        if (assertCondition.equals("Nulo")) {
            condicion = "assertNull";
        }

        if (assertCondition.equals("No Nulo")) {
            condicion = "assertNotNull";
        }

        if (assertCondition.equals("Verdadero")) {
            condicion = "assertTrue";
        }

        if (assertCondition.equals("Falso")) {
            condicion = "assertFalse";
        }

        return condicion;
    }

    public AssertTest getActualAssert() {
        AssertTest assertion = new AssertTest(assertMensaje.getText(),
                assertVariables.getSelectedItem().toString(),
                setAssertCondition(assertCondiciones.getSelectedItem().toString()));

        if (assertCondiciones.getSelectedItem().toString().equals("Igual")
                || assertCondiciones.getSelectedItem().equals("No Igual")) {
            assertion.setValorAssert(resultadoAssert.getText());
        }
        return assertion;
    }

    public Metodo agregarMetodo(Method method, Integer cont, AssertTest condicionAssert, ArrayList<Argumento> argumentos) {

        Metodo metodo = null;

        XmlManager xmlManager = new XmlManager();

        metodo = xmlManager.agregarMetodoALista(metodosGuardados, method, cont, argumentos, condicionAssert);

        return metodo;
    }

    public ArrayList<Argumento> getArgumentos(Method method) {

        Integer cont = 1;

        ArrayList<Argumento> argumentos = new ArrayList<Argumento>();

        Class[] parametros = method.getParameterTypes();

        for (Class clazz : parametros) {
            Argumento argumento = new Argumento();
            argumento.setNombre("arg" + cont);
            argumento.setTipo(clazz.getName());
            argumento.setValor(tablaArgumentos.getValueAt(cont - 1, 1).toString());
            argumentos.add(argumento);
            cont++;
        }
        return argumentos;
    }

    public File getMethodHTML(ArrayList<File> archivos, String className) {

        File archivo = null;

        for (File file : archivos) {
            if (file.getName().equals(className + ".html")) {
                archivo = file;
                return archivo;
            }
        }
        return archivo;
    }

    public void javaDocPanel(String className, String methodName, ArrayList<File> archivos) {

        File file = this.getMethodHTML(archivos, className);

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file.getPath()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            Pattern pattern = Pattern.compile("<H3>" + methodName + "</H3>.*<!-- ========= END OF CLASS DATA ========= --><HR>");
            Matcher matcherExterno = pattern.matcher(sb.toString());

            while (matcherExterno.find()) {
                String javadocMetodo = matcherExterno.group().substring(0, matcherExterno.group().indexOf("<HR>"));
                //editorPane.setText(javadocMetodo);
                //editorPane.setContentType("text/html");
            }
        } catch (IOException ex) {
            Logger.getLogger(CaseTestEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
