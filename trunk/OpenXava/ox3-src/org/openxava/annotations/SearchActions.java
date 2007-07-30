package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * A group of @SearchAction associated to the same reference. <p>
 * 
 * Applies to references.<p>
 * 
 * It allows to define a value different for @SearchAction in each view.<br>
 * Example:
 * <pre>
 * @SearchActions({
 *   @SearchAction(forViews="DEFAULT", ... ),
 *   @SearchAction(forViews="Simple, VerySimple", ... ),
 *   @SearchAction(forViews="Complete", ... )
 * })
 * </pre>
 * 
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface SearchActions {
	
	SearchAction [] value();
	
}
