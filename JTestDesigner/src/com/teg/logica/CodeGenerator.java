package com.teg.logica;

import com.teg.dominio.CasoPrueba;
import com.teg.dominio.ClaseTemplate;
import com.teg.vista.Inicio;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

/**
 * Clase para la generacion de codigo java 
 * @author maya
 */
public class CodeGenerator {

    private Inicio inicio;
    private XmlManager xmlManager = new XmlManager();

    public CodeGenerator() {
    }

    /**
     * Metodo para generarPrueba la prueba unitaria
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


            ArrayList<File> jars = this.getInicio().getJarsRuta();
            String rutaJava = this.getRutaJava(casoPrueba.getNombre());
            String rutaClass = this.getRutaClass(casoPrueba.getNombre());
            String rutaResultados = this.getRutaResultados(casoPrueba.getNombre());

            this.compileTest(jars, rutaJava, rutaClass);
            Class clase = this.setClassPath(rutaClass, jars, casoPrueba.getNombre());
            this.runTest(clase, rutaResultados);

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

        File casoPruebaFile = new File(System.getProperty("user.home")
                + System.getProperty("file.separator") + nombreCasoPrueba
                + System.getProperty("file.separator"));
        File src = new File(casoPruebaFile.getPath()
                + System.getProperty("file.separator") + "src"
                + System.getProperty("file.separator"));
        File com = new File(src.getPath()
                + System.getProperty("file.separator") + "com"
                + System.getProperty("file.separator"));
        File paquete = new File(com.getPath()
                + System.getProperty("file.separator") + "codeGeneratorTest"
                + System.getProperty("file.separator"));

        OutputStream outputStream = new FileOutputStream(paquete.getPath()
                + System.getProperty("file.separator") + nombreCasoPrueba + ".java");
        OutputStreamWriter output = new OutputStreamWriter(outputStream);

        tpl.process(datamodel, output);
    }

    public Class setClassPath(String rutaClass, ArrayList<File> jars, String nombreCaso) {
        Class clase = null;
        File classFile = new File(rutaClass);
        //File classFile = new File("/Users/maya/otroCaso/classes/");
        //File jar = new File("/Users/maya/Documents/TEG/Librerías/Prueba.jar");

        try {
            URL url = classFile.toURI().toURL();
            //URL jarUrl = jar.toURI().toURL();
            //URL[] urls = new URL[]{url, jarUrl};

            ArrayList<URL> arrayUrls = new ArrayList<URL>();

            arrayUrls.add(url);

            for (File file : jars) {
                URL jarURL = file.toURI().toURL();
                arrayUrls.add(jarURL);
            }

            URL[] urls = new URL[arrayUrls.size()];
            urls = arrayUrls.toArray(urls);


            URLClassLoader cl = new URLClassLoader(urls);
            clase = cl.loadClass("com.codeGeneratorTest." + nombreCaso);
            //clase = cl.loadClass("com.codeGeneratorTest.otroCaso");

            ClassPathHandler cph = new ClassPathHandler();
            cph.addURLs(urls);

        } catch (NoSuchMethodException ex) {
            Logger.getLogger(CodeGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CodeGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(CodeGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return clase;
    }

    public void compileTest(ArrayList<File> jars, String rutaJava, String rutaClass) {
        String jarList = "";
        int cont = 1;

        for (File file : jars) {
            if (cont == 1) {
                jarList = file.getPath();
            } else {
                jarList = jarList + ":" + file.getPath();
            }
            cont++;
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int compilationResult = compiler.run(null, null, null, "-classpath", jarList, rutaJava, "-d", rutaClass);
        //int compilationResult = compiler.run(null, null, null, "-classpath", "/Users/maya/Documents/TEG/Librerías/Prueba.jar", "/Users/maya/otroCaso/src/com/codeGeneratorTest/otroCaso.java", "-d", "/Users/maya/otroCaso/classes");

        if (compilationResult == 0) {
            System.out.println("Compilation is successful");
        } else {
            System.out.println("Compilation Failed");
        }
    }

    public void runTest(Class clase, String rutaResultados) {


        this.printActualClassPath();

        Class[] classes = {clase};
        TestListenerAdapter tla = new TestListenerAdapter();
        TestNG testNG = new TestNG();
        testNG.setTestClasses(classes);
        testNG.addListener(tla);
        testNG.setOutputDirectory(rutaResultados);
        //testNG.setOutputDirectory("/Users/maya/otroCaso/resultados");
        testNG.run();
    }

    public void printActualClassPath() {

        ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();

        URL[] urls = ((URLClassLoader) sysClassLoader).getURLs();
        System.out.println("System CLASSPATH: ");
        for (int i = 0; i < urls.length; i++) {
            System.out.println(urls[i].getFile());
        }
    }

    public String getRutaJava(String casoPrueba) {

        String rutaJava = "";

        File casoPruebaFile = new File(System.getProperty("user.home")
                + System.getProperty("file.separator") + casoPrueba
                + System.getProperty("file.separator"));
        File src = new File(casoPruebaFile.getPath()
                + System.getProperty("file.separator") + "src"
                + System.getProperty("file.separator"));
        File com = new File(src.getPath()
                + System.getProperty("file.separator") + "com"
                + System.getProperty("file.separator"));
        File paquete = new File(com.getPath()
                + System.getProperty("file.separator") + "codeGeneratorTest"
                + System.getProperty("file.separator"));
        File java = new File(paquete.getPath()
                + System.getProperty("file.separator") + casoPrueba + ".java");

        rutaJava = java.getPath();

        return rutaJava;
    }

    public String getRutaClass(String casoPrueba) {
        String rutaClass = "";

        File casoPruebaFile = new File(System.getProperty("user.home")
                + System.getProperty("file.separator") + casoPrueba
                + System.getProperty("file.separator"));
        File clases = new File(casoPruebaFile.getPath()
                + System.getProperty("file.separator") + "classes"
                + System.getProperty("file.separator"));

        rutaClass = clases.getPath();

        return rutaClass;
    }

    public String getRutaResultados(String casoPrueba) {
        String rutaClass = "";

        File casoPruebaFile = new File(System.getProperty("user.home")
                + System.getProperty("file.separator") + casoPrueba
                + System.getProperty("file.separator"));
        File resultados = new File(casoPruebaFile.getPath()
                + System.getProperty("file.separator") + "resultados"
                + System.getProperty("file.separator"));

        rutaClass = resultados.getPath();

        return rutaClass;
    }

    /**
     * @return the inicio
     */
    public Inicio getInicio() {
        return inicio;
    }

    /**
     * @param inicio the inicio to set
     */
    public void setInicio(Inicio inicio) {
        this.inicio = inicio;
    }
}
