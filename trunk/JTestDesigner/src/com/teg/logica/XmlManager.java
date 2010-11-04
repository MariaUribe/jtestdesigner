package com.teg.logica;

import com.teg.dominio.Argumento;
import com.teg.dominio.AssertTest;
import com.teg.dominio.CasoPrueba;
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
 *
 * @author maya
 */
public class XmlManager {

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

    public void crearCasoPrueba(String nombreCasoPrueba, ArrayList<Metodo> metodos) {

        CasoPrueba casoPrueba = new CasoPrueba(nombreCasoPrueba);
        casoPrueba.setMetodos(metodos);

        this.crearXml(casoPrueba);

    }

    public ArrayList<Metodo> agregarMetodoALista(ArrayList<Metodo> metodos, Method method, Integer numVariable, AssertTest condAssert) {

        Metodo miMetodo = new Metodo(method.getName(), method.getDeclaringClass().getName(), method.getDeclaringClass().getSimpleName());
        miMetodo.setRetorno(new Retorno(method.getReturnType().getName(), "var" + numVariable));

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
}
