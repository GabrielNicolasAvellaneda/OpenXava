package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * A hidden property has a meaning for the developer but not for the user. <p>
 * 
 * Applies to properties.<p>
 * 
 * The hidden properties are excluded when the automatic user interface is 
 * generated. However at Java code level they are present and fully functional. 
 * Even if you put it explicitly into a view the property will be shown in 
 * the user interface.<br>
 * Example:
 * <pre>
 * @Id @Hidden
 * @GeneratedValue(strategy=GenerationType.IDENTITY)
 * private Integer oid; 
 * </pre>
 * 
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface Hidden {
	
}
