package org.openxava.ejbx;

import java.lang.reflect.*;
import java.rmi.*;
import java.util.*;
import javax.ejb.*;
import javax.rmi.*;

import org.openxava.util.*;
import org.openxava.validators.*;




/**
 * Clase de utilidad para crear EJB a partir del home. <p>
 *
 * Podemos crear con un argumento (típico para EntityBeans) y
 * sin argumento (típico de SessionBeans). <br>
 *
 * @version 00.09.28
 * @author  Javier Paniza
 */

 /*
 00.02.18  Creación
 00.08.30  Se añade create(EJBHome, Map)
 00.09.28  Se añade narrow para que sea compatible IIOP
 */

public class EJBFactory {

  private static Class [] detailsClass0 = { };
  private static Class [] detailsClass1 = { java.lang.Object.class };
  private static Class [] detailsClass2 = { java.util.Map.class };

  /**
   * Crea un EJB a partir de un <i>home</i>. <p>
   *
   * El <i>home</i> enviado ha de tener un método <code>create()</code>.<br>
   *
   * @param home  El <i>home</i> sobre que se invoca el método de creación.
   * @exception CreateException Se redireccion la del create original
   * @exception RemoteException Se redireccion la del create original
   * @exception NoSuchMethodException Si el <i>home</i> no tiene ningún método <code>create()</code>
   */
  public static Object create(EJBHome home) throws CreateException, RemoteException, NoSuchMethodException {
	try {
	  Class cl = home.getEJBMetaData().getHomeInterfaceClass();
	  if (cl == null) {
		  throw new RemoteException(XavaResources.getString("ejbhome_error"));
	  }
	  Method m = cl.getDeclaredMethod("create", detailsClass0);
	  Object narrowHome = PortableRemoteObject.narrow(home, cl);
	  return m.invoke(narrowHome, null);
	}
	catch (InvocationTargetException ex) {
	  Throwable th = ex.getTargetException();
	  try {
		throw th;
	  }
	  catch (CreateException ex2) {
		throw ex2;
	  }
	  catch (RemoteException ex2) {
		throw ex2;
	  }
	  catch (Throwable ex2) {
		throw new RemoteException(ex2.getLocalizedMessage(), ex2);
	  }
	}
	catch (NoSuchMethodException ex) {
	  throw ex;
	}
	catch (Exception ex) {
	  throw new RemoteException(ex.getLocalizedMessage(), ex);
	}
  }
  
  /**
   * Crea un EJB a partir de un <i>home</i> y su clase. <p>
   *
   * El <i>home</i> enviado ha de tener un método <code>create()</code>.<br>
   *
   * @param home  El <i>home</i> sobre que se invoca el método de creación.
   * @param clase  Clse del <i>home</i> sobre que se invoca el método de creación.
   * @exception CreateException Se redireccion la del create original
   * @exception RemoteException Se redireccion la del create original
   * @exception NoSuchMethodException Si el <i>home</i> no tiene ningún método <code>create()</code>
   */
  public static Object create(Object home, Class clase) throws CreateException, RemoteException, NoSuchMethodException {
	try {		
	  Method m = clase.getDeclaredMethod("create", detailsClass0);	  
	  Object narrowHome = PortableRemoteObject.narrow(home, clase);	  
	  return m.invoke(narrowHome, null);
	}
	catch (InvocationTargetException ex) {
	  Throwable th = ex.getTargetException();
	  try {
		throw th;
	  }
	  catch (CreateException ex2) {
		throw ex2;
	  }
	  catch (RemoteException ex2) {
		throw ex2;
	  }
	  catch (Throwable ex2) {
		throw new RemoteException(ex2.getLocalizedMessage(), ex2);
	  }
	}
	catch (NoSuchMethodException ex) {
	  throw ex;
	}
	catch (Exception ex) {
	  throw new RemoteException(ex.getLocalizedMessage(), ex);
	}
  }
  
  /**
   * Crea un EJB a partir de un <i>home</i> y el argumento del create. <p>
   *
   * El <i>home</i> enviado ha de tener un método <code>create(Object )</code>.<br>
   *
   * @param home  El <i>home</i> sobre que se invoca el método de creación.
   * @param details  Argumento enviado al método <code>create</code>
   * @exception ValidationException Se redireccion la del create original
   * @exception CreateException Se redireccion la del create original
   * @exception RemoteException Se redireccion la del create original
   * @exception NoSuchMethodException Si el <i>home</i> no tiene ningún método <code>create(Object )</code>
   */
  public static Object create(EJBHome home, Object details) throws ValidationException, CreateException, RemoteException, NoSuchMethodException  {
	try {
	  Class cl = home.getEJBMetaData().getHomeInterfaceClass();
	  if (cl == null) {
	  	throw new RemoteException(XavaResources.getString("ejbhome_error"));
	  }	  
	  Method m = cl.getDeclaredMethod("create", detailsClass1);
	  Object [] args = { details };
	  return m.invoke(home, args);
	}
	catch (InvocationTargetException ex) {
	  Throwable th = ex.getTargetException();
	  try {
		throw th;
	  }
	  catch (CreateException ex2) {
		throw ex2;
	  }
	  catch (ValidationException ex2) {
		throw ex2;
	  }
	  catch (RemoteException ex2) {
		throw ex2;
	  }
	  catch (Throwable ex2) {
		throw new RemoteException(ex2.getLocalizedMessage(), ex2);
	  }
	}
	catch (NoSuchMethodException ex) {
	  throw ex;
	}
	catch (Exception ex) {
	  throw new RemoteException(ex.getLocalizedMessage(), ex);
	}
  }
  
  /**
   * Crea un EJB a partir de un <i>home</i> y el argumento del create. <p>
   *
   * El <i>home</i> enviado ha de tener un método <code>create(Map )</code>.<br>
   *
   * @param home  El <i>home</i> sobre que se invoca el método de creación.
   * @param mapa  Argumento enviado al método <code>create</code>
   * @exception ValidationException Se redireccion la del create original
   * @exception CreateException Se redireccion la del create original
   * @exception RemoteException Se redireccion la del create original
   * @exception NoSuchMethodException Si el <i>home</i> no tiene ningún método <code>create(Object )</code>
   */
  public static Object create(EJBHome home, Map mapa) throws ValidationException, CreateException, RemoteException, NoSuchMethodException  {
	try {
	  Class cl = home.getEJBMetaData().getHomeInterfaceClass();
	  if (cl == null) {
	  	throw new RemoteException(XavaResources.getString("ejbhome_error"));
	  }	  
	  Method m = cl.getDeclaredMethod("create", detailsClass2);
	  Object [] args = { mapa };
	  return m.invoke(home, args);
	}
	catch (InvocationTargetException ex) {
	  Throwable th = ex.getTargetException();
	  try {
		throw th;
	  }
	  catch (CreateException ex2) {
		throw ex2;
	  }
	  catch (ValidationException ex2) {
		throw ex2;
	  }
	  catch (RemoteException ex2) {
		throw ex2;
	  }
	  catch (Throwable ex2) {
		throw new RemoteException(ex2.getLocalizedMessage(), ex2);
	  }
	}
	catch (NoSuchMethodException ex) {
	  throw ex;
	}
	catch (Exception ex) {
	  throw new RemoteException(ex.getLocalizedMessage(), ex);
	}
  }
  
  
  /**
   * Crea un EJB a partir de un <i>home</i>, su clase y un <tt>java.util.Map</tt> como
   * argumento del create. <p>
   * 
   * El <i>home</i> enviado ha de tener un método <code>create(Map )</code>.<br>
   *
   * @param home  El <i>home</i> sobre que se invoca el método de creación.
   * @param claseHome clase del home
   * @param mapa  Argumento enviado al método <code>create</code>.
   * @exception CreateException Se redireccion la del create original
   * @exception ValidationException Se redireccion la del create original
   * @exception RemoteException Se redireccion la del create original
   * @exception NoSuchMethodException Si el <i>home</i> no tiene ningún método <code>create(Map )</code>
   */
  public static Object create(Object home, Class claseHome, Map mapa) throws CreateException, ValidationException, RemoteException, NoSuchMethodException  {
	try {	  
	  Method m = claseHome.getDeclaredMethod("create", detailsClass2);
	  Object [] args = { mapa };
	  return m.invoke(home, args);
	}
	catch (InvocationTargetException ex) {
	  Throwable th = ex.getTargetException();
	  try {
		throw th;
	  }
	  catch (CreateException ex2) {
		throw ex2;
	  }
	  catch (ValidationException ex2) {
		throw ex2;
	  }
	  catch (RemoteException ex2) {
		throw ex2;
	  }
	  catch (Throwable ex2) {
		throw new RemoteException(ex2.getLocalizedMessage(), ex2);
	  }
	}
	catch (NoSuchMethodException ex) {
	  throw ex;
	}
	catch (Exception ex) {
	  throw new RemoteException(ex.getLocalizedMessage(), ex);
	}
  }
  
}
