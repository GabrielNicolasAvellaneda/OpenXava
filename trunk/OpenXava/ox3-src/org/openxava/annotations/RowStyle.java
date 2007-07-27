package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * For indicating the row style in a {@link Tab}. <p>
 * 
 * Example:
 * <pre>
 * @Tab(
 *		rowStyles=@RowStyle(style="highlight", property="type", value="steady")
 * )
 * </pre>
 * In this case you are saying that the object which property type has the 
 * value steady will use the style highlight. The style has to be defined in the
 * CSS stylesheet. The <i>highlight</i> style are already defined in OpenXava, but 
 * you can define more.
 * 
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE })
public @interface RowStyle {
	
	/**
	 * The name of the style to apply. <p>
	 * 
	 * Must be a style defined in the CSS.
	 */
	String style();
	
	/**
	 * Property to evaluate. <p>
	 * 
	 * If value of this 'property' is the one indicate in 'value', 
	 * then the 'style' apply to this row.  
	 */
	String property();
	
	/**
	 * Value to compare with value property. <p>
	 * 
	 * If value of 'property' is the one indicate here, 
	 * then the 'style' apply to this row.  
	 */
	String value();
	
}
