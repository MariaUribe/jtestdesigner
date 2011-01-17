/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teg.logica;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


        /**
 *
 * @author Daniel
 */
public class WidgetObjectLoading {

    private ArrayList<Object> object;
    private Collection coleccion;
    private Map mapa;
    private boolean guardado;
    private Object[] arreglo;


    /**
     * @return the object
     */
    public ArrayList<Object> getObject() {
        return object;
    }

    /**
     * @param object the object to set
     */
    public void setObject(ArrayList<Object> object) {
        this.object = object;
    }

   public WidgetObjectLoading()
    {

    }

    /**
     * @return the coleccion
     */
    public Collection getColeccion() {
        return coleccion;
    }

    /**
     * @param coleccion the coleccion to set
     */
    public void setColeccion(Collection coleccion) {
        this.coleccion = coleccion;
    }

    /**
     * @return the mapa
     */
    public Map getMapa() {
        return mapa;
    }

    /**
     * @param mapa the mapa to set
     */
    public void setMapa(Map mapa) {
        this.mapa = mapa;
    }

    /**
     * @return the guardado
     */
    public boolean getGuardado() {
        return guardado;
    }

    /**
     * @param guardado the guardado to set
     */
    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

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

    
    

}
