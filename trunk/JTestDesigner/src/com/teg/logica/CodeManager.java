package com.teg.logica;

import com.teg.dominio.Argumento;
import com.teg.dominio.CasoPrueba;
import com.teg.dominio.EscenarioPrueba;
import com.teg.dominio.ClaseTest;
import com.teg.dominio.Metodo;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Clase para el manejo del codigo a imprimir en el template
 * @author maya
 */
public class CodeManager {

    private ArrayList<ClaseTest> classesNoRepetidas = new ArrayList<ClaseTest>();
    private ArrayList<ClaseTest> excepcionesNoRepetidas = new ArrayList<ClaseTest>();

    /**
     * Constructor por defecto
     */
    public CodeManager() {
    }

    public boolean esClaseEnvolvente(String tipoDato) {
        boolean esEnvolvente = false;
        String[] datoArray = tipoDato.split("\\.");
        String java = datoArray[0];
        String lang = datoArray[1];
        String nombre = datoArray[2];

        if (java.equals("java") && lang.equals("lang")) {
            if (nombre.equals("Integer") || nombre.equals("Byte") || nombre.equals("Float")
                    || nombre.equals("Character") || nombre.equals("Long") || nombre.equals("Short")
                    || nombre.equals("Double") || nombre.equals("Boolean") || nombre.equals("Void")) {
                esEnvolvente = true;
            }
        } else {
            esEnvolvente = false;
        }

        return esEnvolvente;
    }

    public String getRuta(CasoPrueba casoPrueba, String nombreClase) {
        String ruta = "";

        File casoPruebaFile = new File(System.getProperty("user.home")
                + System.getProperty("file.separator") + casoPrueba.getNombre()
                + System.getProperty("file.separator"));

        File metadata = new File(casoPruebaFile.getPath()
                + System.getProperty("file.separator") + "metadata"
                + System.getProperty("file.separator"));

        ruta = metadata.getPath()
                + System.getProperty("file.separator") + nombreClase + ".xml";

        return ruta;
    }

    public boolean escenarioVacio(EscenarioPrueba escenario) {
        boolean isEmpty = true;

        ArrayList<ClaseTest> excepciones = this.generarExcepciones(escenario);
        if (excepciones.isEmpty()) {
            isEmpty = true;
        } else {
            isEmpty = false;
        }

        return isEmpty;
    }

    /**
     * Metodo para verificar la existencia de una excepcion
     * @param ex la excepcion a comprobar
     * @return true si existe, false de lo contrario
     */
    public Boolean existeExcepcion(String ex) {

        Boolean flag = Boolean.FALSE;

        for (ClaseTest e : excepcionesNoRepetidas) {

            if (ex.equals(e.getNombre())) {
                flag = Boolean.TRUE;
                break;
            } else {
                flag = Boolean.FALSE;
            }
        }
        return flag;
    }

    /**
     * Metodo para generar excepciones no repetidas
     * @param excepciones las excepciones a ser filtradas
     * @return ArrayList<ClaseTest> arraylist con las excepciones no repetidas
     */
    public ArrayList<ClaseTest> excepcionesNoRepetidas(ArrayList<ClaseTest> excepciones) {

        for (ClaseTest ex : excepciones) {
            if (excepcionesNoRepetidas.isEmpty()) {

                excepcionesNoRepetidas.add(ex);

            } else {
                if (!existeExcepcion(ex.getNombre())) {
                    excepcionesNoRepetidas.add(ex);
                }
            }
        }

        return excepcionesNoRepetidas;
    }

    /**
     * Metodo para generar las excepciones dado un escenario de prueba
     * @param escenario el escenario a generar excepciones
     * @return ArrayList<ClaseTest> arraylist con las excepciones 
     */
    public ArrayList<ClaseTest> generarExcepciones(EscenarioPrueba escenario) {

        ArrayList<ClaseTest> excepciones = new ArrayList<ClaseTest>();
        ArrayList<ClaseTest> exs = new ArrayList<ClaseTest>();
        ArrayList<Metodo> metodos = escenario.getMetodos();

        for (Metodo metodo : metodos) {
            ArrayList<ClaseTest> methodExcepcions = metodo.getExcepciones();
            for (ClaseTest excepcion : methodExcepcions) {
                excepciones.add(excepcion);
            }
        }

        if (!excepciones.isEmpty()) {
            exs = this.excepcionesNoRepetidas(excepciones);
        }
        return exs;
    }

    /**
     * Metodo para obtener las clases a la que pertenecen los metodos
     * dado el caso de prueba
     * @param escenarioPrueba el caso de prueba a examinar
     * @return arreglo de las clases del caso de prueba
     */
    public ArrayList<ClaseTest> getClases(EscenarioPrueba escenarioPrueba) {

        ArrayList<Metodo> metodos = escenarioPrueba.getMetodos();

        ArrayList<ClaseTest> clases = new ArrayList<ClaseTest>();

        for (Metodo metodo : metodos) {
            clases.add(new ClaseTest(metodo.getClase().getNombre(), metodo.getClase().getSimpleNombre()));
        }

        return clases;
    }

    /**
     * Metodo para verificar la existencia de una clase
     * @param clase la clase a comprobar
     * @return true existe, false no existe
     */
    public Boolean existeClase(String clase) {

        Boolean flag = Boolean.FALSE;

        for (ClaseTest clazz : classesNoRepetidas) {

            if (clase.equals(clazz.getNombre())) {
                flag = Boolean.TRUE;
                break;
            } else {
                flag = Boolean.FALSE;
            }
        }
        return flag;
    }

    /**
     * Metodo para obtener las clases no repetidas
     * @param casoPrueba el caso de prueba a examinar
     * @return arreglo con las clases no repetidas
     */
    public ArrayList<ClaseTest> clasesNoRepetidas(CasoPrueba casoPrueba) {

        ArrayList<ClaseTest> clases = new ArrayList<ClaseTest>();

        for (EscenarioPrueba escenario : casoPrueba.getEscenariosPrueba()) {
            clases = this.getClases(escenario);

            for (ClaseTest clase : clases) {

                if (classesNoRepetidas.isEmpty()) {

                    classesNoRepetidas.add(clase);

                } else {
                    if (!existeClase(clase.getNombre())) {
                        classesNoRepetidas.add(clase);
                    }
                }
            }
        }
        return classesNoRepetidas;
    }

    /**
     * Metodo para dividir la lista obteniendo una mas reducida en la cual se
     * hara la comparacion de metodos y argumentos
     * @param metodos la lista completa de metodos a probar
     * @param metodo el metodo que sera la division de la lista
     * @return ArrayList<Metodo> lista de metodos dividida
     */
    public ArrayList<Metodo> dividirLista(ArrayList<Metodo> metodos, Metodo metodo) {

        ArrayList<Metodo> metodosDivididos = new ArrayList<Metodo>();

        for (Metodo method : metodos) {
            if (this.sonMetodosIguales(method, metodo)) {
                break;
            } else {
                metodosDivididos.add(method);
            }
        }

        return metodos;
    }

    /**
     * Metodo para verificar si el argumento arg se encuentra en la lista de
     * metodos
     * @param arg argumento a verificar
     * @param metodos metodos a comparar
     * @return Metodo metodo que es llamado para asignar el valor del argumento
     */
    public Metodo getMetodoEnLista(Argumento arg, ArrayList<Metodo> metodos) {

        Metodo metodo = null;
        String[] argValue = arg.getValor().split("\\.");
        String argumento = argValue[0];

        for (Metodo method : metodos) {
            if (method.getRetorno().getNombreVariable().equals(argumento)) {
                metodo = method;
                break;
            }
        }
        return metodo;
    }

    /*
     * verificar si funciona
     */
    public Boolean sonArgumentosIguales(Method method1, Method method2) {

        Boolean iguales = Boolean.FALSE;
        Class[] args1 = method1.getParameterTypes();
        Class[] args2 = method2.getParameterTypes();

        if ((method1.getParameterTypes() == null) && (method2.getParameterTypes() == null)) {
            iguales = Boolean.TRUE;
        } else if (args1.length == args2.length) {

            for (int i = 0; i < args1.length; i++) {
                if (args1[i].getName().equals(args2[i].getName())) {
                    iguales = Boolean.TRUE;
                } else {
                    iguales = Boolean.FALSE;
                    break;
                }
            }
        }
        return iguales;
    }

    public Boolean sonMetodosIguales(Method method1, Method method2) {

        Boolean sonIguales = Boolean.FALSE;

        // si pertenecen a la misma clase
        if (method1.getDeclaringClass().getName().equals(method2.getDeclaringClass().getName())) {
            // si tienen el mismo nombre y los argumentos son iguales
            if ((method1.getName().equals(method2.getName())) && (sonArgumentosIguales(method1, method2))) {
                sonIguales = Boolean.TRUE;
            }
        }

        return sonIguales;
    }

    /*
     * verificar si funciona!!!
     */
    public Boolean sonArgumentosIguales(Method method, Metodo metodo) {

        Boolean iguales = Boolean.FALSE;
        Class[] args1 = method.getParameterTypes();
        ArrayList<Argumento> args2 = metodo.getArgumentos();

        if ((method.getParameterTypes() == null) && (metodo.getArgumentos().isEmpty())) {
            iguales = Boolean.TRUE;
        } else if (args1.length == args2.size()) {

            for (int i = 0; i < args1.length; i++) {
                if (args1[i].getName().equals(args2.get(i).getNombre())) {
                    iguales = Boolean.TRUE;
                } else {
                    iguales = Boolean.FALSE;
                    break;
                }
            }
        }
        return iguales;
    }

    public Boolean sonMetodosIguales(Method method, Metodo metodo) {

        Boolean sonIguales = Boolean.FALSE;

        // si pertenecen a la misma clase
        if (method.getDeclaringClass().getName().equals(metodo.getClase().getNombre())) {
            // si tienen el mismo nombre y los argumentos son iguales
            if ((method.getName().equals(metodo.getNombre())) && (sonArgumentosIguales(method, metodo))) {
                sonIguales = Boolean.TRUE;
            }
        }

        return sonIguales;
    }

    public Boolean sonArgumentosIguales(Metodo metodo1, Metodo metodo2) {

        Boolean iguales = Boolean.FALSE;
        ArrayList<Argumento> args1 = metodo1.getArgumentos();
        ArrayList<Argumento> args2 = metodo2.getArgumentos();

        if ((metodo1.getArgumentos().isEmpty()) && (metodo2.getArgumentos().isEmpty())) {
            iguales = Boolean.TRUE;
        } else if (args1.size() == args2.size()) {

            for (int i = 0; i < args1.size(); i++) {
                if (args1.get(i).getTipo().equals(args2.get(i).getTipo())) {
                    iguales = Boolean.TRUE;
                } else {
                    iguales = Boolean.FALSE;
                    break;
                }
            }
        }
        return iguales;
    }

    public Boolean sonMetodosIguales(Metodo metodo1, Metodo metodo2) {

        Boolean sonIguales = Boolean.FALSE;

        // si pertenecen a la misma clase
        if (metodo1.getClase().equals(metodo2.getClase())) {
            // si tienen el mismo nombre y los argumentos son iguales
            if ((metodo1.getNombre().equals(metodo2.getNombre())) && (sonArgumentosIguales(metodo1, metodo2))) {
                sonIguales = Boolean.TRUE;
            }
        }
        return sonIguales;
    }

    public Boolean isMetodoEnLista(Metodo metodo, ArrayList<Metodo> metodos) {

        Boolean flag = Boolean.FALSE;

        for (Metodo method : metodos) {
            if (sonMetodosIguales(method, metodo)) {
                flag = Boolean.TRUE;
                break;
            }
        }
        return flag;
    }

    /**
     * Metodo para generarPrueba un ArrayList que contendra los metodos involucrados
     * para la ejecucion del Metodo metodo
     * @param metodo el metodo a crear posibles dependencias
     * @param metodosDividos lista con los metodos divididos
     * @param ordenMetodos arraylist que contendra los metodos involucrados
     * @return ArrayList<Metodo> que contendra los metodos involucrados
     * para la ejecucion del Metodo metodo
     */
    public ArrayList<Metodo> generarLinea(Metodo metodo, ArrayList<Metodo> metodosDividos, ArrayList<Metodo> ordenMetodos) {

        ArrayList<Argumento> argumentos = metodo.getArgumentos();

        for (Argumento argumento : argumentos) {

            Metodo newMetodo = this.getMetodoEnLista(argumento, metodosDividos);

            if (newMetodo != null) {
                if (!isMetodoEnLista(newMetodo, ordenMetodos)) {
                    ordenMetodos = generarLinea(newMetodo, this.dividirLista(metodosDividos, newMetodo), ordenMetodos);
                    ordenMetodos.add(newMetodo);
                }
            }
        }
        return ordenMetodos;
    }

    /**
     * Metodo para generar un escenario de prueba
     * @param casoPrueba caso de prueba para obtener los metodos que seran
     * @param escenarioActual escenario en el que se esta generando la prueba
     * @return ArrayList<Metodo> metodos involucrados para crear el escenario
     * de prueba del Metodo metodo
     */
    public ArrayList<Metodo> generarEscenario(CasoPrueba casoPrueba, EscenarioPrueba escenarioActual) {

        ArrayList<EscenarioPrueba> escenarios = casoPrueba.getEscenariosPrueba();

        ArrayList<Metodo> ordenMetodos = new ArrayList<Metodo>();

        ArrayList<Metodo> metodos = new ArrayList<Metodo>();

        for (EscenarioPrueba escenarioPrueba : escenarios) {
            if (escenarioActual.getNombre().equals(escenarioPrueba.getNombre())) {

                ArrayList<Metodo> metodosEscenario = escenarioPrueba.getMetodos();

                for (Metodo m : metodosEscenario) {
                    metodos.add(m);
                }
            }
        }

        for (Metodo metodo : metodos) {
            ArrayList<Metodo> metodosDivididos = this.dividirLista(metodos, metodo);
            ordenMetodos = this.generarLinea(metodo, metodosDivididos, ordenMetodos);
            ordenMetodos.add(metodo);
        }

        return ordenMetodos;
    }

    /**
     * Metodo para la generacion de la prueba
     * @param casoPrueba el caso de prueba donde se encuentran los disntintos
     * escenarios
     * @param escenarioActual el escenario de prueba actual
     * @return Arraylist<Metodo> el orden de los metodos para generar la prueba
     */
    public ArrayList<Metodo> generarPrueba(CasoPrueba casoPrueba, EscenarioPrueba escenarioActual) {
        ArrayList<Metodo> ordenMetodos = new ArrayList<Metodo>();

        ordenMetodos = this.generarEscenario(casoPrueba, escenarioActual);

        return ordenMetodos;
    }
}
