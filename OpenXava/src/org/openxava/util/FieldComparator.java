package org.openxava.util;

import java.lang.reflect.*;
import java.util.*;

/**
 * Compare <code>java.lang.reflect.Field</code> by name 
 * 
 * @author Javier Paniza
 */
public class FieldComparator implements Comparator {
	
	final private static FieldComparator instance = new FieldComparator();
	
	// Use getInstance
	private FieldComparator() {		
	}

	public int compare(Object f1, Object f2) {
		if (f1 == f2) return 0;
		if (f1 == null) return -1;
		if (f2 == null) return 1;
		return ((Field) f1).getName().compareTo(((Field) f2).getName());
	}
	
	public static FieldComparator getInstance() {
		return instance;
	}

}
