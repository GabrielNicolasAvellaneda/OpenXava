package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * The eXtended version of @OrderBy. <p>
 * 
 * Applies to collections.<p>
 * 
 * The @OrderBy of JPA does not allow to use qualified properties (properties of references).<br>
 * OpenXava has its own version of @OrderBy, this @XOrderBy to allow it.
 * Example:
 * <pre>
 * @OneToMany (mappedBy="invoice", cascade=CascadeType.REMOVE)
 * @ListProperties("product.description, quantity, unitPrice, amount")
 * @XOrderBy("product.description desc") 
 * private Collection<InvoiceDetail> details;
 * </pre>
 * You can note as <code>product.description</code> can be used for ordering the collection.<br>
 * In order to use a qualified property, it must be included in the @ListProperties,
 * as in this case with <code>product.description</code>.<p>
 * 
 * The order in @XOrderBy has only effect at visual level, when you access programmatically
 * to the collection, the collection is ordered as indicated by the JPA @OrderBy or by its default order.<br> 
 * 
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface XOrderBy {

	/**
	 * The order using the syntax of JPA @OrderBy but allowing qualified properties with
	 * any level of indirection.
	 */
	String value();
	
}
