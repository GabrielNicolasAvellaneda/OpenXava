package org.openxava.util;

import java.io.*;
import java.lang.reflect.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Utility class to work with objects. <p>
 * 
 * @author: Javier Paniza
 */

public class Objects {

  private static final Class [] arg0 = { };	
  
  private static Log log = LogFactory.getLog(Objects.class);

  /**
   * Clone the sent object. <p>
   * 
   * The clone is done using the method <code>clone()</tt> of the 
   * sent object if this implements <code>Cloneable</code>
   *
   * @exception CloneNotSupportedException If the object is not <tt>Cloneable</tt>.<br>
   */
  public static Object clone(Object o) throws CloneNotSupportedException {
	try {
		if (o == null)  return null;
		if (o instanceof Cloneable) {
			return execute(o, "clone");
		}
		else {
			throw new CloneNotSupportedException(XavaResources.getString("implement_cloneable_required"));
		}
	}
	catch (NoSuchMethodException ex) {
		throw new CloneNotSupportedException(XavaResources.getString("clone_required"));
	}
	catch (CloneNotSupportedException ex) {
		throw ex;
	}
	catch (Exception ex) { // Not very often
		log.error(ex.getMessage(), ex);
		throw new CloneNotSupportedException(XavaResources.getString("clone_error", o));
	}
  }
  
  /**
   * Does an deep clone of the sent object. <p>
   * 
   * If the argument is null, then returns null.<br>
   * 
   * @param source  Must be serializable. It's not necessary that it implements <code>Cloneable</code>.
   * @return Clone of the sent object. Does an clon of all levels, complete, deep.
   * @exception CloneException  Any problem on cloning.
   */
  public static Object deepClone(Object source) throws CloneException {
	try {
		if (source == null) return null;
		// reading
		ByteArrayOutputStream byteout = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteout);		
		out.writeObject(source);
		byte [] buffer = byteout.toByteArray();
		byteout.close();
		out.close();
		// saving
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buffer));
		Object result = in.readObject();
		in.close();
		return result;
	}
	catch (Exception ex) {
		log.error(ex.getMessage(), ex);
		throw new CloneException(XavaResources.getString("deep_clone_error", source));
	}
  }

  /**
   * Allows to execute dinamically (as in SmallTalk) an method of an object. <p> 
   * 
   * @param o  Object where the method will be executed. It cannot be null.
   * @param methodName  Name of the method to execute.
   * @return  Result of method execution.
   * @exception Exception  Any problem, including exceptions from target method.
   * @exception NoSuchMethodException  If the method does not exist in target object.
   */
  public static Object execute(Object o, String methodName) throws Exception {
	try {
		Assert.arg(o);		
		Method m = o.getClass().getMethod(methodName, arg0);
		Object[] args = { };
		return m.invoke(o, args);
	} catch (InvocationTargetException ex) {
		Throwable th = ex.getTargetException();
		if (th instanceof Exception) {
	        throw (Exception) th;
		}
		else {
	        throw (Error) th;
		}
	} catch (NoSuchMethodException ex) {
		throw ex;
	}
  }

  /**
   * Allows to execute dinamically (as in SmallTalk) an method of an object. <p> 
   * 
   * @param o  Object where the method will be executed. It cannot be null.
   * @param methodName  Name of the method to execute.
   * @param argumentClass Not null.
   * @param arg Argument value. It can be null.
   * @return  Result of method execution.
   * @exception Exception  Any problem, including exceptions from target method.
   * @exception NoSuchMethodException  If the method does not exist in target object.
   */  
  public static Object execute(Object o, String methodName, Class argumentClass, Object arg) throws Exception {
	try {
		Assert.arg(o, methodName, argumentClass);
		Class [] clArg = { argumentClass }; 
		Method m = o.getClass().getMethod(methodName, clArg);
		Object[] args = { arg };
		return m.invoke(o, args);
	} catch (InvocationTargetException ex) {
		Throwable th = ex.getTargetException();
		if (th instanceof Exception) {
	        throw (Exception) th;
		}
		else {
	        throw (Error) th;
		}
	} catch (NoSuchMethodException ex) {
		throw ex;
	}
  }

  /**
   * Try to clone the sent object. <p> 
   *
   * If it does not win it, then return the original element.<br>
   * 
   * The clone is done using the method <code>clone()</code> of the
   * sent object, if it implements <code>Cloneable</code>.<br>
   */
  public static Object tryClone(Object o) {
	try {
		if (o == null)  return null;
		if (o instanceof Cloneable) {
			return execute(o, "clone");
		}
		return o;
	}
	catch (NoSuchMethodException ex) {
		throw new IllegalArgumentException(XavaResources.getString("clone_required"));
	}
	catch (Exception ex) {
		log.warn("Impossible to clone " + o + " because " + ex.getMessage() + " The original element is returned", ex);
		return o;
	}
  }
  
}
