package ${claseTemplate.nombrePaquete};

import import org.testng.*;
import static org.junit.Assert.*;

/**
 * Test Class ${claseTemplate.nombreClase?cap_first}.java
 *
 * version 1.0
 */
public class ${claseTemplate.nombreClase?cap_first}Test {

<#list clasesNoRepetidas as clase>
	private ${clase.nombre} ${clase.simpleNombre?uncap_first};
</#list>

	public ${claseTemplate.nombreClase}Test() {
	}

	@Before
	public void setUp(){
	<#list clasesNoRepetidas as clase>
		${clase.simpleNombre?uncap_first} = new ${clase.nombre}();
	</#list>
	}

	@After
	public void tearDown() {
	}

    <#list casoPrueba.escenariosPrueba as escenario>
	/**
	 * Test of ${escenario.nombre}.
	 */
	@Test
	public void ${escenario.nombre}Test(){
        <#assign isEmpty = codeManager.escenarioVacio(escenario) />
        <#if isEmpty><#else>
        try {<#assign excepciones = codeManager.generarExcepciones(escenario) />
        </#if>

            <#assign ordenMetodos = codeManager.generarPrueba(casoPrueba, escenario) />
            <#list ordenMetodos as metodo>
            <#if metodo.assertLinea??>${metodo.retorno.retornoSimpleName} ${metodo.retorno.nombreVariable} = </#if>${metodo.clase.simpleNombre?uncap_first}.${metodo.getNombre()}(<#list metodo.argumentos as arg>${arg.valor}<#if arg_has_next>, </#if></#list>);
            <#if metodo.assertLinea??>${metodo.assertLinea.condicion}("${metodo.assertLinea.mensaje}",<#if metodo.assertLinea.valorAssert??> ${metodo.assertLinea.valorAssert}, </#if> ${metodo.assertLinea.variable});</#if>

            </#list>
         <#if isEmpty><#else>
         <#list excepciones as exception>
        } catch (${exception.nombre} ex) {
            fail(ex.getMessage());
        <#if !exception_has_next>}</#if>
        </#list>
        </#if>
	}
	
    </#list>
}