package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * A group of @HideDetailAction associated to the same collection. <p>
 * 
 * Applies to collections.<p>
 * 
 * It allows to define a value different for @HideDetailAction in each view.<br>
 * Example:
 * <pre>
 * @HideDetailActions({
 *   @HideDetailAction(forViews="DEFAULT", value= ... ),
 *   @HideDetailAction(forViews="Simple, VerySimple", value= ... ),
 *   @HideDetailAction(forViews="Complete", value= ... )
 * })
 * </pre>
 * 
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface HideDetailActions {
	
	HideDetailAction [] value();
	
}
