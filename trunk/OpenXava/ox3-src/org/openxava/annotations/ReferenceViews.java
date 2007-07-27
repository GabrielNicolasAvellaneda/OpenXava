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
 *   @ReferenceView(forViews="DEFAULT", ... ),
 *   @ReferenceView(forViews="Simple, VerySimple", ... ),
 *   @ReferenceView(forViews="Complete", ... )
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
