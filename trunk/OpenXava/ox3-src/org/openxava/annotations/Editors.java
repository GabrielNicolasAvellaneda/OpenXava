package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * A group of @Editor associated to the same property. <p>
 * 
 * Applies to properties.<p>
 * 
 * It allows to define a value different for @Editor in each view.<br>
 * Example:
 * <pre>
 * @Editors({
 *   @Editor(forViews="DEFAULT", value= ... ),
 *   @Editor(forViews="Simple, VerySimple", value= ... ),
 *   @Editor(forViews="Complete", value= ... )
 * })
 * </pre>
 *
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface Editors {
	
	Editor [] value();
	
}
