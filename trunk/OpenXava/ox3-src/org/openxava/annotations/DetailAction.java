package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * In collection view to add actions in detail mode, usually actions which scope
 * is the detail that is being edited. <p>
 * 
 * Applies to collections.<p>
 * 
 * Example:
 * <pre>
 * @OneToMany (mappedBy="invoice", cascade=CascadeType.REMOVE)
 * @DetailAction("Invoices.viewProduct")
 * private Collection<InvoiceDetail> details;
 * </pre>
 * 
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface DetailAction {
	
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
	 * You have to write the action identifier that is the controller
	 * name and the action name. This action must be registered in controllers.xml
	 */	
	String value();
	
}
