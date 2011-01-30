package com.teg.dominio;

import java.util.ArrayList;

/**
 *
 * @author maya
 */
public class CasoPrueba {

    private String nombre;
    
    private ArrayList<EscenarioPrueba> escenariosPrueba = new ArrayList<EscenarioPrueba>();

    private ArrayList<MockObject> mockObjects = new ArrayList<MockObject>();

    private boolean mock;

    public CasoPrueba() {
    }

    public CasoPrueba(String nombre) {
        this.nombre = nombre;
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
     * @return the escenariosPrueba
     */
    public ArrayList<EscenarioPrueba> getEscenariosPrueba() {
        return escenariosPrueba;
    }

    /**
     * @param escenariosPrueba the casosPrueba to set
     */
    public void setEscenariosPrueba(ArrayList<EscenarioPrueba> escenariosPrueba) {
        this.escenariosPrueba = escenariosPrueba;
    }

    /**
     * @return the mockObjects
     */
    public ArrayList<MockObject> getMockObjects() {
        return mockObjects;
    }

    /**
     * @param mockObjects the mockObjects to set
     */
    public void setMockObjects(ArrayList<MockObject> mockObjects) {
        this.mockObjects = mockObjects;
    }

    /**
     * @return the mock
     */
    public boolean isMock() {
        return mock;
    }

    /**
     * @param mock the mock to set
     */
    public void setMock(boolean mock) {
        this.mock = mock;
    }


}
