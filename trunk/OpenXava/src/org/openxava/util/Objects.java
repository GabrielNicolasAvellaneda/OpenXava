package org.openxava.util;

import java.io.*;
import java.lang.reflect.*;
/**
 * Clase de utilidad gen�rica para trabajar con objetos java. <p>
 * 
 * @author: Javier Paniza
 */
public class Objects {

  private static final Class [] arg0 = { };	
/**
 * Clona el objeto enviado. <p>
 *
 * La clonaci�n la hace usando el m�todo <tt>clone()</tt> del objeto
 * enviado si �ste implementa <tt>Cloneable</tt>.<br>
 *
 * @exception CloneNotSupportedException Si no el objeto no es <tt>Cloneable</tt>.<br>
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
	catch (Exception ex) { // Muy pocas veces ir� por aqu�
		ex.printStackTrace();
		throw new CloneNotSupportedException(XavaResources.getString("clone_error", o));
	}
}
/**
 * Hace un clon profundo del objeto enviado. <p>
 *
 * Si el argumento es nulo se devuelve nulo.<br>
 *
 * @param origen  Ha de ser serializable. No es necesario que implementa <tt>Cloneable</tt>.
 * @return Clon del objeto enviado. Hace un clon de todos los niveles, completo, profundo.
 * @exception ClonException  Alg�n problema al clonar.
 */
public static Object deepClone(Object origen) throws ClonException {
	try {
		if (origen == null) return null;
		// leemos
		ByteArrayOutputStream byteout = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteout);		
		out.writeObject(origen);
		byte [] buffer = byteout.toByteArray();
		byteout.close();
		out.close();
		// grabamos
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buffer));
		Object result = in.readObject();
		in.close();
		return result;
	}
	catch (Exception ex) {
		ex.printStackTrace();
		throw new ClonException(XavaResources.getString("deep_clone_error", origen));
	}
}

/**
 * Permite ejecutar de forma din�mica (como con SmallTalk) un m�todo en un objeto. <p>
 * 
 * @param o Objeto sobre el que se ejecuta el m�todo. No puede ser nulo.
 * @param nombreMetodo Nombre del m�todo a ejecutar.
 * @return  Resultado de la ejecuci�n del m�todo.
 * @exception Exception  Cualquier problema, incluyendo excepciones originales que lance el m�todo.
 * @exception NoSuchMethodException  Si el m�todo no existe en el objeto destino.
 */
public static Object execute(Object o, String nombreMetodo) throws Exception {
	try {
		Assert.arg(o);
		Method m = o.getClass().getMethod(nombreMetodo, arg0);
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
 * Permite ejecutar de forma din�mica (como con SmallTalk) un m�todo en un objeto con un argumento. <p>
 * 
 * @param o Objeto sobre el que se ejecuta el m�todo. No puede ser nulo.
 * @param methodName Nombre del m�todo a ejecutar. No puede ser nulo.
 * @param argumentClass No nulo.
 * @param arg Valor del argumento. Puede ser nulo.
 * @return  Resultado de la ejecuci�n del m�todo.
 * @exception Exception  Cualquier problema, incluyendo excepciones originales que lance el m�todo.
 * @exception NoSuchMethodException  Si el m�todo no existe en el objeto destino.
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
 * Intenta clonar el objeto enviado. <p>
 *
 * Si no lo consigue devuelve el elemento original.<br>
 *
 * La clonaci�n la hace usando el m�todo <tt>clone()</tt> del objeto
 * enviado si este implementa <tt>Cloneable</tt>.<br>
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
		throw new IllegalArgumentException("clone_required");
	}
	catch (Exception ex) {
		ex.printStackTrace();
		System.err.println("ADVERTENCIA: Imposible clonar " + o + " por " + ex.getMessage() + "Se devuelve elemento original");
		return o;
	}
}
}
