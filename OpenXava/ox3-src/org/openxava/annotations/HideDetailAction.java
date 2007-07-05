package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * 
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface HideDetailAction {
	
	String forViews() default "";
	String notForViews() default "";
	String name();
	
}
