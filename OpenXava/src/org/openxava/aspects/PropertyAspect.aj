package org.openxava.aspects;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.aspectj.lang.reflect.FieldSignature;
import org.openxava.util.*;

/**
 * Allows to work using properties without getters and setter, at Delphi style. <p>
 * 
 * <i><b>WARNING!</b>: This is a <b>experimental</b> feature and it will not 
 * be full functional and tested until OX3.1.</i><p>
 * 
 * You need to compile your code with AspectJ, or to have installed the
 * AJDT plugin in your Eclipse in order to use this aspect.<p>
 * 
 * With this aspect you can write a property in this way:
 * <pre>
 * public String name;
 * </pre>
 * with no getter nor setter, and use it as following:
 * <pre>
 * p.name = "Mª Carmen";
 * </pre>
 * If you want to create a calculated property you can write:
 * <pre>
 * public String fullName;
 * protected String getFullName() {
 *   return name + " " + surnames;
 * }
 * </pre>
 * And you can use <code>fullName</code> property in this way:
 * <pre>
 * p.name = "Mª Carmen";
 * p.surnames = "Gimeno Alabau";		
 * assertEquals("Mª Carmen Gimeno Alabau", p.fullName); 
 * </pre> 
 * You can also refined the way to set value to property:
 * <pre>
 * public int age;
 * protected void setAge(int age) {		
 *   if (age > 200) {
 *     throw new IllegalArgumentException("Too old");
 *   }
 *   this.age = age;
 * }	
 * </pre>
 * Then if you write:
 * <pre>
 * p.age = 250;
 * </pre>
 * An exception will be throw.<br>
 * Also it's possible to refine the getter, in this way:
 * <pre>
 * public String address;
 * protected String getAddress() {
 *   return address.toUpperCase();
 * }
 * </pre>
 * Then you can write:
 * <pre>
 * p.address = "C/. Almudaina";		
 * assertEquals("C/. ALMUDAINA", p.address);		
 * </pre>
 * You can note as when you use the field inside the declaring class
 * the getter is not automatically called, that is inside the declaring
 * class this.address or address access directly to the field, but outside
 * the declaring class (p.address) the access using the getter (if it exists).<p>
 * 
 * You can use properties to define properties, as following:
 * <pre>
 * public String fullNameAndAddress;
 * protected String getFullNameAndAddress() {
 *   return getFullName() + " " + getAddress(); 
 * }
 * </pre>
 * You can note that inside the class we use getters and setter
 * for acceding to others properties.<br>
 * In this way, from inside the declaring class, you can choose to use 
 * the getter or to access to the field directly.<br>
 * 
 * @author Javier Paniza
 */

aspect PropertyAspect {
	
 
	pointcut getter() : 
		get(public !final !static * *..model*.*.*) &&
		!get(public !final !static * *..model*.*Key.*) &&
		!within(PropertyAspect);
	pointcut setter() : 
		set(public !final !static  * *..model*.*.*) &&
		!set(public !final !static * *..model*.*Key.*) &&
		!within(PropertyAspect);
	
	Object around(Object target, Object source) : target(target) && this(source) && getter(){
		if (target == source) {			
			return proceed(target, source);
		}
		try {
			String methodName = "get" + Strings.firstUpper(thisJoinPointStaticPart.getSignature().getName()); 
			Method method = target.getClass().getDeclaredMethod(methodName, null);
			method.setAccessible(true);
			return method.invoke(target, null);
		}
		catch (NoSuchMethodException ex) {
			return proceed(target, source);
		}
		catch (InvocationTargetException ex) {
			Throwable tex = ex.getTargetException();
			if (tex instanceof RuntimeException) {
				throw (RuntimeException) tex;
			}
			else throw new RuntimeException(
					XavaResources.getString("getter_failed", 
							thisJoinPointStaticPart.getSignature().getName(), 
							target.getClass()), 
					ex);

		}		
		catch (Exception ex) {			
			throw new RuntimeException(
				XavaResources.getString("getter_failed", 
						thisJoinPointStaticPart.getSignature().getName(), 
						target.getClass()), 
				ex);

		}
	}
	
	void around(Object target, Object source, Object newValue) : target(target) && this(source) && args(newValue) && setter(){		
		if (target == source) {					
			proceed(target, source, newValue);
			return;
		}
		try {			
			String methodName = "set" + Strings.firstUpper(thisJoinPointStaticPart.getSignature().getName());						
			Class fieldType = ((FieldSignature) thisJoinPointStaticPart.getSignature()).getFieldType();			
			Method method = target.getClass().getDeclaredMethod(methodName, new Class[] { fieldType } );
			method.setAccessible(true);
			method.invoke(target, new Object[] { newValue } );
		}
		catch (NoSuchMethodException ex) {
			proceed(target, source, newValue);
		}
		catch (InvocationTargetException ex) {
			Throwable tex = ex.getTargetException();
			if (tex instanceof RuntimeException) {
				throw (RuntimeException) tex;
			}
			else throw new RuntimeException(
					XavaResources.getString("setter_failed", 
							thisJoinPointStaticPart.getSignature().getName(), 
							target.getClass()), 
					ex);
		}
		catch (Exception ex) {			
			throw new RuntimeException(
				XavaResources.getString("setter_failed", 
						thisJoinPointStaticPart.getSignature().getName(), 
						target.getClass()), 
				ex);
		}
	}
	
			
}
