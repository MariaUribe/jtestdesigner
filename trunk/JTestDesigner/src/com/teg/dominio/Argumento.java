package com.teg.dominio;

/**
 *
 * @author maya
 */
public class Argumento {

    private String nombre;
    
    private String tipo;

    /**
     *
     * @param nombre the nombre to set
     * @param tipo the tipo to set
     */
    public Argumento(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
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
