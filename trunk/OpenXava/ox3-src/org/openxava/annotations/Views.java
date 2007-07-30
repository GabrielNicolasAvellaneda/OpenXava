package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * A group of @View associated to the same entity. <p>
 * 
 * Applies to entities.<p>
 * 
 * Example:
 * <pre>
 * @Entity
 * @Views({
 *	 @View(members=
 *		"year, number, date, paid;" +
 *		"discounts [" +
 *			"customerDiscount, customerTypeDiscount, yearDiscount;" +
 *		"];" +
 *		"comment;" +			
 *		"customer { customer }" +
 *		"details { details }" +			
 *		"amounts { amountsSum; vatPercentage; vat }" +
 *		"deliveries { deliveries }"		
 *	 ),
 * 	 @View(name="Simple", members="year, number, date, yearDiscount;"),
 * 	 @View(name="NestedSections", members= 	
 * 		"year, number, date;" + 		  		
 *		"customer { customer }" +
 *		"data {" +				 
 *		"	details { details }" +
 *		"	amounts {" +
 *		"		vat { vatPercentage; vat }" +				
 *		"		amountsSum { amountsSum }" +
 *		"	}" +				
 *		"}" +						
 *		"deliveries { deliveries }"		
 *	 ),
 *	 @View(name="Deliveries", members=
 *		"year, number, date;" +
 *		"deliveries;"
 *	 ),
 *	 @View(name="Amounts", members=
 *		"year, number;" +
 *		"amounts [#" + 
 *			"customerDiscount, customerTypeDiscount, yearDiscount;" +
 *			"amountsSum, vatPercentage, vat;" +
 *		"]"			
 *	 )
 * }) 
 * public class Invoice {
 * ...
 * </pre>
 *
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Views {
	
	View [] value();
	
}
