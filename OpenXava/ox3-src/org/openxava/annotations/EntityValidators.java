package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * A group of @EntityValidator associated to the same entity. <p>
 * 
 * Applies to entities.<p>
 * 
 * Example:
 * <pre>
 * @Entity
 * @EntityValidators({
 *	@EntityValidator(value=org.openxava.test.validators.CheapProductValidator.class, properties= {
 *		@PropertyValue(name="limit", value="100"),
 *		@PropertyValue(name="description"),
 *		@PropertyValue(name="unitPrice")
 *	}),
 *	@EntityValidator(value=org.openxava.test.validators.ExpensiveProductValidator.class, properties= {
 *		@PropertyValue(name="limit", value="1000"),
 *		@PropertyValue(name="description"),
 *		@PropertyValue(name="unitPrice")
 *	}),
 *	@EntityValidator(value=org.openxava.test.validators.ForbiddenPriceValidator.class, 
 *		properties= {
 *			@PropertyValue(name="forbiddenPrice", value="555"),
 *			@PropertyValue(name="unitPrice")
 *		},
 *		onlyOnCreate=true
 *	)	
 * })
 * public class Product {
 * ...
 * </pre>
 *
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface EntityValidators {
	
	EntityValidator [] value();
	
}
