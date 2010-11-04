package com.teg.logica;

import com.teg.dominio.CasoPrueba;
import com.teg.dominio.ClaseTemplate;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maya
 */
public class CodeGenerator {

    private XmlManager xmlManager = new XmlManager();

    public void generateTest(String rutaXmlCasoPrueba) {

        CasoPrueba casoPrueba = xmlManager.getCasoPruebaXML(rutaXmlCasoPrueba);

        try {
            Map root = new HashMap();

            ClaseTemplate claseTemplate = new ClaseTemplate();
            claseTemplate.setNombreClase("MayaClase");
            claseTemplate.setClaseInstancia(casoPrueba.getMetodos().get(0).getClaseSimpleName());
            claseTemplate.setInstanciaMinusculas(casoPrueba.getMetodos().get(0).getClaseSimpleName());
            claseTemplate.setNombrePaquete("com.CodeGeneratorTest");

            root.put("claseTemplate", claseTemplate);
            freemarkerDo(root);

        } catch (IOException ex) {
            Logger.getLogger(CodeGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TemplateException ex) {
            Logger.getLogger(CodeGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public static void freemarkerDo(Map datamodel) throws IOException, TemplateException {
        File folder = new File("/home/maya/");

        Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading(folder);

        Template tpl = cfg.getTemplate("TemplateTest.txt");
        OutputStream outputStream = new FileOutputStream("/home/maya/PrimerIntento.java");
        OutputStreamWriter output = new OutputStreamWriter(outputStream);

        tpl.process(datamodel, output);
    }
}
