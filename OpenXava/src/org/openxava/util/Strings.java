package org.openxava.util;
import java.math.*;
import java.util.*;

/**
 * Utilidades genéricas para trabajar con <tt>String</tt>. <p>
 *
 * @version 00.09.16
 * @author  Javier Paniza
 */

public class Strings {

	

/**
 * Corta hasta el tamaño especificado. <p>
 *
 * Devuelve una cadena con la cadena enviada pero el tamaño especificado. <br>
 * Corta la cadena según la alineación.<br>
 *
 *
 * ¡¡¡OJO, SI ESTE metodo SE HACE PUBLICO REVISAR ESTA PRECONDICION!!!
 * <b>Precondición:</b>
 * <ul>
 * <li><tt>cadena.length() > tamaño</tt>
 * </ul>
 * 
 * @return Nunca será nulo
 * @param cadena No puede ser nulo. ¡¡¡OJO!!! Puede cambiar si se publica el método
 * @param tamaño Número de caracteres hasta que se tiene que cortar.
 * @param alineacion No puede ser nulo. ¡¡¡OJO!!! Puede cambiar si se publica el método
 */
private static String cut(String cadena, int tamaño, Align alineacion) {
	int c = tamaño - cadena.length();
	StringBuffer result = crearSB(c, ' ');
	if (alineacion.isIzquierda()) {
		return cadena.substring(0, tamaño);
	}
	else if (alineacion.isRight()) {
		int t = cadena.length();
		return cadena.substring(t - tamaño , t);

	}
	else if (alineacion.isCenter()) {
		int iz = (cadena.length() - tamaño)/2;		
		return cadena.substring(iz, iz + tamaño);
	}
	else {
		Assert.fail("Alineación " + alineacion.getDescription() + " no soportada en Cadenas.cortar");
	}
	return result.toString();	
}
/**
 * Crea una cadena con la cantidad de espacios en blanco especificada.
 * 
 * @param cantidad  Cantidad de espacios.
 * @return No será nulo.
 */
public static String spaces(int cantidad) {
	return crearSB(cantidad, ' ').toString();
}
/**
 * Crea un <tt>StringBuffer</tt> con la cantidad de espacios en blanco especificada.
 * 
 * @param cantidad  Cantidad de espacios.
 * @return No será nulo.
 */
private static StringBuffer crearSB(int cantidad, char caracter) {
	StringBuffer result = new StringBuffer();
	for (int i = 0; i < cantidad; i++){
		result.append(caracter);
	}
	return result;
}
/**
 * Fija el tamaño de la cadena rellenando con espacios si hace falta. <p>
 *
 * Devuelve una cadena con la cadena enviada pero el tamaño especificado. <br>
 * Rellena espacios o corta según necesite.<br>
 * 
 * @return Nunca será nulo
 * @param cadena Puede ser nulo, en cuyo caso se toma por cadena vacía.
 * @param tamaño Número de caracteres hasta que se tiene que rellenar o cortar.
 * @param alineacion No puede ser nulo.
 */
public static String fix(String cadena, int tamaño, Align alineacion) {
	return fix(cadena, tamaño, alineacion, ' ');
}

/**
 * Fija el tamaño de la cadena rellenando con el caracter especificado si hace falta. <p>
 *
 * Devuelve una cadena con la cadena enviada pero el tamaño especificado. <br>
 * Rellena o corta según necesite.<br>
 * 
 * @return Nunca será nulo
 * @param cadena Puede ser nulo, en cuyo caso se toma por cadena vacía.
 * @param tamaño Número de caracteres hasta que se tiene que rellenar o cortar.
 * @param alineacion No puede ser nulo.
 * @param caracterRelleno Caracter usado para rellenar 
 */
public static String fix(String cadena, int tamaño, Align alineacion, char caracterRelleno) {
	if (tamaño < 0) 
		throw new IllegalArgumentException(XavaResources.getString("size_in_Strings_fix_not_negative"));
	cadena = cadena == null?"":cadena.trim();
	int t = cadena.length();
	if (t < tamaño) return rellenar(cadena, tamaño, alineacion, caracterRelleno);    
	if (t == tamaño) return cadena;
	return cut(cadena, tamaño, alineacion); // if (t > tamaño)
}

/**
 * Rellena hasta el tamaño especificado. <p>
 *
 * Devuelve una cadena con la cadena enviada pero el tamaño especificado. <br>
 * Rellena con el caracter indicado si hace falta, pero si sobran no corta la cadena.<br>
 *
 * ¡¡¡OJO, SI ESTE metodo SE HACE PUBLICO REVISAR ESTA PRECONDICION!!! 
 *  
 * <b>Precondición:</b>
 * <ul>
 * <li><tt>tamaño > cadena.length()</tt>
 * </ul>
 * 
 * @return Nunca será nulo
 * @param cadena No puede ser nulo. ¡¡¡OJO!!! Puede cambiar si se publica el método
 * @param tamaño Número de caracteres hasta que se tiene que rellenar.
 * @param alineacion No puede ser nulo.
 * @param caracterRelleno Caracter usado para rellenar
 */
private static String rellenar(String cadena, int tamaño, Align alineacion, char caracterRelleno) {	
	int c = tamaño - cadena.length();
	StringBuffer result = crearSB(c, caracterRelleno);
	if (alineacion.isIzquierda()) {
		result.insert(0, cadena);		
	}
	else if (alineacion.isRight()) {		
		result.append(cadena);

	}
	else if (alineacion.isCenter()) {
		int iz = c/2;		
		result.insert(iz, cadena);
	}
	else {
		Assert.fail("Alineación " + alineacion.getDescription() + " no soportada en Cadenas.rellenar");
	}
	return result.toString();	
}
/**
 * Crea una cadena formada por la repetición de otra cadena. <p>
 * 
 * @param cantidad  Cantidad de repeticiones.
 * @param cadena    Cadena a repetir.
 * @return No será nulo.
 */
public static String repeat(int cantidad, String cadena) {	
	return repetirSB(cantidad, cadena).toString();
}
/**
 * Crea un <tt>StringBuffer</tt> formado por la repetición de una cadena. <p>
 * 
 * @param cantidad  Cantidad de repeticiones.
 * @param cadena    Cadena a repetir.
 * @return No será nulo.
 */
private static StringBuffer repetirSB(int cantidad, String cadena) {
	StringBuffer result = new StringBuffer();
	for (int i = 0; i < cantidad; i++){
		result.append(cadena);
	}
	return result;
}
  /**
   * Convierte una lista de elementos separados por comas en un array de cadenas. <p>
   *
   * Por ejemplo, la lista <i>Angel, Manolo, Antonia</i> se convierte en
   * un array de 3 elementos con los tres nombres sin comas ni espacios.<br>
   *
   * @param lista  Una cadena con una lista. Si se envía nulo se devuelve un
   *               array vacío.
   * @return Nunca será nulo, aunque <tt>lista == null</tt>.
   */
  public final static String [] toArray(String lista) {
	Collection c = toCollection(lista);
	String [] rs = new String[c.size()];
	c.toArray(rs);
	return rs;
  }
  /**
   * Convierte una lista de elementos separados por un separador en un array. <p>
   *
   * Por ejemplo, si usamos los <i>:</> (dos puntos) como separador la lista
   * <i>Angel : Manolo : Antonia</i> se convierte en un array de 3
   * elementos con los tres nombres sin comas ni espacios.<br>
   *
   * @param lista  Una cadena con una lista. Si se envía nulo se devuelve un
   *               array vacío.
   * @param separador  Separador.
   * @return Nunca será nulo, aunque <tt>lista == null</tt>.
   * @exception IllegalArgumentException  Si <tt>separador == null</tt>.
   */
  public final static String [] toArray(String lista, String separador) {
	Collection c = toCollection(lista, separador);
	String [] rs = new String[c.size()];
	c.toArray(rs);
	return rs;
  }
  /**
   * Convierte una lista de elementos separados por comas en una colección. <p>
   *
   * Por ejemplo, la lista <i>Angel, Manolo, Antonia</i> se convierte en
   * una colección de 3 elementos con los tres nombres sin comas ni espacios.<br>
   *
   * @param lista  Una cadena con una lista. Si se envía nulo se devuelve una
   *               colección vacía.
   * @return Nunca será nulo, aunque <tt>lista == null</tt>.
   */
  public final static Collection toCollection(String lista) {
	return toCollection(lista, ",");
  }
  /**
   * Convierte una lista de elementos separados por un separador en una colección. <p>
   *
   * Por ejemplo, si usamos los <i>:</> (dos puntos) como separador la lista
   * <i>Angel : Manolo : Antonia</i> se convierte en una colección de 3
   * elementos con los tres nombres sin comas ni espacios.<br>
   *
   * @param lista  Una cadena con una lista. Si se envía nulo se devuelve una
   *               colección vacía.
   * @param separador  Separador.
   * @return Nunca será nulo, aunque <tt>lista == null</tt>.
   * @exception IllegalArgumentException  Si <tt>separador == null</tt>.
   */
  public final static Collection toCollection(String lista, String separador) {
	Assert.arg(separador);
	Vector rs = new Vector();
	if (lista == null) return rs;
	StringTokenizer st = new StringTokenizer(lista, separador);
	while (st.hasMoreTokens()) {
	  rs.add(st.nextToken().trim());
	}
	return rs;
  }
  
  /**
   * Convierte la cadena en un objeto del tipo especificado. <p>
   * 
   * Soporta todos los tipos primitivos junto con sus 
   * envoltorios java excepto char y void.<br>
   * Tambien soporta <tt>String</tt> y <tt>BigDecimal</tt>.<p>
   * 
   * Si se produce un error en la conversion o no es un tipo
   * soportado devulve nulo o el valor por defecto del tipo (un cero
   * si es numerico). <p> 
   * 
   * @param tipo El tipo del objeto devuelto (puede ser un tipo primitivo
   *             en cuyo caso lo devuelve envuelto). No puede ser nulo.
   * @param cadena La cadena con el dato a convertir. Puede ser nulo en cuyo
   *               caso coge valores por defecto.
   */
  public final static Object toObject(Class tipo, String cadena) {
  	try {
	  	if (tipo.equals(String.class)) return cadena;
	  	
	  	if (tipo.equals(Integer.TYPE) || tipo.equals(Integer.class)) {
	  		try {
		  		if (Is.emptyString(cadena)) return new Integer(0);
		  		return new Integer(cadena.trim());
	  		}
	  		catch (NumberFormatException ex) {
				System.err.println("No es posible convertir " + cadena + " en un " + tipo + " se asume el valor cero");				  			
	  			return new Integer(0);
	  		}
	  	}
	  	
	  	if (tipo.equals(java.math.BigDecimal.class)) {
	  		try {	  		
		  		if (Is.emptyString(cadena)) return new BigDecimal("0.00");
		  		return new BigDecimal(cadena.trim());
	  		}
	  		catch (NumberFormatException ex) {
				System.err.println("No es posible convertir " + cadena + " en un " + tipo + " se asume el valor cero");				  			
	  			return new BigDecimal("0.00");
	  		}		  		
	  	}
	  	
	  	if (tipo.equals(Double.TYPE) || tipo.equals(Double.class)) {
	  		try {	  		
		  		if (Is.emptyString(cadena)) return new Double(0);
		  		return new Double(cadena.trim());
	  		}
	  		catch (NumberFormatException ex) {
				System.err.println("No es posible convertir " + cadena + " en un " + tipo + " se asume el valor cero");				  			
	  			return new Double(0);
	  		}		  				  		
	  	}
	  	
	  	if (tipo.equals(Long.TYPE) || tipo.equals(Long.class)) {
	  		try {	  		
		  		if (Is.emptyString(cadena)) return new Long(0);
		  		return new Long(cadena.trim());
	  		}
	  		catch (NumberFormatException ex) {
				System.err.println("No es posible convertir " + cadena + " en un " + tipo + " se asume el valor cero");				  			
	  			return new Long(0);
	  		}		  				  		
	  	}
	  	
	  	if (tipo.equals(Float.TYPE) || tipo.equals(Float.class)) {
	  		try {	  		
		  		if (Is.emptyString(cadena)) return new Float(0);
		  		return new Float(cadena.trim());
	  		}
	  		catch (NumberFormatException ex) {
				System.err.println("No es posible convertir " + cadena + " en un " + tipo + " se asume el valor cero");				  			
	  			return new Float(0);
	  		}		  				  		
	  	}
	  	
	  	if (tipo.equals(Short.TYPE) || tipo.equals(Short.class)) {
	  		try {	  		
		  		if (Is.emptyString(cadena)) return new Short((short)0);
		  		return new Short(cadena.trim());
	  		}
	  		catch (NumberFormatException ex) {
				System.err.println("No es posible convertir " + cadena + " en un " + tipo + " se asume el valor cero");				  			
	  			return new Short((short)0);
	  		}		  				  		
	  	}
	  	
	  	if (tipo.equals(Byte.TYPE) || tipo.equals(Byte.class)) {
	  		try {	  		
		  		if (Is.emptyString(cadena)) return new Byte((byte)0);
		  		return new Byte(cadena.trim());
	  		}
	  		catch (NumberFormatException ex) {
				System.err.println("No es posible convertir " + cadena + " en un " + tipo + " se asume el valor cero");				  			
	  			return new Byte((byte)0);
	  		}		  				  		
	  	}
	  	
	  	if (tipo.equals(Boolean.TYPE) || tipo.equals(Boolean.class)) {
	  		try {	  		
		  		if (Is.emptyString(cadena)) return new Boolean(false);
		  		return new Boolean(cadena.trim());
	  		}
	  		catch (NumberFormatException ex) {
				System.err.println("No es posible convertir " + cadena + " en un " + tipo + " se asume el valor cero");				  			
	  			return new Boolean(false);
	  		}		  				  		
	  	}
  	}
  	catch (Exception ex) {
  		ex.printStackTrace();
  		System.err.println("No es posible convertir " + cadena + " en un " + tipo);
  	}  	
  	return null;

  }
  
  	/**
  	 * Devuelve una cadena como la enviada pero con la primera en mayuscula. <p>
  	 * 
  	 * Si se envia nulo se devuelve nulo.
  	 */
	public static String firstUpper(String s) {
		if (s==null) return null;
		if (s.length() == 0) return "";
		return s.substring(0, 1).toUpperCase() + s.substring(1);		
	}

  	/**
  	 * Devuelve una cadena como la enviada pero con la primera en minuscula. <p>
  	 * 
  	 * Si se envia nulo se devuelve nulo.
  	 */	
	public static String firstLower(String s) {
		if (s==null) return null;
		if (s.length() == 0) return "";
		return s.substring(0, 1).toLowerCase() + s.substring(1);
	}
	
	/**
	 * Cambia las cadenas contenidas en las claves por sus
	 * correspondientes valores.<p>
	 * 
	 * @param aCambiar No puede ser nulo.
	 */
	public static String change(String cadena, Map aCambiar) {
		Iterator it = aCambiar.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry en = (Map.Entry) it.next();
			cadena = change(cadena, (String) en.getKey(), (String)en.getValue());
		}
		return cadena;				
	}
	
	/**
	 * Cambia en <tt>cadena</tt> <tt>original</tt> por <tt>nueva</tt>. <p>
	 * @param cadena Cadena en la que hacemos el cambio. Nunca nulo.
	 * @param original Cadena a buscar. Nunca nulo.
	 * @param nueva Nuevo valor para la cadena búscada. Nunca nulo.
	 * @return String
	 */
	public static String change(String cadena, String original, String nueva) {				
		int i = cadena.indexOf(original);
		if (i < 0) return cadena; // así evitamos crear un StringBuffer
		StringBuffer sb = new StringBuffer(cadena);		
		while (i >= 0) {
			int f = i + original.length();			
			sb.replace(i, f, nueva);			
			i = sb.toString().indexOf(original, i + nueva.length());
		}		
		return sb.toString();
	} 
  
  
}
