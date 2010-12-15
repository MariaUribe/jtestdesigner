package com.teg.dominio;

import java.util.ArrayList;

/**
 *
 * @author maya
 */
public class EscenarioPrueba {

    private String nombre;

    private ArrayList<Metodo> metodos;

    /**
     * 
     * @param nombre the nombre to set
     */
    public EscenarioPrueba(String nombre) {
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
     * @return the metodos
     */
    public ArrayList<Metodo> getMetodos() {
        return metodos;
    }

    /**
     * @param metodos the metodos to set
     */
    public void setMetodos(ArrayList<Metodo> metodos) {
        this.metodos = metodos;
    }

}
