package com.teg.dominio;

import java.util.ArrayList;

/**
 *
 * @author maya
 */
public class GrupoCasoPrueba {

    private String nombre;

    private String nombrePaquete;

    private String directorio;
    
    private ArrayList<CasoPrueba> casosPrueba;

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    /**
     * @return the directorio
     */
    public String getDirectorio() {
        return directorio;
    }

    /**
     * @param directorio the directorio to set
     */
    public void setDirectorio(String directorio) {
        this.directorio = directorio;
    }

    /**
     * @return the casosPrueba
     */
    public ArrayList<CasoPrueba> getCasosPrueba() {
        return casosPrueba;
    }

    /**
     * @param casosPrueba the casosPrueba to set
     */
    public void setCasosPrueba(ArrayList<CasoPrueba> casosPrueba) {
        this.casosPrueba = casosPrueba;
    }

}
