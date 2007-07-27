package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * A group of @RemoveAction associated to the same collection. <p>
 * 
 * Applies to collections.<p>
 * 
 * It allows to define a value different for @RemoveAction in each view.<br>
 * Example:
 * <pre>
 * @RemoveActions({
 *   @RemoveAction(forViews="DEFAULT", ... ),
 *   @RemoveAction(forViews="Simple, VerySimple", ... ),
 *   @RemoveAction(forViews="Complete", ... )
 * })
 * </pre>
 * 
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface RemoveActions {
	
	RemoveAction [] value();
	
}
