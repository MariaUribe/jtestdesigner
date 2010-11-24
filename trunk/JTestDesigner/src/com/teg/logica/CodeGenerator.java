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
            root.put("codeManager", codeManager);

            freemarkerDo(root, casoPrueba.getNombre());

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
    public static void freemarkerDo(Map datamodel, String nombreCasoPrueba) throws IOException, TemplateException {

        File folder = new File("src/com/teg/recursos/template/");
        Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading(folder.getAbsoluteFile());

        Template tpl = cfg.getTemplate("TemplateTest.ftl");

        File casoPruebaFile = new File(System.getProperty("user.home") + 
                System.getProperty("file.separator") + nombreCasoPrueba +
                System.getProperty("file.separator"));
        File src = new File(casoPruebaFile.getPath() + 
                System.getProperty("file.separator") + "src" +
                System.getProperty("file.separator"));

        OutputStream outputStream = new FileOutputStream(src.getPath() +
                System.getProperty("file.separator") + nombreCasoPrueba + ".java");
        OutputStreamWriter output = new OutputStreamWriter(outputStream);

        tpl.process(datamodel, output);
    }

}
