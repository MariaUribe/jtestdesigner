package com.teg.logica;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
        codeGenerator.generateTest(rutaXmlCasoPrueba);
    }

}