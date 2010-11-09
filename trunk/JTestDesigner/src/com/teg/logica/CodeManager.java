package com.teg.logica;

import com.teg.dominio.Argumento;
import com.teg.dominio.CasoPrueba;
import com.teg.dominio.Metodo;
import java.util.ArrayList;

/**
 * Clase para el manejo del codigo a imprimir en el template
 * @author maya
 */
public class CodeManager {

    private ArrayList<String> classesNoRepetidas = new ArrayList<String>();

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
    public ArrayList<String> getClases(CasoPrueba casoPrueba) {

        ArrayList<Metodo> metodos = casoPrueba.getMetodos();

        ArrayList<String> clases = new ArrayList<String>();

        for (Metodo metodo : metodos) {
            clases.add(metodo.getClaseSimpleName());
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

        for (String clazz : classesNoRepetidas) {

            if (clase.equals(clazz)) {
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
    public ArrayList<String> clasesNoRepetidas(CasoPrueba casoPrueba) {

        ArrayList<String> clases = new ArrayList<String>();

        clases = this.getClases(casoPrueba);

        for (String clase : clases) {

            if (classesNoRepetidas.isEmpty()) {

                classesNoRepetidas.add(clase);

            } else {
                if (!existeClase(clase)) {
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
        String[] argValue = arg.getValor().split(".");
        String argumento = argValue[0];

        for (Metodo method : metodos) {
            if (method.getRetorno().getNombreVariable().equals(argumento)) {
                metodo = method;
                break;
            }
        }
        return metodo;
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
                ordenMetodos.add(newMetodo);
                ordenMetodos = generarLinea(newMetodo, this.dividirLista(metodosDividos, newMetodo), ordenMetodos);
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

        ordenMetodos.add(metodo);
//        ArrayList<Metodo> metodosDivididos = this.dividirLista(metodos, metodo);
//        ordenMetodos = this.generarLinea(metodo, metodosDivididos, ordenMetodos);

        return ordenMetodos;
    }
}
