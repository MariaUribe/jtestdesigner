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

}
