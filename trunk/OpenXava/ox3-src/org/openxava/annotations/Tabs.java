package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * A group of @Tab associated to the same entity. <p>
 * 
 * Applies to entities.<p>
 * 
 * Example:
 * <pre>
 * @Entity
 * @Tabs({
 *	  @Tab(properties="year, number, date, amountsSum, vat, detailsCount, paid, importance"),
 *	  @Tab(name="Level4Reference", properties="year, number, customer.seller.level.description"),
 *	  @Tab(name="Simple", properties="year, number, date", 
 *		 defaultOrder="${year} desc, ${number} desc"
 *    )
 * }) 
 * public class Invoice {
 * ...
 * </pre>
 * 
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Tabs {
	
	Tab [] value();
	
}
