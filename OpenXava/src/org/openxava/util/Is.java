package org.openxava.util;

/**
  Clase de utilidad para reducir el tamaño de los ifs. <p>
  Util para implementar afirmaciones (invariantes, precondiciones, postcondiciones).

  Por ejemplo:
  <pre>
  if (nombre != null || nombre.trim().equals("") ||
	  apellido1 != null || apellido1.trim().equals("")) ||
	  apellido2 != null || apellido2.trim().equals(""))
  {
	hacerAlgo();
  }
  </pre>
  Se puede escribir:
  <pre>
  if (Es.cadVacia(nombre, apellido1, apellido2)) {
	hacerAlgo();
  }
  </pre>

  <i>Nota:</i> El <code>Es</code> es del verbo ser en español.<br>


  @version 1.0 (19/11/1999)
  @author  Javier Paniza
*/


public class Is {

  /**
  Comprueba si la cadena enviada es <code>null</code>,
  o cadena vacía.
  @cadena cadena a comprobar
  @return <code>true</code> si es cadena vacía.
  */
  public final static boolean emptyString(String cadena) {
	if (cadena == null || cadena.trim().equals(""))
	  return true;
	return false;
  }
  /**
  Comprueba si alguna de las 2 cadenas enviadas es <code>null</code>,
  o cadena vacía.
  @cadena1 cadena a comprobar
  @cadena2 cadena a comprobar
  @return <code>true</code> si hay alguna cadena vacía.
  */
  public final static boolean emptyString(String cadena1, String cadena2) {
	if (cadena1 == null || cadena1.trim().equals(""))
	  return true;
	if (cadena2 == null || cadena2.trim().equals(""))
	  return true;
	return false;
  }
  /**
  Comprueba si alguna de las 3 cadenas enviadas es <code>null</code>,
  o cadena vacía.
  @cadena1 cadena a comprobar
  @cadena2 cadena a comprobar
  @cadena3 cadena a comprobar
  @return <code>true</code> si hay alguna cadena vacía.
  */
  public final static boolean emptyString(String cadena1, String cadena2, String cadena3) {
	if (cadena1 == null || cadena1.trim().equals(""))
	  return true;
	if (cadena2 == null || cadena2.trim().equals(""))
	  return true;
	if (cadena3 == null || cadena3.trim().equals(""))
	  return true;
	return false;
  }
  /**
  Comprueba si alguna de las 4 cadenas enviadas es <code>null</code>,
  o cadena vacía.
  @cadena1 cadena a comprobar
  @cadena2 cadena a comprobar
  @cadena3 cadena a comprobar
  @cadena4 cadena a comprobar
  @return <code>true</code> si hay alguna cadena vacía.
  */
  public final static boolean emptyString(String cadena1, String cadena2, String cadena3, String cadena4) {
	if (cadena1 == null || cadena1.trim().equals(""))
	  return true;
	if (cadena2 == null || cadena2.trim().equals(""))
	  return true;
	if (cadena3 == null || cadena3.trim().equals(""))
	  return true;
	if (cadena4 == null || cadena4.trim().equals(""))
	  return true;
	return false;
  }
  /**
  Comprueba si alguna de las 5 cadenas enviadas es <code>null</code>,
  o cadena vacía.
  @cadena1 cadena a comprobar
  @cadena2 cadena a comprobar
  @cadena3 cadena a comprobar
  @cadena4 cadena a comprobar
  @cadena5 cadena a comprobar
  @return <code>true</code> si hay alguna cadena vacía.
  */
  public final static boolean emptyString(String cadena1, String cadena2, String cadena3, String cadena4, String cadena5) {
	if (cadena1 == null || cadena1.trim().equals(""))
	  return true;
	if (cadena2 == null || cadena2.trim().equals(""))
	  return true;
	if (cadena3 == null || cadena3.trim().equals(""))
	  return true;
	if (cadena4 == null || cadena4.trim().equals(""))
	  return true;
	if (cadena5 == null || cadena5.trim().equals(""))
	  return true;
	return false;
  }
  /**
  Comprueba si alguna de las 6 cadenas enviadas es <code>null</code>,
  o cadena vacía.
  @cadena1 cadena a comprobar
  @cadena2 cadena a comprobar
  @cadena3 cadena a comprobar
  @cadena4 cadena a comprobar
  @cadena5 cadena a comprobar
  @cadena6 cadena a comprobar
  @return <code>true</code> si hay alguna cadena vacía.
  */
  public final static boolean emptyString(String cadena1, String cadena2, String cadena3, String cadena4, String cadena5, String cadena6) {
	if (cadena1 == null || cadena1.trim().equals(""))
	  return true;
	if (cadena2 == null || cadena2.trim().equals(""))
	  return true;
	if (cadena3 == null || cadena3.trim().equals(""))
	  return true;
	if (cadena4 == null || cadena4.trim().equals(""))
	  return true;
	if (cadena5 == null || cadena5.trim().equals(""))
	  return true;
	if (cadena6 == null || cadena6.trim().equals(""))
	  return true;
	return false;
  }
/**
 * Si <tt>a</tt> es igual a <tt>b</tt>. <p>
 *
 * Controla los nulos.<br>
 * Usa <tt>compare</tt> si <tt>a</tt> implementa <tt>Comparable</tt>.<br>
 * 
 * @return boolean
 * @param a Puede ser nulo.
 * @param b Puede ser nulo.
 */
public final static boolean equal(Object a, Object b) {
	if (a == null) return b == null;
	if (b == null) return false;
	if (a instanceof Comparable) {
		return ((Comparable) a).compareTo(b) == 0;
	}
	return a.equals(b);
}

/**
 * Si <tt>a.toString().trim()</tt> es igual a <tt>b.toString().trim()</tt>. <p>
 *
 * Controla los nulos.<br> 
 * 
 * @return boolean
 * @param a Puede ser nulo.
 * @param b Puede ser nulo.
 */
public final static boolean equalAsString(Object a, Object b) {
	a = a==null?"":a.toString().trim();
	b = b==null?"":b.toString().trim();
	return a.equals(b);
}

/**
 * Si <tt>a.toString().trim()</tt> es igual a <tt>b.toString().trim()</tt> sin
 * tener en cuenta mayúsculas y minúsculas. <p>
 *
 * Controla los nulos.<br> 
 * 
 * @return boolean
 * @param a Puede ser nulo.
 * @param b Puede ser nulo.
 */
public final static boolean equalAsStringIgnoreCase(Object a, Object b) {
	String sa = a == null?"":a.toString().trim();
	String sb = b == null?"":b.toString().trim();
	return sa.equalsIgnoreCase(sb);
}


  /**
  Comprueba si las 2 cadenas enviadas son <code>null</code>,
  o cadena vacía.
  @cadena1 cadena a comprobar
  @cadena2 cadena a comprobar
  @return <code>true</code> si todas las cadenas son vacías.
  */
  public final static boolean emptyStringAll(String cadena1, String cadena2) {
	return (cadena1 == null || cadena1.trim().equals(""))
	    && (cadena2 == null || cadena2.trim().equals(""));
  }
  /**
  Comprueba si las 3 cadenas enviadas son <code>null</code>,
  o cadena vacía.
  @cadena1 cadena a comprobar
  @cadena2 cadena a comprobar
  @cadena3 cadena a comprobar  
  @return <code>true</code> si todas las cadenas son vacías.
  */
  public final static boolean emptyStringAll(String cadena1, String cadena2, String cadena3) {
	return (cadena1 == null || cadena1.trim().equals(""))
	    && (cadena2 == null || cadena2.trim().equals(""))
   	    && (cadena3 == null || cadena3.trim().equals(""));
  }
  /**
  Comprueba si las 4 cadenas enviadas son <code>null</code>,
  o cadena vacía.
  @cadena1 cadena a comprobar
  @cadena2 cadena a comprobar
  @cadena3 cadena a comprobar
  @cadena4 cadena a comprobar    
  @return <code>true</code> si todas las cadenas son vacías.
  */
  public final static boolean emptyStringAll(String cadena1, String cadena2, String cadena3, String cadena4) {
	return (cadena1 == null || cadena1.trim().equals(""))
	    && (cadena2 == null || cadena2.trim().equals(""))
   	    && (cadena3 == null || cadena3.trim().equals(""))
   	    && (cadena4 == null || cadena4.trim().equals(""));   	    
  }
  /**
  Comprueba si las 5 cadenas enviadas son <code>null</code>,
  o cadena vacía.
  @cadena1 cadena a comprobar
  @cadena2 cadena a comprobar
  @cadena3 cadena a comprobar
  @cadena4 cadena a comprobar
  @cadena5 cadena a comprobar      
  @return <code>true</code> si todas las cadenas son vacías.
  */
  public final static boolean emptyStringAll(String cadena1, String cadena2, String cadena3, String cadena4, String cadena5) {
	return (cadena1 == null || cadena1.trim().equals(""))
	    && (cadena2 == null || cadena2.trim().equals(""))
   	    && (cadena3 == null || cadena3.trim().equals(""))
   	    && (cadena4 == null || cadena4.trim().equals(""))
   	    && (cadena5 == null || cadena5.trim().equals(""));   	       	    
  }
  /**
  Comprueba si las 6 cadenas enviadas son <code>null</code>,
  o cadena vacía.
  @cadena1 cadena a comprobar
  @cadena2 cadena a comprobar
  @cadena3 cadena a comprobar
  @cadena4 cadena a comprobar
  @cadena5 cadena a comprobar
  @cadena6 cadena a comprobar        
  @return <code>true</code> si todas las cadenas son vacías.
  */
  public final static boolean emptyStringAll(String cadena1, String cadena2, String cadena3, String cadena4, String cadena5, String cadena6) {
	return (cadena1 == null || cadena1.trim().equals(""))
	    && (cadena2 == null || cadena2.trim().equals(""))
   	    && (cadena3 == null || cadena3.trim().equals(""))
   	    && (cadena4 == null || cadena4.trim().equals(""))
   	    && (cadena5 == null || cadena5.trim().equals(""))
   	    && (cadena6 == null || cadena6.trim().equals(""));   	       	       	    
  }
}
