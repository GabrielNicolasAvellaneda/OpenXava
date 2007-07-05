package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * 
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD }) 
public @interface DescriptionsList {
	
	String forViews() default "";
	String notForViews() default "";	
	String descriptionProperties() default "";
	String depends() default "";
	String condition() default "";
	boolean orderByKey() default false;
	String order() default "";
			
}
