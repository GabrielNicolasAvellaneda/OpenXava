package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * A group of @DetailAction associated to the same collection. <p>
 * 
 * Applies to collections.<p>
 * 
 * It allows to define a value different for @DetailAction in each view.<br>
 * Example:
 * <pre>
 * @DetailActions({
 *   @DetailAction(forViews="DEFAULT", ... ),
 *   @DetailAction(forViews="Simple, VerySimple", ... ),
 *   @DetailAction(forViews="Complete", ... )
 * })
 * </pre>
 *
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface DetailActions {
	
	DetailAction [] value();
	
}
