package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * 
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE })
public @interface RowStyle {
	
	String style();
	String property();
	String value();
	
}
