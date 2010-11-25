package com.teg.logica;

import com.teg.dominio.Argumento;
import com.teg.dominio.CasoPrueba;
import com.teg.dominio.ClaseTest;
import com.teg.dominio.Metodo;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Clase para el manejo del codigo a imprimir en el template
 * @author maya
 */
public class CodeManager {

    private ArrayList<ClaseTest> classesNoRepetidas = new ArrayList<ClaseTest>();

    /**
     * Constructor por defecto
     */
    public CodeManager() {
    }

    /**
     * Metodo para obtener las clases a la que pertenecen los metodos
     * dado el caso de prueba
     * @param casoPrueba el caso de prueba a examinar
     * @return arreglo de las clases del caso de prueba
     */
    public ArrayList<ClaseTest> getClases(CasoPrueba casoPrueba) {

        ArrayList<Metodo> metodos = casoPrueba.getMetodos();

        ArrayList<ClaseTest> clases = new ArrayList<ClaseTest>();

        for (Metodo metodo : metodos) {
            clases.add(new ClaseTest(metodo.getClase(), metodo.getClaseSimpleName()));
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

        clases = this.getClases(casoPrueba);

        for (ClaseTest clase : clases) {

            if (classesNoRepetidas.isEmpty()) {

                classesNoRepetidas.add(clase);

            } else {
                if (!existeClase(clase.getNombre())) {
                    classesNoRepetidas.add(clase);
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
            if (method.getNombre().equals(metodo.getNombre())) {
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
     * Metodo para generar un ArrayList que contendra los metodos involucrados
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
     * Metodo para generar una Prueba
     * @param casoPrueba caso de prueba para obtener los metodos que seran
     * ejecutados en la prueba
     * @param metodo metodo al cual se generara el escenario de prueba
     * @return ArrayList<Metodo> metodos involucrados para crear el escenario
     * de prueba del Metodo metodo
     */
    public ArrayList<Metodo> generarPrueba(CasoPrueba casoPrueba, Metodo metodo) {

        ArrayList<Metodo> metodos = casoPrueba.getMetodos();

        ArrayList<Metodo> ordenMetodos = new ArrayList<Metodo>();

        ArrayList<Metodo> metodosDivididos = this.dividirLista(metodos, metodo);
        ordenMetodos = this.generarLinea(metodo, metodosDivididos, ordenMetodos);
        ordenMetodos.add(metodo);

        return ordenMetodos;
    }
}
