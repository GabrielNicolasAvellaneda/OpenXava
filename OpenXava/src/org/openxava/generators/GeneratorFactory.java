package org.openxava.generators;

import java.util.*;

import org.openxava.generators.xmlparse.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;

/**
 * @author Javier Paniza
 */
public class GeneratorFactory {
	
	private static Map clasesEJB;
	private static Map clasesPOJO;
	
	public static IPropertyCodeGenerator create(MetaProperty metaProperty, boolean ejb) throws Exception {
		if (!has(metaProperty, ejb)) return null;
		Map clases = ejb?clasesEJB:clasesPOJO;
		String clase = (String) clases.get(metaProperty.getStereotype());
		Object o = instanciar(clase);
		if (!(o instanceof IPropertyCodeGenerator)) {
			throw new XavaException("implements_required", clase, "IPropertyCodeGenerator");
		}
		IPropertyCodeGenerator generador = (IPropertyCodeGenerator) o;
		generador.setMetaProperty(metaProperty);
		return generador;		
	}
	
	public static boolean has(MetaProperty metaPropiedad, boolean ejb) throws Exception {		
		configure();
		Map clases = ejb?clasesEJB:clasesPOJO;
		return clases.containsKey(metaPropiedad.getStereotype());				
	}
	
	public static void _addForStereotype(String name, String modelType, String className) throws XavaException {
		if (clasesEJB == null || clasesPOJO==null) {
			throw new XavaException("only_from_parse", "GeneratorFactory._addForStereotype");
		}		
		if ("ejb".equals(modelType)) {  
			clasesEJB.put(name, className);
		}
		else if ("pojo".equals(modelType)) {  
			clasesPOJO.put(name, className);
		}
		else {
			clasesEJB.put(name, className);
			clasesPOJO.put(name, className);
		}
	}
	
	private static Object instanciar(String className) throws Exception {
		return Class.forName(className).newInstance();		
	}
	
	private static void configure() throws XavaException {
		if (clasesEJB != null) return;
		clasesEJB = new HashMap();
		clasesPOJO = new HashMap();
		GeneratorsParser.configurarGeneradores();
	}

}
