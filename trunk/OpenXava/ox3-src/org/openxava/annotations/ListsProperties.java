package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * A group of @ListProperties associated to the same collection. <p>
 * 
 * Applies to collections.<p>
 * 
 * It allows to define a value different for @ListProperties in each view.<br>
 * Example:
 * <pre>
 * @ListsProperties({
 *   @ListProperties(forViews="DEFAULT", ... ),
 *   @ListProperties(forViews="Simple, VerySimple", ... ),
 *   @ListProperties(forViews="Complete", ... )
 * })
 * </pre>
 * 
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface ListsProperties {
	
	ListProperties [] value();
	
}
