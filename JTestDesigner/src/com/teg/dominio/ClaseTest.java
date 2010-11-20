/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teg.dominio;

/**
 *
 * @author maya
 */
public class ClaseTest {

    private String nombre;

    private String simpleNombre;

    public ClaseTest() {
    }

    public ClaseTest(String nombre, String simpleNombre) {
        this.nombre = nombre;
        this.simpleNombre = simpleNombre;
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
     * @return the simpleNombre
     */
    public String getSimpleNombre() {
        return simpleNombre;
    }

    /**
     * @param simpleNombre the simpleNombre to set
     */
    public void setSimpleNombre(String simpleNombre) {
        this.simpleNombre = simpleNombre;
    }

}
