package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * A group of @NewAction associated to the same collection. <p>
 * 
 * Applies to collections.<p>
 * 
 * It allows to define a value different for @NewAction in each view.<br>
 * Example:
 * <pre>
 * @NewActions({
 *   @NewAction(forViews="DEFAULT", value= ... ),
 *   @NewAction(forViews="Simple, VerySimple", value= ... ),
 *   @NewAction(forViews="Complete", value= ... )
 * })
 * </pre>
 *
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface NewActions {
	
	NewAction [] value();
	
}
