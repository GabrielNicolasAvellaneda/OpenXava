package org.openxava.controller.meta;


import java.util.*;

import org.openxava.controller.meta.xmlparse.*;
import org.openxava.util.*;

public class MetaControllers {
	
	
	/** Para propiedad subcontexto */
	public final static String SWING="swing";
	/** Para propiedad subcontexto */
	public final static String WEB="web";
	
	private static Map environmentVariables;
	private static Map metaControllers;
	private static Map mapMetaObjects;
	private static String context = SWING;
	
	/**
	 * 
	 * @param nuevo Nunca nulo
	 * @throws XavaException
	 */
	public static void _addMetaController(MetaController nuevo) throws XavaException {
		if (metaControllers == null) {
			throw new XavaException("only_from_parse", "MetaControllers._addMetaController");
		}
		metaControllers.put(nuevo.getName(), nuevo);
	}
	
	/**
	 */
	private static void setup() throws XavaException {
		metaControllers = new HashMap();
		ControllersParser.configurarControladores(context);		
	}
	
	/**
	 * 
	 * @param nombre java.lang.String
	 * @exception XavaException  Cualquier problema.
	 * @exception ElementNotFoundException
	 */
	public static MetaController getMetaController(String nombre) throws ElementNotFoundException, XavaException {
		if (metaControllers == null) {
			setup();
		}
		MetaController result = (MetaController) metaControllers.get(nombre);
		if (result == null) {
			throw new ElementNotFoundException("controller_not_found", nombre);				
		}
		return result;
	}
	
	public static boolean contains(String nombre) throws XavaException {
		if (metaControllers == null) {
			setup();
		}
		return metaControllers.containsKey(nombre);
	}
	
	public static MetaAction getMetaAction(String nombreCalificado) throws ElementNotFoundException, XavaException {
		if (metaControllers == null) {
			setup();
		}		
		if (nombreCalificado == null) {
			throw new ElementNotFoundException("action_from_null_not_found");
		}
		if (nombreCalificado.trim().equals("")) {
			throw new ElementNotFoundException("action_from_empty_string_not_found");
		}
		if (nombreCalificado.indexOf('.') < 0) {
			throw new XavaException("only_qualified_action", nombreCalificado);
		}
		StringTokenizer st = new StringTokenizer(nombreCalificado, ".");
		String controlador = st.nextToken().trim();
		String accion = st.nextToken().trim();
		return getMetaController(controlador).getMetaAction(accion);
	}
	
	public static void addMetaObject(MetaObject objeto) {
		if (mapMetaObjects == null) mapMetaObjects = new HashMap();		
		mapMetaObjects.put(objeto.getName(), objeto);		
	}
	
	
	public static MetaObject getMetaObject(String nombre) throws ElementNotFoundException, XavaException {
		if (metaControllers == null) {
			setup();
		}						
		if (mapMetaObjects == null) throw new ElementNotFoundException("object_not_found", nombre);
		MetaObject a = (MetaObject) mapMetaObjects.get(nombre);
		if (a == null) throw new ElementNotFoundException("object_not_found", nombre);		
		return a; 
	}	
	
	public static String getContext() {
		return context;
	}

	public static void setContext(String string) {
		context = string;
	}
	
	public static void addEnvironmentVariable(String nombre, String valor) {
		if (environmentVariables == null) environmentVariables = new HashMap();
		environmentVariables.put(nombre, valor);
	}
	
	/**	 	
	 * @return Nulo si no existe.	 
	 */
	public static String getEnvironmentVariable(String nombre) throws XavaException {
		if (metaControllers == null) {
			setup();
		}
		if (environmentVariables == null) return null;
		return (String) environmentVariables.get(nombre);						
	}	
	
}


