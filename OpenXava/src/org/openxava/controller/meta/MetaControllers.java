package org.openxava.controller.meta;


import java.util.*;

import org.openxava.controller.meta.xmlparse.*;
import org.openxava.util.*;

public class MetaControllers {
		
	/** For context property */
	public final static String SWING="swing";
	/** For context property */
	public final static String WEB="web";
	
	private static Map environmentVariables;
	private static Map metaControllers;
	private static Map mapMetaObjects;
	private static String context = SWING;
	
	public static void _addMetaController(MetaController newController) throws XavaException {
		if (metaControllers == null) {
			throw new XavaException("only_from_parse", "MetaControllers._addMetaController");
		}
		metaControllers.put(newController.getName(), newController);
	}
	
	private static void setup() throws XavaException {
		metaControllers = new HashMap();
		ControllersParser.configureControllers(context);		
	}
	
	public static MetaController getMetaController(String name) throws ElementNotFoundException, XavaException {
		if (metaControllers == null) {
			setup();
		}
		MetaController result = (MetaController) metaControllers.get(name);
		if (result == null) {
			throw new ElementNotFoundException("controller_not_found", name);				
		}
		return result;
	}
	
	public static boolean contains(String name) throws XavaException {
		if (metaControllers == null) {
			setup();
		}
		return metaControllers.containsKey(name);
	}
	
	public static MetaAction getMetaAction(String qualifiedName) throws ElementNotFoundException, XavaException {
		if (metaControllers == null) {
			setup();
		}		
		if (qualifiedName == null) {
			throw new ElementNotFoundException("action_from_null_not_found");
		}
		if (qualifiedName.trim().equals("")) {
			throw new ElementNotFoundException("action_from_empty_string_not_found");
		}
		if (qualifiedName.indexOf('.') < 0) {
			throw new XavaException("only_qualified_action", qualifiedName);
		}
		StringTokenizer st = new StringTokenizer(qualifiedName, ".");
		String controller = st.nextToken().trim();
		String action = st.nextToken().trim();
		return getMetaController(controller).getMetaAction(action);
	}
	
	public static void addMetaObject(MetaObject object) {
		if (mapMetaObjects == null) mapMetaObjects = new HashMap();		
		mapMetaObjects.put(object.getName(), object);		
	}
	
	
	public static MetaObject getMetaObject(String name) throws ElementNotFoundException, XavaException {
		if (metaControllers == null) {
			setup();
		}						
		if (mapMetaObjects == null) throw new ElementNotFoundException("object_not_found", name);
		MetaObject a = (MetaObject) mapMetaObjects.get(name);
		if (a == null) throw new ElementNotFoundException("object_not_found", name);		
		return a; 
	}	
	
	public static String getContext() {
		return context;
	}

	public static void setContext(String string) {
		context = string;
	}
	
	public static void addEnvironmentVariable(String name, String value) {
		if (environmentVariables == null) environmentVariables = new HashMap();
		environmentVariables.put(name, value);
	}
	
	/**	 	
	 * @return Null if it does not exist.	 
	 */
	public static String getEnvironmentVariable(String name) throws XavaException {
		if (metaControllers == null) {
			setup();
		}
		if (environmentVariables == null) return null;
		return (String) environmentVariables.get(name);						
	}	
	
}


