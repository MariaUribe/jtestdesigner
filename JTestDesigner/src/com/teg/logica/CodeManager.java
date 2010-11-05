package com.teg.logica;

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
}
