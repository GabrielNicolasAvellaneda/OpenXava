package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * A group of @ViewAction associated to the same collection. <p>
 * 
 * Applies to collections.<p>
 * 
 * It allows to define a value different for @ViewAction in each view.<br>
 * Example:
 * <pre>
 * @ViewActions({
 *   @ViewAction(forViews="DEFAULT", ... ),
 *   @ViewAction(forViews="Simple, VerySimple", ... ),
 *   @ViewAction(forViews="Complete", ... )
 * })
 * </pre>
 *
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface ViewActions {
	
	ViewAction [] value();
	
}
