package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * The size in characters of the editor in the User Interface used to display 
 * this property. <p>
 * 
 * Applies to properties.<p>
 *  
 * The editor display only the characters indicated by @DisplaySize but it 
 * allows to the user to entry until the total size of the property. 
 * If @DisplaySize is not specified, the value of the size of the property 
 * is assumed.<br>
 * Example:
 * <pre>
 * @Column(length=50) @Required @DisplaySize(25) 
 * private String description;
 * </pre>
 * In this case the user can type until 50 characters but only 25 are displayed.
 * 
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface DisplaySize {
	
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
	 * The size in characters.
	 */
	int value();
	
}
