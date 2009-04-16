package org.openxava.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created on 27/03/2009
 * @autor Ana Andres
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface OnSelectedElementAction {
	
	String forViews() default "";
	
	String notForViews() default "";
	
	String value();
	
}
