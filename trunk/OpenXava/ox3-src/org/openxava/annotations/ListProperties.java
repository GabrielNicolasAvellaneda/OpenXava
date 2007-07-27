package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * Properties to show in the list for visualization of a collection. <p>
 * 
 * Applies to collections. <p>
 * 
 * You can qualify the properties. By default it shows all persistent properties 
 * of the referenced object (excluding references and calculated properties).<br>
 * Example:
 * <pre>
 * @OneToMany (mappedBy="invoice", cascade=CascadeType.REMOVE)
 * @ListProperties(properties="serviceType, product.description, product.unitPriceInPesetas, quantity, unitPrice, amount, free")
 * private Collection<InvoiceDetail> details;
 * </pre>
 * 
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface ListProperties {
	
	/**
	 * List of comma separated view names where this annotation applies. <p>
	 * 
	 * Exclusive with notForViews.<br>
	 * If both forViews and notForViews are omitted then this annotation
	 * apply to all views.<br>
	 * You can use the string "DEFAULT" for referencing to the default
	 * view (the view with no name).
	 */	
	String forViews() default "";
	
	/**
	 * List of comma separated view names where this annotation does not apply. <p>
	 * 
	 * Exclusive with forViews.<br>
	 * If both forViews and notForViews are omitted then this annotation
	 * apply to all views.<br>
	 * You can use the string "DEFAULT" for referencing to the default
	 * view (the view with no name).
	 */ 	
	String notForViews() default "";
	
	/**
	 * Comma separated list of properties. <p>
	 * 
	 * It's possible to use qualified properties to show members of rerences.
	 */
	String properties();
	
}
