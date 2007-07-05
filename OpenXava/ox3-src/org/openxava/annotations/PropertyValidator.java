package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * 
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface PropertyValidator {
	
	Class validator();
	PropertyValue [] properties() default {};
	boolean onlyOnCreate() default false;
	
}
