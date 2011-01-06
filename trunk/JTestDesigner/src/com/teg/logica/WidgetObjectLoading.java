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

}
