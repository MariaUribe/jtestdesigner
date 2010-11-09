package com.teg.logica;

import com.teg.dominio.Argumento;
import com.teg.dominio.CasoPrueba;
import com.teg.dominio.ClaseTemplate;
import com.teg.dominio.Metodo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase para la generacion de codigo java 
 * @author maya
 */
public class CodeGenerator {

    private XmlManager xmlManager = new XmlManager();

    /**
     * Metodo para generar la prueba unitaria 
     * @param rutaXmlCasoPrueba la ruta en la cual se generara la clase
     */
    public void generateTest(String rutaXmlCasoPrueba) {

        CasoPrueba casoPrueba = xmlManager.getCasoPruebaXML(rutaXmlCasoPrueba);

        CodeManager codeManager = new CodeManager();

        try {
            Map root = new HashMap();

            ClaseTemplate claseTemplate = new ClaseTemplate();
            claseTemplate.setNombreClase(casoPrueba.getNombre());
            claseTemplate.setNombrePaquete("com.codeGeneratorTest");

            root.put("claseTemplate", claseTemplate);
            root.put("casoPrueba", casoPrueba);
            root.put("clasesNoRepetidas", codeManager.clasesNoRepetidas(casoPrueba));

            ArrayList<Metodo> metodos = casoPrueba.getMetodos();
            for (Metodo metodo : metodos) {
                ArrayList<Metodo> metodosLinea = this.generarPrueba(metodos, metodo);
            }

            freemarkerDo(root);

        } catch (IOException ex) {
            Logger.getLogger(CodeGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TemplateException ex) {
            Logger.getLogger(CodeGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metodo para crear la clase .java 
     * @param datamodel
     * @throws IOException
     * @throws TemplateException
     */
    public static void freemarkerDo(Map datamodel) throws IOException, TemplateException {

        File folder = new File("/home/maya/");
        Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading(folder);

        Template tpl = cfg.getTemplate("TemplateTest.txt");
        OutputStream outputStream = new FileOutputStream("/home/maya/PrimerIntento.java");
        OutputStreamWriter output = new OutputStreamWriter(outputStream);

        tpl.process(datamodel, output);
    }

    public ArrayList<Metodo> dividirLista(ArrayList<Metodo> metodos, Metodo metodo) {

        ArrayList<Metodo> metodosDivididos = new ArrayList<Metodo>();

        for (Metodo method : metodos) {
            if (method.getNombre().equals(metodo.getNombre())) {
                break;
            } else {
                metodosDivididos.add(method);
            }
        }

        return metodos;
    }

    public Metodo getMetodoEnLista(Argumento arg, ArrayList<Metodo> metodos) {
        Metodo metodo = null;
        String[] argValue = arg.getValor().split(".");
        String argumento = argValue[0];

        for (Metodo method : metodos) {
            if (method.getRetorno().getNombreVariable().equals(argumento)) {
                metodo = method;
                break;
            }
        }
        return metodo;
    }

    /**
     * TODO: falta que guarde el orden de los metodos a ejecutar
     * @param metodo
     * @param metodosDividos
     */
    public ArrayList<Metodo> generarLinea(Metodo metodo, ArrayList<Metodo> metodosDividos, ArrayList<Metodo> ordenMetodos) {

        ArrayList<Argumento> argumentos = metodo.getArgumentos();

        for (Argumento argumento : argumentos) {

            Metodo newMetodo = this.getMetodoEnLista(argumento, metodosDividos);

            if (newMetodo != null) {
                ordenMetodos.add(newMetodo);
                ordenMetodos = generarLinea(newMetodo, this.dividirLista(metodosDividos, newMetodo), ordenMetodos);
            }
        }

        return ordenMetodos;
    }

    public ArrayList<Metodo> generarPrueba(ArrayList<Metodo> metodos, Metodo metodo) {

        ArrayList<Metodo> ordenMetodos = new ArrayList<Metodo>();

        ordenMetodos.add(metodo);
        ArrayList<Metodo> metodosDivididos = this.dividirLista(metodos, metodo);
        ordenMetodos = this.generarLinea(metodo, metodosDivididos, ordenMetodos);


        return ordenMetodos;
    }
}
