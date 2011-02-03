package com.teg.dominio;

/**
 *
 * @author maya
 */
public class Retorno {

    private String retorno;

    private String retornoNombreSimple;

    private String nombreVariable;

    /**
     * 
     * @param retorno the retorno to set
     * @param nombreVariable the nombreVariable to set
     */
    public Retorno(String retorno, String retornoNombreSimple, String nombreVariable) {
        this.retorno = retorno;
        this.retornoNombreSimple = retornoNombreSimple;
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
     * @return the retornoNombreSimple
     */
    public String getRetornoNombreSimple() {
        return retornoNombreSimple;
    }

    /**
     * @param retornoNombreSimple the retornoNombreSimple to set
     */
    public void setRetornoNombreSimple(String retornoNombreSimple) {
        this.retornoNombreSimple = retornoNombreSimple;
    }

}
