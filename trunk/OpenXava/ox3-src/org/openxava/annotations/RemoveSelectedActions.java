package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * A group of @RemoveSelectedAction associated to the same collection. <p>
 * 
 * Applies to collections.<p>
 * 
 * It allows to define a value different for @RemoveSelectedAction in each view.<br>
 * Example:
 * <pre>
 * @RemoveSelectedActions({
 *   @RemoveSelectedAction(forViews="DEFAULT", ... ),
 *   @RemoveSelectedAction(forViews="Simple, VerySimple", ... ),
 *   @RemoveSelectedAction(forViews="Complete", ... )
 * })
 * </pre>
 * 
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface RemoveSelectedActions {
	
	RemoveSelectedAction [] value();
	
}
