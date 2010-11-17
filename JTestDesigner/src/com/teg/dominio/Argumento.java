package com.teg.dominio;

/**
 *
 * @author maya
 */
public class Argumento {

    private String nombre;
    private String tipo;
    private String valor;

    /**
     *
     * @param nombre the nombre to set
     * @param tipo the tipo to set
     */
    public Argumento(String nombre, String tipo, String valor) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
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
}
