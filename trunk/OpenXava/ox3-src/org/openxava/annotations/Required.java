package org.openxava.annotations;

import java.lang.annotation.*;

/**
 * Indicates if this property or reference is required. <p>
 * 
 * By default this is true for key properties hidden or without default value 
 * calculator on create and false in all other cases. On saving OpenXava 
 * verifies if the required properties are present. If this is not the
 * case, then saving is not done and a validation error list is returned.<br> 
 * The logic to determine if a property is present or not can be configured 
 * by creating a file called validators.xml in your
 * project. You can see the syntax in OpenXava/xava/validators.xml.<br>
 * Example:
 * <pre>
 * &nbsp;@Required 
 * &nbsp;private int zipCode;
 * </pre>
 * 
 * In the case of reference you can use the JPA syntax as alternative:
 * <pre>
 * &nbsp;@ManyToOne(optional=false) 	
 * &nbsp;private State state;
 * </pre>
 * That is, for OpenXava <code>@ManyToOne(optional=false)</code> is synonymous of
 * <code>@{@link Required}</code>. 
 *  
 * @author Javier Paniza
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface Required {
	
}
