package org.openxava.generators;

import java.util.*;

import org.openxava.generators.xmlparse.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;

/**
 * @author Javier Paniza
 */
public class GeneratorFactory {
	
	private static Map clases;
	
	/**
	 * 
	 * @param metaPropiedad
	 * @return IGeneradorCodigoPropiedad Nulo si no hay generador asociado
	 * @throws Exception
	 */
	public static IPropertyCodeGenerator create(MetaProperty metaPropiedad) throws Exception {
		if (!has(metaPropiedad)) return null;
		String clase = (String) clases.get(metaPropiedad.getStereotype());
		Object o = instanciar(clase);
		if (!(o instanceof IPropertyCodeGenerator)) {
			throw new XavaException("implements_required", clase, "IPropertyCodeGenerator");
		}
		IPropertyCodeGenerator generador = (IPropertyCodeGenerator) o;
		generador.setMetaProperty(metaPropiedad);
		return generador;		
	}
	
	public static boolean has(MetaProperty metaPropiedad) throws Exception {		
		configurar();
		return clases.containsKey(metaPropiedad.getStereotype());				
	}
	
	public static void _addForStereotype(String nombre, String clase) throws XavaException {
		if (clases == null) {
			throw new XavaException("only_from_parse", "GeneratorFactory._addForStereotype");
		}		
		clases.put(nombre, clase);
	}
	
	private static Object instanciar(String clase) throws Exception {
		return Class.forName(clase).newInstance();		
	}
	
	private static void configurar() throws XavaException {
		if (clases != null) return;
		clases = new HashMap();
		GeneratorsParser.configurarGeneradores();
	}

}
