package org.openxava.util;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import org.hibernate.*;

/**
 * Utility class to work with classes. <p>
 * 
 * @author: Javier Paniza
 */

public class Classes {
		
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
