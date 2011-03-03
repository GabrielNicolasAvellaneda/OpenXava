package org.openxava.invoicing.annotations;  

import java.lang.annotation.*;
import org.hibernate.validator.*;
import org.openxava.invoicing.validators.*;

@ValidatorClass(ISBNValidator.class)  
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ISBN {  
	
	boolean search() default true;  	
	String message() default "ISBN does not exist";  
									
}