package org.openxava.annotations;

import java.lang.annotation.*;


/**
 * A group of @LabelFormat associated to the same member. <p>
 * 
 * Applies to properties and references with descriptions list.<p>
 * 
 * It allows to define a value different for @LabelFormat in each view.<br>
 * Example:
 * <pre>
 * @LabelFormats({
 *   @LabelFormat(forViews="DEFAULT", value= ... ),
 *   @LabelFormat(forViews="Simple, VerySimple", value= ... ),
 *   @LabelFormat(forViews="Complete", value= ... )
 * })
 * </pre>
 *
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface LabelFormats {
	
	LabelFormat [] value();
	
}
