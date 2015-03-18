package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * Encapsulate the name and type of a conventional property Java. <p>
 * 
 * It is generally used in annotations that will generate bytecode. <p>
 *
 * @author Jeromy Altuna
 * @since  5.3
 */ 
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface Property {
	
	/**
	 * The name of the property.<p>
	 */
	String name();
	
	/**
	 * The type of the property.<p>
	 */
	Type type();
	public enum Type {		
		BOOLEAN("java.lang.Boolean"), SHORT("java.lang.Short"),		
		INTEGER("java.lang.Integer"), LONG("java.lang.Long"), 
		TIMESTAMP("java.sql.Timestamp");      

		private String classname;
		private Type(String classname) {
			this.classname = classname;
		}
		
		public String getClassname() {
			return classname;
		}
	}
}
