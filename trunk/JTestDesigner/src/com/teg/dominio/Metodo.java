package com.teg.dominio;

import java.util.ArrayList;

/**
 *
 * @author maya
 */
public class Metodo {

    private String nombre;
    private ClaseTest clase;
    private Retorno retorno;
    private ArrayList<Argumento> argumentos;
    private AssertTest assertLinea;
    private ArrayList<ClaseTest> excepciones;

    public Metodo() {
    }

    /**
     * 
     * @param nombre the nombre to set
     * @param clase the clase to set
     */
    public Metodo(String nombre, ClaseTest clase) {
        this.nombre = nombre;
        this.clase = clase;
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

    /**
     * @return the clasePerteneciente
     */
    public ClaseTest getClase() {
        return clase;
    }

    /**
     * @param clasePerteneciente the clasePerteneciente to set
     */
    public void setClase(ClaseTest clasePerteneciente) {
        this.clase = clasePerteneciente;
    }

    /**
     * @return the excepciones
     */
    public ArrayList<ClaseTest> getExcepciones() {
        return excepciones;
    }

    /**
     * @param excepciones the excepciones to set
     */
    public void setExcepciones(ArrayList<ClaseTest> excepciones) {
        this.excepciones = excepciones;
    }

}
