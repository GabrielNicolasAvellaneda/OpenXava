package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * This validator allows to define a validation at entity level. <p>
 * 
 * Applies to entities. <p>
 * 
 * When you need to make a validation on several properties at a time, and that 
 * validation does not correspond logically with any of them, then
 * you can use this type of validation.<br>
 * Example:
 * <pre>
 * &nbsp;@Entity
 * &nbsp;@EntityValidator(InvoiceDetailValidator.class,
 * &nbsp;&nbsp;&nbsp;properties= { 
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;@PropertyValue(name="invoice"), 
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;@PropertyValue(name="oid"), 
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;@PropertyValue(name="product"),
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;@PropertyValue(name="unitPrice")
 * &nbsp;&nbsp;&nbsp;}
 * &nbsp;)
 * &nbsp;public class InvoiceDetail {
 * &nbsp;...
 * </pre>
 *
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface EntityValidator {
	
	/**
     * Class that implements the validation logic. It
     * has to be of type {@link org.openxava.validators.IValidator}.
	 */
	Class value();
	
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
