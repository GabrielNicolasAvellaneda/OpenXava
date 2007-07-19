package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * 
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface DisplaySize {
	
	String forViews() default "";
	String notForViews() default "";
	int value();
	
}
