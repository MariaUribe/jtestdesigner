/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teg.dominio;

/**
 *
 * @author maya
 */

public class MockObject {
    
    private Metodo metodoSet;
    private String nombreVar;
    private String escenario;
    private String codigo;
    private Boolean imprimirMock;

    public MockObject() {
    }

    public MockObject(Metodo metodoSet, String nombreVar, String escenario, String codigo, Boolean imprimirMock) {
        this.metodoSet = metodoSet;
        this.nombreVar = nombreVar;
        this.escenario = escenario;
        this.codigo = codigo;
        this.imprimirMock = imprimirMock;
    }

    /**
     * @return the nombreVar
     */
    public String getNombreVar() {
        return nombreVar;
    }

    /**
     * @param nombreVar the nombreVar to set
     */
    public void setNombreVar(String nombreVar) {
        this.nombreVar = nombreVar;
    }

    /**
     * @return the metodoSet
     */
    public Metodo getMetodoSet() {
        return metodoSet;
    }

    /**
     * @param metodoSet the metodoSet to set
     */
    public void setMetodoSet(Metodo metodoSet) {
        this.metodoSet = metodoSet;
    }

    /**
     * @return the escenario
     */
    public String getEscenario() {
        return escenario;
    }

    /**
     * @param escenario the escenario to set
     */
    public void setEscenario(String escenario) {
        this.escenario = escenario;
    }

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the imprimirMock
     */
    public Boolean getImprimirMock() {
        return imprimirMock;
    }

    /**
     * @param imprimirMock the imprimirMock to set
     */
    public void setImprimirMock(Boolean imprimirMock) {
        this.imprimirMock = imprimirMock;
    }



}
