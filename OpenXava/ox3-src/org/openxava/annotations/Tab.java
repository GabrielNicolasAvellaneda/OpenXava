package org.openxava.annotations;

import java.lang.annotation.*;
import org.openxava.filters.*;

/**
 * 
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Tab {
	
	String name() default "";
	RowStyle [] rowStyles() default {};
	String properties() default "";
	Class filter() default VoidFilter.class;
	String baseCondition() default "";
	String defaultOrder() default "";
	
}
