package org.openxava.model.meta;

import java.util.*;

import org.openxava.model.meta.xmlparse.*;
import org.openxava.util.*;

/**
 * Utility class to access to default length of a type or stereotype. <p>
 * 
 * @author Javier Paniza
 */
public class DefaultSize {
		
	private static Map stereotypes;
	private static Map types;
	
	public static void _addForStereotype(String name, int length) throws XavaException {
		if (stereotypes == null) {
			throw new XavaException("only_from_parse", "DefaultSize._addForStereotype");
		}		
		stereotypes.put(name, new Integer(length));
	}
	
	public static void _addForType(String className, int length) throws XavaException {
		if (types == null) {
			throw new XavaException("only_from_parse", "DefaultSize._addForType");
		}			
		types.put(className, new Integer(length));
	}
	
	
	public static int forStereotype(String name) throws ElementNotFoundException, XavaException {
		if (stereotypes == null) {
			configure();
		}
		Integer result = (Integer) stereotypes.get(name);
		if (result == null) {
			throw new ElementNotFoundException("default_size_for_stereotype_not_found", name);
		}
		return result.intValue();
	}
	
	public static int forType(Class className) throws ElementNotFoundException, XavaException {
		if (types == null) {
			configure();
		}
		Integer result = (Integer) types.get(className.getName());
		if (result == null) {
			throw new ElementNotFoundException("default_size_for_type_not_found", className.getName());
		}
		return result.intValue();		
	}
	
	private static void configure() throws XavaException {
		stereotypes = new HashMap();
		types = new HashMap();
		DefaultSizeParser.configureDefaultSize();
	}
	
	
}
