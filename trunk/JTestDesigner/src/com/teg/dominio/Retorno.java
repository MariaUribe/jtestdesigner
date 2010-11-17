package com.teg.dominio;

/**
 *
 * @author maya
 */
public class Retorno {

    private String retorno;

    private String retornoSimpleName;

    private String nombreVariable;

    /**
     * 
     * @param retorno the retorno to set
     * @param nombreVariable the nombreVariable to set
     */
    public Retorno(String retorno, String retornoSimpleName, String nombreVariable) {
        this.retorno = retorno;
        this.retornoSimpleName = retornoSimpleName;
        this.nombreVariable = nombreVariable;
    }
    
    /**
     * @return the retorno
     */
    public String getRetorno() {
        return retorno;
    }

    /**
     * @param retorno the retorno to set
     */
    public void setRetorno(String retorno) {
        this.retorno = retorno;
    }

    /**
     * @return the nombreVariable
     */
    public String getNombreVariable() {
        return nombreVariable;
    }

    /**
     * @param nombreVariable the nombreVariable to set
     */
    public void setNombreVariable(String nombreVariable) {
        this.nombreVariable = nombreVariable;
    }

    /**
     * @return the retornoSimpleName
     */
    public String getRetornoSimpleName() {
        return retornoSimpleName;
    }

    /**
     * @param retornoSimpleName the retornoSimpleName to set
     */
    public void setRetornoSimpleName(String retornoSimpleName) {
        this.retornoSimpleName = retornoSimpleName;
    }

}
