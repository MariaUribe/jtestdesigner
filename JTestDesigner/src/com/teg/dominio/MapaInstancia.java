/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teg.dominio;

import java.util.Map;

/**
 *
 * @author danielbello
 */
public class MapaInstancia {

    private Map mapa;

    private String nombreMapa;

    private Class claseKey;

    private Class claseValue;

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
     * @return the nombreMapa
     */
    public String getNombreMapa() {
        return nombreMapa;
    }

    /**
     * @param nombreMapa the nombreMapa to set
     */
    public void setNombreMapa(String nombreMapa) {
        this.nombreMapa = nombreMapa;
    }

    public MapaInstancia(){
        
    }

    /**
     * @return the claseKey
     */
    public Class getClaseKey() {
        return claseKey;
    }

    /**
     * @param claseKey the claseKey to set
     */
    public void setClaseKey(Class claseKey) {
        this.claseKey = claseKey;
    }

    /**
     * @return the claseValue
     */
    public Class getClaseValue() {
        return claseValue;
    }

    /**
     * @param claseValue the claseValue to set
     */
    public void setClaseValue(Class claseValue) {
        this.claseValue = claseValue;
    }

}
