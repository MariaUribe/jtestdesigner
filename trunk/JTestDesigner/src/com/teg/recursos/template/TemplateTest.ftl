package ${claseTemplate.nombrePaquete};

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test Class ${claseTemplate.nombreClase?cap_first}.java
 *
 * version 1.0
 */
public class ${claseTemplate.nombreClase?cap_first}Test {

<#list clasesNoRepetidas as clase>
	private ${clase} ${clase?uncap_first};
</#list>

	public ${claseTemplate.nombreClase}Test() {
	}

	@Before
	public void setUp(){
	<#list clasesNoRepetidas as clase>
		${clase?uncap_first} = new ${clase}();
	</#list>
	}

	@After
	public void tearDown() {
	}

	<#list casoPrueba.metodos as method>
	/**
	 * Test of generateTest method, of class ${claseTemplate.nombreClase}.
	 */
	@Test
	public void testEscenario1(){
		<#assign ordenMetodos = codeManager.generarPrueba(casoPrueba, method) />

		<#list ordenMetodos as metodo>
			${metodo.getNombre()};
		</#list>
	}
	</#list>
}

