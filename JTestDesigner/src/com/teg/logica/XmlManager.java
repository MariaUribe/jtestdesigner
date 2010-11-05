package com.teg.logica;

import com.teg.dominio.Argumento;
import com.teg.dominio.AssertTest;
import com.teg.dominio.CasoPrueba;
import com.teg.dominio.GrupoCasoPrueba;
import com.teg.dominio.Metodo;
import com.teg.dominio.Retorno;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase para el manejo del xml de los casos de pruebas 
 * @author maya
 */
public class XmlManager {

    /**
     * Metodo para crear el XML que contendra la informacion del caso de prueba
     * @param casoPrueba el casoPrueba a agregar
     */
    public void crearXml(CasoPrueba casoPrueba) {

        XStream xstream = new XStream(new DomDriver());
        xstream.alias("casoPrueba", CasoPrueba.class);
        xstream.alias("metodo", Metodo.class);
        xstream.alias("retorno", Retorno.class);
        xstream.alias("argumento", Argumento.class);
        xstream.alias("assert", AssertTest.class);

        String xml = xstream.toXML(casoPrueba);

        try {
            FileOutputStream fos = new FileOutputStream("/home/maya/" + casoPrueba.getNombre() + ".xml");
            xstream.toXML(casoPrueba, fos);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XmlManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metodo para obtener un casoPrueba a partir de un XML
     * @param rutaCasoPrueba la ruta donde se encuentra el xml
     * @return
     */
    public CasoPrueba getCasoPruebaXML(String rutaCasoPrueba) {

        XStream xstream = new XStream(new DomDriver());
        xstream.alias("casoPrueba", CasoPrueba.class);
        xstream.alias("metodo", Metodo.class);
        xstream.alias("retorno", Retorno.class);
        xstream.alias("argumento", Argumento.class);
        xstream.alias("assert", AssertTest.class);

        CasoPrueba casoPrueba = null;

        try {
            InputStream is = new FileInputStream(rutaCasoPrueba);
            casoPrueba = (CasoPrueba) xstream.fromXML(is);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XmlManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return casoPrueba;
    }

    /**
     * Metodo para crear un Caso de Prueba
     * @param nombreCasoPrueba el nombre del caso de prueba a crear
     * @param metodos los metodos a setear al caso de prueba
     */
    public void crearCasoPrueba(String nombreCasoPrueba, ArrayList<Metodo> metodos) {

        CasoPrueba casoPrueba = new CasoPrueba(nombreCasoPrueba);
        CodeGenerator cg = new CodeGenerator();
        casoPrueba.setMetodos(metodos);

        this.crearXml(casoPrueba);
        cg.generateTest("/home/maya/pruebaAnimal.xml");

    }

    /**
     * Metodo para agregar un metodo a la lista de metodos
     * @param metodos lista de metodos donde se agregara el metodo
     * @param method el metodo a setear
     * @param numVariable numero de la variable a crear
     * @param condAssert condicion de Assert para crear la variable
     * @return ArrayList<Metodo> la nueva lista de metodos con el metodo agregado 
     */
    public ArrayList<Metodo> agregarMetodoALista(ArrayList<Metodo> metodos,
            Method method, Integer numVariable, AssertTest condAssert) {

        Metodo miMetodo = new Metodo(method.getName(), method.getDeclaringClass().getName(),
                method.getDeclaringClass().getSimpleName());
        miMetodo.setRetorno(new Retorno(method.getReturnType().getName(),
                "var" + numVariable));

        ArrayList<Argumento> argumentos = new ArrayList<Argumento>();
        Class[] parameterTypes = method.getParameterTypes();
        Integer cont = 0;

        for (Class clazz : parameterTypes) {
            cont += 1;
            argumentos.add(new Argumento("arg" + cont, clazz.getName()));
        }

        miMetodo.setArgumentos(argumentos);
        miMetodo.setAssertLinea(condAssert);

        metodos.add(miMetodo);

        return metodos;
    }

    /**
     * Metodo para agregar un caso de prueba a un grupo de prueba
     * @param grupo el grupo al cual se agregara el caso de prueba
     * @param casoPrueba el caso de prueba a agregar
     * @return el nuevo Grupo con el caso de prueba seteado
     */
    public GrupoCasoPrueba agregarCasoAGrupo(GrupoCasoPrueba grupo, CasoPrueba casoPrueba){

        ArrayList<CasoPrueba> casosPrueba = grupo.getCasosPrueba();

        casosPrueba.add(casoPrueba);
        
        grupo.setCasosPrueba(casosPrueba);

        return grupo;

    }

}
