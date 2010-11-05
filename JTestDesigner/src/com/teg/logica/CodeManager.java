package com.teg.logica;

import com.teg.dominio.CasoPrueba;
import com.teg.dominio.Metodo;
import java.util.ArrayList;

/**
 *
 * @author maya
 */
public class CodeManager {

    private ArrayList<String> classesNoRepetidas = new ArrayList<String>();

    public CodeManager() {
    }

    public ArrayList<String> getClases(CasoPrueba casoPrueba) {

        ArrayList<Metodo> metodos = casoPrueba.getMetodos();

        ArrayList<String> clases = new ArrayList<String>();

        for (Metodo metodo : metodos) {
            clases.add(metodo.getClaseSimpleName());
        }

        return clases;
    }

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
        for (String string : classesNoRepetidas) {
            System.out.println(string);
        }
        return classesNoRepetidas;
    }
}
