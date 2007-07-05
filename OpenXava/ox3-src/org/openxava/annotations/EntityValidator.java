package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * 
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface EntityValidator {
	
	Class validator();
	PropertyValue [] properties() default {};
	boolean onlyOnCreate() default false;
	
}
