package org.openxava.util;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * Utility class to work with classes. <p>
 * 
 * @author: Javier Paniza
 */

public class Classes {

	/**
	 * Returns the name of the class without package. <p>
	 * 
	 * It's the same of <code>Class.getSimpleName()</code> of Java 5 but 
	 * it works on Java 1.4. <br>
	 * If you code does not need to be Java 1.4 compliant you can use 
	 * directly the Class.getSimpleName() of Java 5.
	 */
	public static String getSimpleName(Class theClass) {  
		return Strings.lastToken(theClass.getName(), ".");		
	}
	
	/**
	 * If the class contains the exact method without argumetns. <p>
	 */
	public static boolean hasMethod(Class theClass, String method) { 
		try {
			theClass.getMethod(method, null);
			return true;
		}
		catch (NoSuchMethodException ex) {
			return false;
		}
	}
	
	/**
	 * All fields from all superclasess and including private, protected and public. 
	 * 
	 * @param theClass
	 * @return
	 */
	public static Collection<Field> getFieldsAnnotatedWith(Class theClass, Class<? extends Annotation> annotation) {
		Collection<Field> result = new ArrayList<Field>();
		fillFieldsAnnotatedWith(result, theClass, annotation);
		return result;
	}
	
	private static void fillFieldsAnnotatedWith(Collection<Field> result, Class theClass, Class<? extends Annotation> annotation) {
		if (Object.class.equals(theClass)) return;
		for (Field field: theClass.getDeclaredFields()) {
			if (field.isAnnotationPresent(annotation)) result.add(field);
		}
		fillFieldsAnnotatedWith(result, theClass.getSuperclass(), annotation);
	}
	
}
