package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * A group of @PropertyValidator associated to the same property. <p>
 * 
 * Applies to properties.<p>
 * 
 * Example:
 * <pre>
 * @PropertyValidators ({
 *		@PropertyValidator(validator=org.openxava.test.validators.ExcludeStringValidator.class, properties=
 *			@PropertyValue(name="string", value="MOTO")
 *		),
 *		@PropertyValidator(validator=org.openxava.test.validators.ExcludeStringValidator.class, properties=
 *			@PropertyValue(name="string", value="COCHE")
 *		),		
 *		@PropertyValidator(validator=org.openxava.test.validators.ExcludeStringValidator.class, properties=			
 *			@PropertyValue(name="string", value="CUATRE"),
 *			onlyOnCreate=true
 *		)		
 *	})
 *	private String description;
 * </pre>
 *
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface PropertyValidators {
	
	PropertyValidator [] value();
	
}
