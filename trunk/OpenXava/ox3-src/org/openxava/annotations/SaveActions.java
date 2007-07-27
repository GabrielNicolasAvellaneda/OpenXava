package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * A group of @SaveAction associated to the same collection. <p>
 * 
 * Applies to collections.<p>
 * 
 * It allows to define a value different for @SaveAction in each view.<br>
 * Example:
 * <pre>
 * @SaveActions({
 *   @SaveAction(forViews="DEFAULT", ... ),
 *   @SaveAction(forViews="Simple, VerySimple", ... ),
 *   @SaveAction(forViews="Complete", ... )
 * })
 * </pre>
 *
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface SaveActions {
	
	SaveAction [] value();
	
}
