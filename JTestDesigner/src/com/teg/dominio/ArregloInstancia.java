/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teg.dominio;

/**
 *
 * @author danielbello
 */
public class ArregloInstancia {

    private Object[] arreglo;

    private String nombreArreglo;

    private Class claseComponente;

    /**
     * @return the arreglo
     */
    public Object[] getArreglo() {
        return arreglo;
    }

    /**
     * @param arreglo the arreglo to set
     */
    public void setArreglo(Object[] arreglo) {
        this.arreglo = arreglo;
    }

    /**
     * @return the nombreArreglo
     */
    public String getNombreArreglo() {
        return nombreArreglo;
    }

    /**
     * @param nombreArreglo the nombreArreglo to set
     */
    public void setNombreArreglo(String nombreArreglo) {
        this.nombreArreglo = nombreArreglo;
    }

    public ArregloInstancia()
    {
        
    }

    /**
     * @return the claseComponente
     */
    public Class getClaseComponente() {
        return claseComponente;
    }

    /**
     * @param claseComponente the claseComponente to set
     */
    public void setClaseComponente(Class claseComponente) {
        this.claseComponente = claseComponente;
    }

}
