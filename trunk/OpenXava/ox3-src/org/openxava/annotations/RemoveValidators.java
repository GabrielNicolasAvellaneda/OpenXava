package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * A group of @RemoveValidator associated to the same entity. <p>
 * 
 * Applies to entities.<p>
 * 
 * Example:
 * <pre>
 * @Entity
 * @RemoveValidators({
 *   @EntityValidator(ProductRemoveValidator.class),
 *   @EntityValidator(ProductNotUsedValidator.class)	
 * })
 * public class Product {
 * ...
 * </pre>
 * 
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface RemoveValidators {
	
	RemoveValidator [] value();
	
}
