/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teg.util;

/**
 *
 * @author maya
 */
public class EscenarioPersonalizado {

    private String escenario;
    private Boolean escenarioHabilitado;

    public EscenarioPersonalizado() {
    }

    public EscenarioPersonalizado(String escenario, Boolean escenarioHabilitado) {
        this.escenario = escenario;
        this.escenarioHabilitado = escenarioHabilitado;
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
     * @return the escenarioHabilitado
     */
    public Boolean getEscenarioHabilitado() {
        return escenarioHabilitado;
    }

    /**
     * @param escenarioHabilitado the escenarioHabilitado to set
     */
    public void setEscenarioHabilitado(Boolean escenarioHabilitado) {
        this.escenarioHabilitado = escenarioHabilitado;
    }



}
