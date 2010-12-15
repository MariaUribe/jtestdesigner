package com.teg.dominio;

import java.util.ArrayList;

/**
 *
 * @author maya
 */
public class CasoPrueba {

    private String nombre;

    private String nombrePaquete;
    
    private ArrayList<EscenarioPrueba> escenariosPrueba;

    public CasoPrueba(String nombre) {
        this.nombre = nombre;
    }


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
     * @return the escenariosPrueba
     */
    public ArrayList<EscenarioPrueba> getEscenariosPrueba() {
        return escenariosPrueba;
    }

    /**
     * @param escenariosPrueba the casosPrueba to set
     */
    public void setEscenariosPrueba(ArrayList<EscenarioPrueba> escenariosPrueba) {
        this.escenariosPrueba = escenariosPrueba;
    }

}
