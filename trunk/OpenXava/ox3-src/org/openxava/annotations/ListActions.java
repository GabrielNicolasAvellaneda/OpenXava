package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * A group of @ListAction associated to the same collection. <p>
 * 
 * Applies to collections.<p>
 * 
 * It allows to define a value different for @ListAction in each view.<br>
 * Example:
 * <pre>
 * @ListActions({
 *   @ListAction(forViews="DEFAULT", value= ... ),
 *   @ListAction(forViews="Simple, VerySimple", value= ... ),
 *   @ListAction(forViews="Complete", value= ... )
 * })
 * </pre>
 *
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface ListActions {
	
	ListAction [] value();
	
}
