package com.teg.dominio;

import java.util.ArrayList;

/**
 *
 * @author maya
 */
public class Argumento {

    private String nombre;
    private String tipo;
    private String valor;
    private boolean complejo;
    private boolean arreglo;

    private ArrayList<MapaInstancia> mapasGuardados;

    private ArrayList<ColeccionInstancia> coleccionesGuardadas;

    private ArrayList<ArregloInstancia> arreglosGuardados;

    /**
     *
     * @param nombre the nombre to set
     * @param tipo the tipo to set
     */
    public Argumento(String nombre, String tipo, String valor, boolean complejo, boolean arreglo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
        this.complejo = complejo;
        this.arreglo = arreglo;
    }

    public Argumento() {
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
     * @return the valor
     */
    public String getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(String valor) {
        this.valor = valor;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the complejo
     */
    public boolean isComplejo() {
        return complejo;
    }

    /**
     * @param complejo the complejo to set
     */
    public void setComplejo(boolean complejo) {
        this.complejo = complejo;
    }

    /**
     * @return the arreglo
     */
    public boolean isArreglo() {
        return arreglo;
    }

    /**
     * @param arreglo the arreglo to set
     */
    public void setArreglo(boolean arreglo) {
        this.arreglo = arreglo;
    }
    
}
