package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * A group of @DescriptionsList associated to the same member. <p>
 * 
 * Applies to references.<p>
 * 
 * It allows to define a value different for @Action in each view.<br>
 * Example:
 * <pre>
 * @DescriptionsLists({
 *   @DescriptionsList(forViews="DEFAULT", ... ),
 *   @DescriptionsList(forViews="Simple, VerySimple", ... ),
 *   @DescriptionsList(forViews="Complete", ... )
 * })
 * </pre>
 *  
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface DescriptionsLists {
	
	DescriptionsList [] value();
	
}
