package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * A group of @ReferenceView associated to the same reference. <p>
 * 
 * Applies to references.<p>
 * 
 * It allows to define a value different for @ReferenceView in each view.<br>
 * Example:
 * <pre>
 * @ReferenceViews({
 *   @ReferenceView(forViews="DEFAULT", value= ... ),
 *   @ReferenceView(forViews="Simple, VerySimple", value= ... ),
 *   @ReferenceView(forViews="Complete", value= ... )
 * })
 * </pre>
 *
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface ReferenceViews {
	
	ReferenceView [] value();
	
}
