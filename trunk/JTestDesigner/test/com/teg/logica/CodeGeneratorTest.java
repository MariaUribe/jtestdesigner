package com.teg.logica;

import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author maya
 */
public class CodeGeneratorTest {

    private CodeGenerator codeGenerator;

    public CodeGeneratorTest() {
    }

    @Before
    public void setUp() {
        codeGenerator = new CodeGenerator();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of generateTest method, of class CodeGenerator.
     */
    @Test
    public void testGenerateTest() {
        System.out.println("generateTest");
        String rutaXmlCasoPrueba = "/home/maya/miCasoPrueba.xml";
//        codeGenerator.generateTest(rutaXmlCasoPrueba);
    }

    /**
     * Test of freemarkerDo method, of class CodeGenerator.
     */
//    @Test
    public void testFreemarkerDo() throws Exception {
        System.out.println("freemarkerDo");
        Map datamodel = null;
        CodeGenerator.freemarkerDo(datamodel);
        fail("The test case is a prototype.");
    }

}