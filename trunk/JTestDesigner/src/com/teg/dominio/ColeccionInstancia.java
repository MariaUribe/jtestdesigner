/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teg.dominio;

import java.util.Collection;

/**
 *
 * @author danielbello
 */
public class ColeccionInstancia {

    private Collection coleccionInstancia;

    private String nombreColeccion;

    /**
     * @return the coleccionInstancia
     */
    public Collection getColeccionInstancia() {
        return coleccionInstancia;
    }

    /**
     * @param coleccionInstancia the coleccionInstancia to set
     */
    public void setColeccionInstancia(Collection coleccionInstancia) {
        this.coleccionInstancia = coleccionInstancia;
    }

    /**
     * @return the nombreColeccion
     */
    public String getNombreColeccion() {
        return nombreColeccion;
    }

    /**
     * @param nombreColeccion the nombreColeccion to set
     */
    public void setNombreColeccion(String nombreColeccion) {
        this.nombreColeccion = nombreColeccion;
    }

    public ColeccionInstancia()
    {
        
    }

}
