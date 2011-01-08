package ${claseTemplate.nombrePaquete};

import org.testng.*;
import org.testng.annotations.*;
import static org.junit.Assert.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Test Class ${claseTemplate.nombreClase?cap_first}.java
 *
 * version 1.0
 */
public class ${claseTemplate.nombreClase?cap_first} {

<#list clasesNoRepetidas as clase>
	private ${clase.nombre} ${clase.simpleNombre?uncap_first};
</#list>
<#list casoPrueba.escenariosPrueba as esc>
    <#list esc.metodos as metodo>
        <#list metodo.argumentos as arg>
        <#assign esComplejo = arg.complejo />
        <#if esComplejo>
        private ${arg.tipo} ${arg.valor} = null;
        </#if>
        </#list>
    </#list>
</#list>

	public ${claseTemplate.nombreClase}Test() {
	}

	@Before
	public void setUp(){
	<#list clasesNoRepetidas as clase>
		${clase.simpleNombre?uncap_first} = new ${clase.nombre}();
	</#list>
    <#assign miCount = 0 />
    <#list casoPrueba.escenariosPrueba as esc>
        <#list esc.metodos as metodo>
            <#list metodo.argumentos as arg>
            <#assign esComplejo = arg.complejo />
            <#assign nombreClase = arg.tipo />
            <#if esComplejo>
            <#assign miCount = miCount + 1 />
            <#if miCount==1 >
            try {
            </#if>
            <#assign ruta = codeManager.getRuta(casoPrueba, nombreClase) />
                InputStream is = new FileInputStream("${ruta}");
                ${arg.valor} = (${arg.tipo}) xstream.fromXML(is);
            <#if miCount==1 >
            } catch (FileNotFoundException ex) {
                Logger.getLogger(${claseTemplate.nombreClase?cap_first}.class.getName()).log(Level.SEVERE, null, ex);
            }
            </#if>
            </#if>
            </#list>
        </#list>
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