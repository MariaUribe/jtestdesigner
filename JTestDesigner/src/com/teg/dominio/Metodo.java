package com.teg.dominio;

import java.util.ArrayList;

/**
 *
 * @author maya
 */
public class Metodo {

    private String nombre;
    private String clase;
    private String claseSimpleName;
    private Retorno retorno;
    private ArrayList<Argumento> argumentos;
    private AssertTest assertLinea;

    /**
     * 
     * @param nombre the nombre to set
     * @param clase the clase to set
     */
    public Metodo(String nombre, String clase, String claseSimpleName) {
        this.nombre = nombre;
        this.clase = clase;
        this.claseSimpleName = claseSimpleName;
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
     * @return the clase
     */
    public String getClase() {
        return clase;
    }

    /**
     * @return the claseSimpleName
     */
    public String getClaseSimpleName() {
        return claseSimpleName;
    }

    /**
     * @param claseSimpleName the claseSimpleName to set
     */
    public void setClaseSimpleName(String claseSimpleName) {
        this.claseSimpleName = claseSimpleName;
    }

    /**
     * @param clase the clase to set
     */
    public void setClase(String clase) {
        this.clase = clase;
    }

    /**
     * @return the retorno
     */
    public Retorno getRetorno() {
        return retorno;
    }

    /**
     * @param retorno the retorno to set
     */
    public void setRetorno(Retorno retorno) {
        this.retorno = retorno;
    }

    /**
     * @return the argumentos
     */
    public ArrayList<Argumento> getArgumentos() {
        return argumentos;
    }

    /**
     * @param argumentos the argumentos to set
     */
    public void setArgumentos(ArrayList<Argumento> argumentos) {
        this.argumentos = argumentos;
    }

    /**
     * @return the assertLinea
     */
    public AssertTest getAssertLinea() {
        return assertLinea;
    }

    /**
     * @param assertLinea the assertLinea to set
     */
    public void setAssertLinea(AssertTest assertLinea) {
        this.assertLinea = assertLinea;
    }

}
