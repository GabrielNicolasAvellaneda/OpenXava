package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * Defines with precision the format of the user interface or view. <p>
 * 
 * Applies to entities. <p>
 * 
 * Example:
 * <pre>
 * &nbsp;@Entity
 * &nbsp;@View(members=
 * &nbsp;&nbsp;&nbsp;"year, number, date, paid;" +
 * &nbsp;&nbsp;&nbsp;"discounts [" +
 * &nbsp;&nbsp;&nbsp;"	customerDiscount, customerTypeDiscount, yearDiscount;" +
 * &nbsp;&nbsp;&nbsp;"];" +
 * &nbsp;&nbsp;&nbsp;"comment;" +			
 * &nbsp;&nbsp;&nbsp;"customer { customer }" +
 * &nbsp;&nbsp;&nbsp;"details { details }" +			
 * &nbsp;&nbsp;&nbsp;"amounts { amountsSum; vatPercentage; vat }" +
 * &nbsp;&nbsp;&nbsp;"deliveries { deliveries }"		
 * &nbsp;)
 * &nbsp;public class Invoice {
 * &nbsp;...
 * </pre>
 * 
 * 
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface View {
	
	/**
	 * This name identifies the view. <p>
	 * 
	 * It  can be used in other OpenXava places (for
	 * example in application.xml) or from another entities. <br>
	 * This name is referenced from <code>forViews</code> or 
	 * <code>notForViews</code> of other OpenXava annotations. <br>
	 * If the view has no name then the view is assumed as the default one, 
	 * that is the natural form to display an object of this type.
	 */
	String name() default "";
	
	/**
	 * Indicates the members to display and its layout in the user interface. <p>
	 *
	 * Use comma (,) for separating elements, and semicolon (;) for new line.<br>
	 * You can use section {} and group [] elements for layout purposes; 
	 * or actions ( MyController.myAction() ) element for showing a link 
	 * associated to a custom action inside your view.
	 */	
	String members() default "";
	
}
