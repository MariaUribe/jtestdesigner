package com.teg.dominio;

/**
 *
 * @author maya
 */
public class AssertTest {

    private String mensaje;

    private String variable;

    private String condicion;

    private String valorAssert;

    public AssertTest() {
    }
    
    /**
     *
     * @param mensaje the mensaje to set
     * @param variable the variable to set
     * @param condicion the condicion to set
     */
    public AssertTest(String mensaje, String variable, String condicion) {
        this.mensaje = mensaje;
        this.variable = variable;
        this.condicion = condicion;

    }

    /**
     * @return the mensaje
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * @param mensaje the mensaje to set
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     * @return the variable
     */
    public String getVariable() {
        return variable;
    }

    /**
     * @param variable the variable to set
     */
    public void setVariable(String variable) {
        this.variable = variable;
    }

    /**
     * @return the condicion
     */
    public String getCondicion() {
        return condicion;
    }

    /**
     * @param condicion the condicion to set
     */
    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    /**
     * @return the valorAssert
     */
    public String getValorAssert() {
        return valorAssert;
    }

    /**
     * @param valorAssert the valorAssert to set
     */
    public void setValorAssert(String valorAssert) {
        this.valorAssert = valorAssert;
    }

}
