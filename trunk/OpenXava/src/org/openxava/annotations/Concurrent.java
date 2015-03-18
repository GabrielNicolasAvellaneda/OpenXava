package org.openxava.annotations;

import java.lang.annotation.*;

import org.openxava.annotations.Property.Type;

/**
 * It is used for optimistic concurrency control. If you want concurrency 
 * control on your OpenXava component only you need mark your entity with 
 * {@literal @}Concurrent. <p>
 * 
 * @author Jeromy Altuna
 * @since  5.3
 */
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.TYPE })
public @interface Concurrent {
	
	/**
	 * Property that will be inserted in the class bytecode for concurrency 
	 * control. <p>
	 * 
	 * By default:
	 * <pre><code>
	 * {@literal @}Version
	 * private Integer version;
	 * </code></pre>
	 */
	Property value() default @Property(name="version", type=Type.INTEGER);
}
