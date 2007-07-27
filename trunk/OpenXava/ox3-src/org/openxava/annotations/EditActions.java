package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * A group of @EditAction associated to the same collection. <p>
 * 
 * Applies to collections.<p>
 * 
 * It allows to define a value different for @EditAction in each view.<br>
 * Example:
 * <pre>
 * @EditActions({
 *   @EditAction(forViews="DEFAULT", ... ),
 *   @EditAction(forViews="Simple, VerySimple", ... ),
 *   @EditAction(forViews="Complete", ... )
 * })
 * </pre>
 *
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface EditActions {
	
	EditAction [] value();
	
}
