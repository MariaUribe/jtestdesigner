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

    private Class claseColeccion;

    private Class tipo;

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

    

    /**
     * @return the tipo
     */
    public Class getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(Class tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the claseColeccion
     */
    public Class getClaseColeccion() {
        return claseColeccion;
    }

    /**
     * @param claseColeccion the claseColeccion to set
     */
    public void setClaseColeccion(Class claseColeccion) {
        this.claseColeccion = claseColeccion;
    }

}
