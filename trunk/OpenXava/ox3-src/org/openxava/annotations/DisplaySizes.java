package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * A group of @DisplaySize associated to the same property. <p>
 * 
 * Applies to properties.<p>
 * 
 * It allows to define a value different for @DisplaySize in each view.<br>
 * Example:
 * <pre>
 * @DisplaySizes({
 *   @DisplaySize(forViews="DEFAULT", value= ... ),
 *   @DisplaySize(forViews="Simple, VerySimple", value= ... ),
 *   @DisplaySize(forViews="Complete", value= ... )
 * })
 * </pre>

 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface DisplaySizes {
	
	DisplaySize [] value();
	
}
