/*
 * CaseTestEditor.java
 *
 * Created on Oct 28, 2010, 3:24:27 PM
 */
package com.teg.vista;

import com.teg.dominio.Argumento;
import com.teg.dominio.AssertTest;
import com.teg.dominio.EscenarioPrueba;
import com.teg.dominio.Metodo;
import com.teg.dominio.VariableInstancia;
import com.teg.logica.WidgetObjectLoading;
import com.teg.logica.XmlManager;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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

/**
 *
 * @author maya
 */
public class CaseTestEditor extends javax.swing.JInternalFrame {

    private ArrayList<Method> metodos = new ArrayList<Method>();
   
    private ArrayList<DefaultCellEditor> editores = new ArrayList<DefaultCellEditor>();
    private ArrayList<Metodo> metodosGuardados = new ArrayList<Metodo>();
    private ArrayList<VariableInstancia> variablesGuardadas = new ArrayList<VariableInstancia>();
    private ArrayList<File> archivosJavaDoc = new ArrayList<File>();
    private ArrayList<EscenarioPrueba> escenariosPrueba = new ArrayList<EscenarioPrueba>();
    private WidgetObjectLoading listWidget = new WidgetObjectLoading();
    private static int varId = 0;
    private static int objId = 0;
    private Class tipoVarRetorno;
    private String actualNameMethod;
    private JTable tablaArgumentos;
    private Inicio inicio;
    private Document docXml;
    private Integer contComplejo = 1;

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

    

    public void cargarMetodos() {
        ArrayList<Object> metodosLista = new ArrayList<Object>();

        for (Method method : metodos) {
            metodosLista.add(method.getName());
        }
        addMethodList(metodosLista);
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

    public void crearMetawidgetMetadata(Document docXml) throws JDOMException, IOException {

        try {
            XMLOutputter out = new XMLOutputter();
            FileOutputStream file = new FileOutputStream(inicio.getDirectorioCasoPrueba().getPath() + "/" + "metawidgetData.xml");
            out.output(docXml, file);
            file.flush();
            file.close();
            out.output(docXml, System.out);
        } catch (Exception e) {
        }
    }

    public void addMethodList(ArrayList<Object> metodosLista) {

        listaMetodos.setListData(metodosLista.toArray());
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

        for (VariableInstancia variable : variablesGuardadas) {
            Class variableClase = variable.getInstancia().getClass();

            if (variableClase.getName().equals(argument.getName())) {
                combo.addItem(variable.getNombreVariable());
            } else {
                if (variableClase.getDeclaredFields().length != 0) {
                    for (Field field : variableClase.getDeclaredFields()) {
                        if (field.getType().getName().equals(argument.getName())) {
                            combo.addItem(variable.getNombreVariable()
                                    + "." + field.getName());
                        }
                    }
                }
            }
        }
    }

    public void deshabilitarMetodos() {
        for (Component component : panelTablaArgumentos.getComponents()) {
            component.setEnabled(false);
        }
        assertVariables.setEnabled(false);
        assertCondiciones.setEnabled(false);
        resultadoAssert.setEnabled(false);
        assertMensaje.setEnabled(false);
        guardarBt.setEnabled(false);
    }

    public void deshabilitarMetodosData() {
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

    /**
     * 
     * @param nombreMetodo
     */
    public void habilitarMetodosData(String nombreMetodo) {
        for (Component component : panelTablaArgumentos.getComponents()) {
            component.setEnabled(true);
        }

        Method metodo = getMethodSelected(nombreMetodo);

        if (!metodo.getReturnType().getName().equals("void")) {
            assertVariables.setEnabled(true);
            assertCondiciones.setEnabled(true);
            resultadoAssert.setEnabled(true);
            assertMensaje.setEnabled(true);
            guardarBt.setEnabled(true);
        } else {

            assertCondiciones.setEnabled(false);
            resultadoAssert.setEnabled(false);
            assertMensaje.setEnabled(false);
            guardarBt.setEnabled(true);

        }

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

        for (VariableInstancia variable : variablesGuardadas)
        {
            Class variableClase = variable.getInstancia().getClass();

            if (variableClase.getDeclaredFields().length != 0)
            {
                for (Field field : variableClase.getDeclaredFields())
                {
                    if (field.getType().getName().equals(parameter.getName())) {
                        combo.addItem(variable.getNombreVariable() +
                                "." + field.getName());
                    }
                }
            }
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

            if (!field.getType().isPrimitive() && !field.getType().getName().equals("java.lang.String")
                    && !field.getType().getName().equals("java.lang.Integer")) {

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



    public Object getInstance(Class clase) throws InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, JDOMException, IOException {

        
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
        docXml = new Document(raiz);
        crearMetawidgetMetadata(docXml);

        return claseInstancia;
        
    }

    public void addInstanceVariable() {
        if (listWidget.getObject().size() > 0) {
            objId++;
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) tablaVariables.getModel();

            Method method = this.getActualMethod();

            for (Object object : listWidget.getObject()) {

                VariableInstancia varInstancia = new VariableInstancia();
                varInstancia.setInstancia(object);
                varInstancia.setNombreVariable("object" + objId);
                variablesGuardadas.add(varInstancia);
                Vector objects = new Vector();
                objects.add("object" + objId);

                objects.add(method.getName());
                objects.add(object.getClass().getName());
                model.addRow(objects);
            }

        }

    }

    public boolean argumentoEsColeccion(Class clase) {
        boolean esColeccion = false;
        Class[] interfaces = clase.getInterfaces();

        for (Class class1 : interfaces) {
            if (class1.getName().equals("java.util.Map")
                    || class1.getName().equals("java.util.Set")
                    || class1.getName().equals("java.util.List")
                    || class1.getName().equals("java.util.Queue")) {
                esColeccion = true;
            }
        }
        return esColeccion;
    }

    public boolean argumentoEsArreglo(Class clase) {
        boolean esArreglo = false;

        if (clase.isArray()) {
            esArreglo = true;
        }
        return esArreglo;
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

   private boolean verificarDato(Class clase)
    {

        boolean verificado = false;
        if (clase.getName().equals("java.lang.Integer") ||
                clase.getName().equals("java.lang.Float") ||
                clase.getName().equals("java.lang.Double") ||
                clase.getName().equals("java.lang.Long") ||
                clase.getName().equals("java.lang.Short") ||
                clase.getName().equals("java.lang.Byte") ||
                clase.getName().equals("java.lang.Character") ||
                
            
            clase.getName().equals("java.lang.String") ||
            clase.getName().equals("java.lang.Boolean"))
            verificado = true;

        return verificado;
    }



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
            final int pos = i;
            final Class argument = parameterTypes[i];
            if (!argument.isPrimitive() && verificarDato(argument) == false) {
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

                        if (cadena[0].equals("Crear")) {

                            boolean esColeccion = false;

                            boolean esArreglo = false;

                            listWidget.setGuardado(false);

                            Method metodo = getActualMethod();

                            ArrayList<Class> classInstances;

                            ArrayList<Object> objectInstances;

                            esColeccion = argumentoEsColeccion(argument);

                            esArreglo = argumentoEsArreglo(argument);

                            Class clase;


                            if (esColeccion == true) {

                                classInstances = new ArrayList<Class>();
                                
                                objectInstances = new ArrayList<Object>();

                                Type[] genericParameterTypes = metodo.getGenericParameterTypes();

                                Type genericParameterType = genericParameterTypes[pos];

                                if (genericParameterType instanceof ParameterizedType) {

                                    ParameterizedType aType = (ParameterizedType) genericParameterType;

                                    Type[] parameterArgTypes = aType.getActualTypeArguments();

                                    for (Type parameterArgType : parameterArgTypes) {

                                        Class parameterArgClass = (Class) parameterArgType;

                                        classInstances.add(parameterArgClass);

                                    }
                                }

                                if (classInstances.size() == 2) {
                                    
                                    try {
                                        InstanceMapForm editorMap = new InstanceMapForm(classInstances, inicio.getDirectorioCasoPrueba().getPath(), listWidget, argument);
                                    } catch (InstantiationException ex) {
                                        Logger.getLogger(CaseTestEditor.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                    

                                }
                                else {

                                    if (classInstances.size() == 1){

                                        if (!classInstances.get(0).isPrimitive()
                                        && verificarDato(classInstances.get(0)) == false)
                                        {
                                            try {
                                                Object claseInstance = getInstance(classInstances.get(0));
                                                objectInstances.add(claseInstance);

                                                if (listWidget.getColeccion() != null
                                                        || listWidget.getMapa() != null) {
                                                    listWidget.getColeccion().clear();
                                                    listWidget.getMapa().clear();
                                                }

                                                InstanceListForm editorList = new InstanceListForm(objectInstances, inicio.getDirectorioCasoPrueba().getPath(), listWidget, argument);

                                                editorList.Visible();

                                                editorList.getColeccion();
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
                                            } catch (JDOMException ex) {
                                                Logger.getLogger(CaseTestEditor.class.getName()).log(Level.SEVERE, null, ex);
                                            } catch (IOException ex) {
                                                Logger.getLogger(CaseTestEditor.class.getName()).log(Level.SEVERE, null, ex);
                                            }

                                        }else{

                                            if (classInstances.get(0).isPrimitive()
                                                    || verificarDato(classInstances.get(0)) == true) {

                                                InstanceListForm editorList = new InstanceListForm(classInstances.get(0), listWidget, argument);

                                            }
                                        }
                                    }
                                }


                                    

                                    
                                    
                                

                             
                                    
                               

                                /*String interfazArgumento = argumentoColeccion(argument);

                                if (interfazArgumento.equals("java.util.List") ||
                                        interfazArgumento.equals("java.util.Set") ||
                                        interfazArgumento.equals("java.util.Queue")) {

                                    if (listWidget.getGuardado() == true)

                                    editorList.getColeccion();
                                }
                                else {

                                    if (interfazArgumento.equals("java.util.Map")) {

                                        if (listWidget.getGuardado() == true)

                                        editorList.getMap();

                                    }

                                }*/

                            } else {

                                if (esArreglo == true) {

                                    Class arrayComponente = argument.getComponentType();

                                    if (!arrayComponente.isPrimitive() &&
                                            verificarDato(arrayComponente) == false)
                                    {
                                        InstanceArrayForm editorArray = new InstanceArrayForm(arrayComponente,inicio.getDirectorioCasoPrueba().getPath(), listWidget, inicio);

                                    }
                                    else
                                    {
                                        try {
                                            Object object = getInstance(arrayComponente);
                                            InstanceArrayForm editorArray = new InstanceArrayForm(object, inicio.getDirectorioCasoPrueba().getPath(), listWidget, inicio);
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
                                        } catch (JDOMException ex) {
                                            Logger.getLogger(CaseTestEditor.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (IOException ex) {
                                            Logger.getLogger(CaseTestEditor.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }


                                }
                                else {

                                    try {

                                        if (listWidget.getObject() != null)
                                        {
                                            listWidget.getObject().clear();
                                        }

                                        Object claseInstance = getInstance(argument);

                                        

                                        InstanceForm editorInstance = new InstanceForm(claseInstance, inicio.getDirectorioCasoPrueba().getPath(), listWidget, metodo, inicio);

                                        editorInstance.Visible();

                                       

                                        editorInstance.getObject();
                                            System.out.println(listWidget.getObject());

                                        addInstanceVariable();

                                        
                                    

                                        int row = tablaVariables.getSelectedRow();

                                    }
                                    catch (JDOMException ex) {

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
        dependencias = new javax.swing.JButton();
        newTestEscenario = new javax.swing.JButton();
        generar = new javax.swing.JButton();
        siguiente = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaMetodosRegistrados = new javax.swing.JTable();
        nombreEscenario = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        volver = new javax.swing.JButton();

        setBorder(null);
        setTitle("Case Test Editor");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Historial de Variables", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 12))); // NOI18N

        tablaVariables.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Variable", "Metodo", "Tipo Retorno"
            }
        ));
        tablaVariables.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaVariablesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablaVariables);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
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
                .addContainerGap(167, Short.MAX_VALUE))
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
        assertCondiciones.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccione una opcion...", "Igual", "No Igual", "Nulo", "No Nulo", "Verdadero", "Falso", "" }));
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
                .addGroup(panelAssertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelAssertLayout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addGroup(panelAssertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbAssertCondiciones, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbAssertVariables, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                            .addComponent(lbResultadoAssert, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)))
                    .addGroup(panelAssertLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbAssertMensaje)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAssertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAssertLayout.createSequentialGroup()
                        .addGroup(panelAssertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(resultadoAssert, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelAssertLayout.createSequentialGroup()
                                .addComponent(assertVariables, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5))
                            .addComponent(assertCondiciones, 0, 241, Short.MAX_VALUE))
                        .addGap(57, 57, 57))
                    .addGroup(panelAssertLayout.createSequentialGroup()
                        .addComponent(assertMensaje, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        panelAssertLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {lbAssertCondiciones, lbAssertMensaje, lbAssertVariables, lbResultadoAssert});

        panelAssertLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {assertCondiciones, assertMensaje, assertVariables, resultadoAssert});

        panelAssertLayout.setVerticalGroup(
            panelAssertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAssertLayout.createSequentialGroup()
                .addGap(10, 10, 10)
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
                .addGroup(panelAssertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(assertMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbAssertMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        panelAssertLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {assertCondiciones, assertMensaje, assertVariables, lbAssertCondiciones, lbAssertMensaje, lbAssertVariables, lbResultadoAssert, resultadoAssert});

        guardarBt.setText("Nuevo Metodo");
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
                    .addComponent(guardarBt, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelAssert, javax.swing.GroupLayout.Alignment.TRAILING, 0, 463, Short.MAX_VALUE)
                    .addComponent(panelTablaArgumentos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        panelMetodoInfoLayout.setVerticalGroup(
            panelMetodoInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMetodoInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelTablaArgumentos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(panelAssert, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(guardarBt)
                .addGap(23, 23, 23))
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
                .addGap(9, 9, 9)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(136, 136, 136)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dependencias.setText("Dependencias");
        dependencias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dependenciasActionPerformed(evt);
            }
        });

        newTestEscenario.setText("Nuevo Escenario");
        newTestEscenario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newTestEscenarioActionPerformed(evt);
            }
        });

        generar.setText("Generar");
        generar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generarActionPerformed(evt);
            }
        });

        siguiente.setText("Siguiente");
        siguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                siguienteActionPerformed(evt);
            }
        });

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jLabel5.setText("Nombre Escenario de Prueba :");

        volver.setText("Volver");
        volver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                volverActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelInicialLayout = new javax.swing.GroupLayout(panelInicial);
        panelInicial.setLayout(panelInicialLayout);
        panelInicialLayout.setHorizontalGroup(
            panelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInicialLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(panelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelInicialLayout.createSequentialGroup()
                        .addGroup(panelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(29, 29, 29))
                    .addGroup(panelInicialLayout.createSequentialGroup()
                        .addComponent(volver)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(panelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelInicialLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombreEscenario))
                    .addGroup(panelInicialLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(panelMetodoInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelInicialLayout.createSequentialGroup()
                                .addComponent(generar, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(newTestEscenario, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dependencias)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(siguiente)))))
                .addGap(73, 73, 73))
        );

        panelInicialLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jPanel1, jPanel2, jPanel4});

        panelInicialLayout.setVerticalGroup(
            panelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInicialLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(panelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelInicialLayout.createSequentialGroup()
                        .addGroup(panelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(nombreEscenario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelMetodoInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelInicialLayout.createSequentialGroup()
                        .addComponent(jPanel4, 0, 188, Short.MAX_VALUE)
                        .addGap(10, 10, 10)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(volver)
                    .addComponent(siguiente)
                    .addComponent(newTestEscenario)
                    .addComponent(dependencias)
                    .addComponent(generar, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51))
        );

        panelInicialLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jPanel1, jPanel2, jPanel4});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelInicial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelInicial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void assertCondicionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_assertCondicionesActionPerformed
}//GEN-LAST:event_assertCondicionesActionPerformed

    private void guardarBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarBtActionPerformed
        this.deshabilitarMetodos();

        Method method = this.getActualMethod();
        varId++;

        Metodo metodoActual = this.agregarMetodo(method, varId, this.getActualAssert(method), this.getArgumentos(method));

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
            if (assertCondiciones.getSelectedItem().equals("Elige una opcion")) {
                lbResultadoAssert.setEnabled(false);
                resultadoAssert.setEnabled(false);
            } else {
                lbResultadoAssert.setEnabled(false);
                resultadoAssert.setEnabled(false);
            }
        }
    }//GEN-LAST:event_assertCondicionesPopupMenuWillBecomeInvisible

    private void generarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generarActionPerformed

        XmlManager xmlManager = new XmlManager();

        xmlManager.setInicio(inicio);

        xmlManager.crearCasoPrueba(this.inicio.getNombreCasoPrueba(), escenariosPrueba);

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
                    deshabilitarMetodosData();

                } else {
                    cargarTablaArgumentos(metodoNombre);
                    habilitarMetodosData(metodoNombre);//ver orden
                    actualNameMethod = metodoNombre;
                }
            }
        }
    }//GEN-LAST:event_listaMetodosMouseClicked

    private void newTestEscenarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newTestEscenarioActionPerformed

        String escenario = this.cadenaSinBlancos(this.nombreEscenario.getText());
        EscenarioPrueba escenarioPrueba = new EscenarioPrueba(escenario);
        escenarioPrueba.setMetodos(metodosGuardados);
        escenariosPrueba.add(escenarioPrueba);

        varId = 0;
        metodosGuardados = new ArrayList<Metodo>();
        variablesGuardadas.clear();
        nombreEscenario.setText("");

        DefaultTableModel model = (DefaultTableModel) tablaVariables.getModel();
        model.setNumRows(0);

        DefaultTableModel model2 = (DefaultTableModel) tablaMetodosRegistrados.getModel();
        model2.setNumRows(0);

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

    private void volverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_volverActionPerformed
        varId = 0;
        metodosGuardados = new ArrayList<Metodo>();
        variablesGuardadas.clear();
        nombreEscenario.setText("");

        ArrayList<Class> clases = this.inicio.getClasesManager();

        this.inicio.caseTestToMethods(this, clases);
    }//GEN-LAST:event_volverActionPerformed

    private void dependenciasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dependenciasActionPerformed

    }//GEN-LAST:event_dependenciasActionPerformed

    private void tablaVariablesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaVariablesMouseClicked
        // TODO add your handling code here:
       /* if ( evt.getButton() == MouseEvent.BUTTON1) {
        if (evt.getClickCount() == 2)
        {
        if (tablaVariables.getSelectedColumn() == 0 && tablaVariables.getSelectedRow() == 0) {
        final JTextField text = (JTextField) tablaVariables.getComponentAt(evt.getX(), evt.getY());
        String actualVar = text.getText();
        Metodo metodo = null;

        for (Metodo metodo1 : metodosGuardados)
        {
        if (metodo1.getRetorno().getNombreVariable().equals(actualVar))
        {
        metodo = (Metodo) metodo1.clone();
        }
        }


        text.getDocument().addDocumentListener(new DocumentListener() {

        public void insertUpdate(DocumentEvent e) {
        System.out.println("camio");
        }

        public void removeUpdate(DocumentEvent e) {
        }

        public void changedUpdate(DocumentEvent e) {

        System.out.println("cambio!");

        }
        });
        } else {
        evt.consume();
        }

        }
        }

         */
    }//GEN-LAST:event_tablaVariablesMouseClicked

    private void siguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_siguienteActionPerformed
        
    }//GEN-LAST:event_siguienteActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox assertCondiciones;
    private javax.swing.JTextField assertMensaje;
    private javax.swing.JComboBox assertVariables;
    private javax.swing.JButton dependencias;
    private javax.swing.JButton generar;
    private javax.swing.JButton guardarBt;
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
    private javax.swing.JLabel lbAssertCondiciones;
    private javax.swing.JLabel lbAssertMensaje;
    private javax.swing.JLabel lbAssertVariables;
    private javax.swing.JLabel lbResultadoAssert;
    private javax.swing.JList listaMetodos;
    private javax.swing.JButton newTestEscenario;
    private javax.swing.JTextField nombreEscenario;
    private javax.swing.JPanel panelAssert;
    private javax.swing.JPanel panelInicial;
    private javax.swing.JPanel panelMetodoInfo;
    private javax.swing.JPanel panelTablaArgumentos;
    private javax.swing.JPopupMenu popMenuMetodos;
    private javax.swing.JTextField resultadoAssert;
    private javax.swing.JButton siguiente;
    private javax.swing.JTable tablaMetodosRegistrados;
    private javax.swing.JTable tablaVariables;
    private javax.swing.JButton volver;
    // End of variables declaration//GEN-END:variables

    public String cadenaSinBlancos(String sTexto) {
        String sCadenaSinBlancos = "";

        for (int x = 0; x < sTexto.length(); x++) {
            if (sTexto.charAt(x) != ' ') {
                sCadenaSinBlancos += sTexto.charAt(x);
            }
        }
        return sCadenaSinBlancos;
    }

    public ArrayList<File> getJarsRuta() {
        return this.inicio.getJarsRuta();
    }

    /**
     * FALTA LIMPIAR ASSERT VARIABLE EN ESTE METODO
     */
    public void disableAssert() {
        this.lbAssertVariables.setEnabled(false);
        this.assertVariables.setEnabled(false);

        this.lbAssertCondiciones.setEnabled(false);
        this.assertCondiciones.setEnabled(false);

        this.lbResultadoAssert.setEnabled(false);
        this.resultadoAssert.setEnabled(false);

        this.lbAssertMensaje.setEnabled(false);
        this.assertMensaje.setEnabled(false);

        this.limpiarFormulario();
    }

    public void limpiarFormulario() {

        assertMensaje.setText("");
        resultadoAssert.setText("");
        assertCondiciones.setSelectedItem("Seleccione una opcion...");
        this.repaint();
    }

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

    public AssertTest getActualAssert(Method method) {
        AssertTest assertion = null;

        if (!method.getReturnType().getName().equals("void")) {
            if (this.assertVariables.getSelectedItem() != null) {
                assertion = new AssertTest(assertMensaje.getText(),
                        assertVariables.getSelectedItem().toString(),
                        setAssertCondition(assertCondiciones.getSelectedItem().toString()));

                if (assertCondiciones.getSelectedItem().toString().equals("Igual")
                        || assertCondiciones.getSelectedItem().equals("No Igual")) {
                    assertion.setValorAssert(resultadoAssert.getText());
                }
            }
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
        Integer contSimple = 1;

        String valorComplejo = "";

        ArrayList<Argumento> argumentos = new ArrayList<Argumento>();

        Class[] parametros = method.getParameterTypes();

        for (Class clazz : parametros) {
            Argumento argumento = new Argumento();

            argumento.setNombre("arg" + contSimple);
            argumento.setTipo(clazz.getName());

            if (clazz.getSimpleName().equals("String")) {
                argumento.setValor("\"" + tablaArgumentos.getValueAt(contSimple - 1, 1).toString() + "\"");
                argumento.setComplejo(false);
            } else if (clazz.getSimpleName().equals("char")) {
                argumento.setValor("\'" + tablaArgumentos.getValueAt(contSimple - 1, 1).toString() + "\'");
                argumento.setComplejo(false);
            } else if (!clazz.isPrimitive()) {
                String[] arregloCampos = clazz.getName().split("\\.");
                String primerCampo = arregloCampos[0];

                if (primerCampo.equals("java")) {
                    argumento.setValor(tablaArgumentos.getValueAt(contSimple - 1, 1).toString());
                    argumento.setComplejo(false);
                } else {
                    valorComplejo = "object" + contComplejo;
                    argumento.setValor(valorComplejo);
                    argumento.setComplejo(true);
                    contComplejo++;
                }
            } else {
                argumento.setValor(tablaArgumentos.getValueAt(contSimple - 1, 1).toString());
                argumento.setComplejo(false);
            }

            argumentos.add(argumento);
            contSimple++;
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
            }
        } catch (IOException ex) {
            Logger.getLogger(CaseTestEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
