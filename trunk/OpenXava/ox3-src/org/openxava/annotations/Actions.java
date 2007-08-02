package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * A group of @Action associated to the same member. <p>
 * 
 * Applies to properties and references.<p>
 * 
 * It allows to define a value different for @Action in each view.<br>
 * Example:
 * <pre>
 * @Actions({
 *   @Action(forViews="DEFAULT", value= ... ),
 *   @Action(forViews="Simple, VerySimple", value= ... ),
 *   @Action(forViews="Complete", value= ... )
 * })
 * </pre>
 *   
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface Actions {
	
	Action [] value();
	
}
