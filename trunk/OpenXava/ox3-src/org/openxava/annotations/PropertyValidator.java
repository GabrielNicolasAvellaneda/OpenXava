package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * The validator execute validation logic on the value assigned to the property 
 * just before storing. <p>
 * 
 * Applies to properties. <p>
 * 
 * Example:
 * <pre>
 * @PropertyValidator(validator=org.openxava.test.validators.UnitPriceValidator.class)
 * private BigDecimal unitPrice;
 * </pre>
 * 
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface PropertyValidator {
	
	/**
	 * Class with the validation logic. <p>
	 * 
	 * Must implements {@link org.openxava.validators.IPropertyValidator}.
	 */
	Class validator();
	
	/**
	 * To set values to the validator properties before executing it.
	 */	
	PropertyValue [] properties() default {};
	
	/**
     * If true the validator is executed only when creating a new object,
     * not when an existing object is modified. 
	 */	
	boolean onlyOnCreate() default false;
	
}
