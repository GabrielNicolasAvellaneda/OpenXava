package org.openxava.annotations;

import java.lang.annotation.*;


/**
 * A group of @RowStyle associated to the same collection. <p>
 * 
 * It allows to define a value different for @RowStyle in each view.<br>
 * Example:
 * <pre>
 * @RowStyles({
 *   @RowStyle(forViews="Specials", style="highlight", property="type", value="special"),
 *   @RowStyle(forViews="Complete", style="highlight", property="type", value="steady") 	
 * })
 * </pre>
 *
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface RowStyles {
	
	RowStyle [] value();
	
}
