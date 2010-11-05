package com.teg.dominio;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class ClaseTemplate {

    private String nombreClase;

    private String nombrePaquete;

    private ArrayList<Field> camposClase = new ArrayList<Field>();

   /**
    * 
    */
    public ClaseTemplate() {
    }

    /**
     * @return the nombreClase
     */
    public String getNombreClase() {
        return nombreClase;
    }

    /**
     * @param nombreClase the nombreClase to set
     */
    public void setNombreClase(String nombreClase) {
        this.nombreClase = nombreClase;
    }
    
    /**
     * @return the metodosClase
     */
    public ArrayList<Field> getCamposClase() {
        return camposClase;
    }

    /**
     * @param metodosClase the metodosClase to set
     */
    public void setCamposClase(ArrayList<Field> camposClase) {
        this.camposClase = camposClase;
    }

    /**
     * @return the nombrePaquete
     */
    public String getNombrePaquete() {
        return nombrePaquete;
    }

    /**
     * @param nombrePaquete the nombrePaquete to set
     */
    public void setNombrePaquete(String nombrePaquete) {
        this.nombrePaquete = nombrePaquete;
    }

}
