package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * The <code>@RemoveValidator</code> is a level model validator, it is executed 
 * just before removing an object, and it has the possibility to deny the deletion. <p>
 * 
 * Applies to entities. <p>
 * 
 * Example:
 * <pre>
 * &nbsp;@RemoveValidator(value=DeliveryTypeRemoveValidator.class,
 * &nbsp;&nbsp;&nbsp;properties=@PropertyValue(name="number")
 * &nbsp;)
 * &nbsp;public class DeliveryType {
 * &nbsp;...
 * </pre>
 * 
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface RemoveValidator {
	
	/**
	 * Class that implements the validation logic. <p>
	 * 
 	 * Must implement IRemoveValidator.
	 */
	Class value();
	
	/**
	 * To set the value of the validator properties before executing it.
	 */
	PropertyValue [] properties() default {};
	
}
