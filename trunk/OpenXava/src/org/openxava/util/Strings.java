package org.openxava.util;
import java.math.*;
import java.util.*;

/**
 * Utilities to work with <code>String</code>. <p>
 *
 * @author  Javier Paniza
 */

public class Strings {

	/**
	 * Cut to specified length. <p> 
	 *
	 * Returns a string with the sent string but with specified length. <br>
	 * Cut the string according align. <br>
	 *
	 * WARNING!!! IF THIS method GO TO PUBLIC REVIEW THIS PRECONDITION!!!
	 * 
	 * <b>Precondition:</b>
	 * <ul>
	 * <li><tt>string.length() > length</tt>
	 * </ul>
	 * 
	 * @return Not null
	 * @param string Not null. WARNING!!! This can change if method is maked public
	 * @param length Number the characters to cut.
	 * @param align Not null. WARNING!!! This can change if method is maked public
	 */
	private static String cut(String string, int length, Align align) {
		int c = length - string.length();
		StringBuffer result = createSB(c, ' ');
		if (align.isLeft()) {
			return string.substring(0, length);
		}
		else if (align.isRight()) {
			int t = string.length();
			return string.substring(t - length , t);
	
		}
		else if (align.isCenter()) {
			int le = (string.length() - length)/2;		
			return string.substring(le, le + length);
		}
		else {
			Assert.fail(XavaResources.getString("align_not_supported", align.getDescription()));
		}
		return result.toString();	
	}
	
	/**
	 * Creates a string with the specified blank spaces. <p> 
	 * 
	 * @param count  Quantity of spaces
	 * @return Not null.
	 */
	public static String spaces(int count) {
		return createSB(count, ' ').toString();
	}
	
	/**
	 * Creates a <tt>StringBuffer</tt> with character speciefied repeated the count specified.
	 * 
	 * @param count  Times to repeat
	 * @param character  Character to repeat
	 * @return  Not null
	 */
	private static StringBuffer createSB(int count, char character) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < count; i++){
			result.append(character);
		}
		return result;
	}
	
	/**
	 * Fix the length of the string filling with spaces if needed. <p> 
	 *
	 * Returns the sent string but with the specified length. <br>
	 * It fills or cuts as it needs. 
	 * 
	 * @param string  Can be null, in which case empty string is assumed 
	 * @param length  Character count of result string
	 * @param align Not null
	 * @return Not null
	 */
	public static String fix(String string, int length, Align align) {
		return fix(string, length, align, ' ');
	}

	/**
	 * Fix the length of the string filling with the specified character if needed. <p> 
	 *
	 * Returns the sent string but with the specified length. <br>
	 * It fills or cuts as it needs. 
	 * 
	 * @param string  Can be null, in which case empty string is assumed 
	 * @param length  Character count of result string
	 * @param align Not null
	 * @param fillCharacter  Character used to fill
	 * @return Not null
	 */
	public static String fix(String string, int length, Align align, char fillCharacter) {
		if (length < 0) 
			throw new IllegalArgumentException(XavaResources.getString("size_in_Strings_fix_not_negative"));
		string = string == null?"":string.trim();
		int t = string.length();
		if (t < length) return fill(string, length, align, fillCharacter);    
		if (t == length) return string;
		return cut(string, length, align); // if (t > length)
	}

	/**
	 * Fills to specefied length. <p> 
	 * 
	 * Returns the sent string but with the specified length. <br>
	 * Fills with the specified charactare as needed but it does not cut the string. <p> 
	 *
	 * WARNING!!! IF THIS method GO TO PUBLIC REVIEW THIS PRECONDITION!!! 
	 *  
	 * <b>Precondition:</b>
	 * <ul>
	 * <li><tt>length > string.length()</tt>
	 * </ul>
	 * 
	 * @return  Not null
	 * @param string  Cannot be null. WARNING!!! This can change if the method is maked public
	 * @param length  Length of result string
	 * @param align  Not null
	 * @param fillCharacter  Character used to fill
	 */
	private static String fill(String string, int length, Align align, char fillCharacter) {	
		int c = length - string.length();
		StringBuffer result = createSB(c, fillCharacter);
		if (align.isLeft()) {
			result.insert(0, string);		
		}
		else if (align.isRight()) {		
			result.append(string);
	
		}
		else if (align.isCenter()) {
			int iz = c/2;		
			result.insert(iz, string);
		}
		else {
			Assert.fail(XavaResources.getString("align_not_supported", align.getDescription()));
		}
		return result.toString();	
	}

	/**
	 * Creates a string from repeating another string. <p> 
	 * 
	 * @param count  Times to repeat
	 * @param string  String to repeat
	 * @return Not null
	 */
	public static String repeat(int count, String string) {	
		return repeatSB(count, string).toString();
	}

	/**
	 * Create a <code>StringBuffer</tt> repeating a string. <p> 
	 * 
	 * @param count  Times to repeat
	 * @param string  String to repeat
	 * @return Not null
	 */
	private static StringBuffer repeatSB(int count, String string) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < count; i++){
			result.append(string);
		}
		return result;
	}
	
  /**
   * Converts a list of comma separated elements in a string array. <p> 
   *
   * For example, the list <i>Angel, Manolo, Antonia</i> is converted to
   * a array of 3 elements with this 3 names without comman nor spaces.<br>
   *
   * @param list  String with the list. If null return a empty string
   * @return Not null, including the case <tt>list == null</tt>.
   */
  public final static String [] toArray(String list) {
		Collection c = toCollection(list);
		String [] rs = new String[c.size()];
		c.toArray(rs);
		return rs;
  }
  
  /**
   * Converts a list of elements separated by a arbitrary character 
   * in a string array. <p> 
   *
   * For example, the list <i>Angel : Manolo : Antonia</i> is converted to
   * a array of 3 elements with this 3 names without colon (for example) nor spaces.<br>
   *
   * @param list  String with the list. If null return a empty string
   * @param separator  The character used as separator.
   * @return Not null, including the case <tt>list == null</tt>.
   */
  public final static String [] toArray(String list, String separator) {
		Collection c = toCollection(list, separator);
		String [] rs = new String[c.size()];
		c.toArray(rs);
		return rs;
  }
  
  /**
   * Converts a list of comma separated elements in a string collection. <p> 
   *
   * For example, the list <i>Angel, Manolo, Antonia</i> is converted to
   * a collection of 3 elements with this 3 names without comman nor spaces.<br>
   *
   * @param list  String with the list. If null return a empty string
   * @return Not null, including the case <tt>list == null</tt>.
   */
  public final static Collection toCollection(String list) {
  	return toCollection(list, ",");
  }
  
  /**
   * Converts a list of elements separated by a arbitrary character 
   * in a string collection. <p> 
   *
   * For example, the list <i>Angel : Manolo : Antonia</i> is converted to
   * a collection of 3 elements with this 3 names without colon (for example) nor spaces.<br>
   *
   * @param list  String with the list. If null return a empty string
   * @param separator  The character used as separator.
   * @return Not null, including the case <tt>list == null</tt>.
   */
  public final static Collection toCollection(String list, String separator) {
		Assert.arg(separator);
		Collection rs = new ArrayList();
		if (list == null) return rs;
		StringTokenizer st = new StringTokenizer(list, separator);
		while (st.hasMoreTokens()) {
		  rs.add(st.nextToken().trim());
		}
		return rs;
  }
 
  /**
   * Converts a collection of objects in a string of comma separated elements. <p> 
   *
   * For example, a collection of 3 elements with 3 names 
   * is converted to the string <i>Angel, Manolo, Antonia</i> <br>
   *
   * @param collection  Collection with the elements. If null return a empty string
   * @return Not null, including the case <tt>list == null</tt>.
   */
  public final static String toString(Collection collection) {
  	return toString(collection, ",");
  }
  
  /**
   * Converts a collection of objects in string of elements separated by a 
   * arbitrary character . <p> 
   *
   * For example, a collection of 3 elements with this 3 names
   * is converted to a string of 3 elements with this 3 names and colon (for example).<br>
   *
   * @param collection  A collection of objects. If null return a empty string
   * @param separator  The character used as separator.
   * @return Not null, including the case <tt>list == null</tt>.
   */
  public final static String toString(Collection collection, String separator) {
		Assert.arg(separator);
		StringBuffer cad = new StringBuffer();
		if (collection == null) return "";
		Iterator itCollections = collection.iterator();	
		while (itCollections.hasNext()) {	
			cad.append(itCollections.next());
			if (itCollections.hasNext()) {
				cad.append(separator);
			}	
		}	
		return cad.toString();
  }
 
   
  /**
   * Converts a string a object of the specified type. <p>
   * 
   * Supports all primitive type plus its wrappers except char and void.<br>
   * Support <code>String</code> and <code>BigDecimal</code> too. <p>
   * 
   * If there is conversion error or is a type not supporte
   * return null or the default value for the type (zero for numeric). <p>
   * 
   * @param type  The type of returned object (can be a primitive type
   * 							in this case return its wrapper). Not null
   * @param string  String with data to convert. Can be null, in this case
   * 		return a default value.
   */
  public final static Object toObject(Class type, String string) {
  	try {
	  	if (type.equals(String.class)) return string;
	  	
	  	if (type.equals(Integer.TYPE) || type.equals(Integer.class)) {
	  		try {
		  		if (Is.emptyString(string)) return new Integer(0);
		  		return new Integer(string.trim());
	  		}
	  		catch (NumberFormatException ex) {	  			
	  			System.err.println(XavaResources.getString("string_convesion_zero_assumed_warning", string, type));				  			
	  			return new Integer(0);
	  		}
	  	}
	  	
	  	if (type.equals(java.math.BigDecimal.class)) {
	  		try {	  		
		  		if (Is.emptyString(string)) return new BigDecimal("0.00");
		  		return new BigDecimal(string.trim());
	  		}
	  		catch (NumberFormatException ex) {
	  			System.err.println(XavaResources.getString("string_convesion_zero_assumed_warning", string, type));				  			
	  			return new BigDecimal("0.00");
	  		}		  		
	  	}
	  	
	  	if (type.equals(Double.TYPE) || type.equals(Double.class)) {
	  		try {	  		
		  		if (Is.emptyString(string)) return new Double(0);
		  		return new Double(string.trim());
	  		}
	  		catch (NumberFormatException ex) {
	  			System.err.println(XavaResources.getString("string_convesion_zero_assumed_warning", string, type));				  			
	  			return new Double(0);
	  		}		  				  		
	  	}
	  	
	  	if (type.equals(Long.TYPE) || type.equals(Long.class)) {
	  		try {	  		
		  		if (Is.emptyString(string)) return new Long(0);
		  		return new Long(string.trim());
	  		}
	  		catch (NumberFormatException ex) {
	  			System.err.println(XavaResources.getString("string_convesion_zero_assumed_warning", string, type));				  			
	  			return new Long(0);
	  		}		  				  		
	  	}
	  	
	  	if (type.equals(Float.TYPE) || type.equals(Float.class)) {
	  		try {	  		
		  		if (Is.emptyString(string)) return new Float(0);
		  		return new Float(string.trim());
	  		}
	  		catch (NumberFormatException ex) {
	  			System.err.println(XavaResources.getString("string_convesion_zero_assumed_warning", string, type));				  			
	  			return new Float(0);
	  		}		  				  		
	  	}
	  	
	  	if (type.equals(Short.TYPE) || type.equals(Short.class)) {
	  		try {	  		
		  		if (Is.emptyString(string)) return new Short((short)0);
		  		return new Short(string.trim());
	  		}
	  		catch (NumberFormatException ex) {
	  			System.err.println(XavaResources.getString("string_convesion_zero_assumed_warning", string, type));				  			
	  			return new Short((short)0);
	  		}		  				  		
	  	}
	  	
	  	if (type.equals(Byte.TYPE) || type.equals(Byte.class)) {
	  		try {	  		
		  		if (Is.emptyString(string)) return new Byte((byte)0);
		  		return new Byte(string.trim());
	  		}
	  		catch (NumberFormatException ex) {
	  			System.err.println(XavaResources.getString("string_convesion_zero_assumed_warning", string, type));				  			
	  			return new Byte((byte)0);
	  		}		  				  		
	  	}
	  	
	  	if (type.equals(Boolean.TYPE) || type.equals(Boolean.class)) {	  			  		
		  	if (Is.emptyString(string)) return new Boolean(false);
		  	return new Boolean(string.trim());	  				  				  		
	  	}
        
        if (type.equals(Object.class)) {
            return string;
        }
        
  	}
  	catch (Exception ex) {
  		ex.printStackTrace();
  		System.err.println(XavaResources.getString("string_convesion_warning", string, type));
  	}  	
  	return null;
  }
  
	/**
	 * Returns a string like the sent one but with the first letter in uppercase. <p> 
	 * 
	 * If null is sent null is returned.
	 */
	public static String firstUpper(String s) {
		if (s==null) return null;
		if (s.length() == 0) return "";
		return s.substring(0, 1).toUpperCase() + s.substring(1);		
	}

	/**
	 * Returns a string like the sent one but with the first letter in lowercase. <p> 
	 * 
	 * If null is sent null is returned.
	 */
	public static String firstLower(String s) {
		if (s==null) return null;
		if (s.length() == 0) return "";
		return s.substring(0, 1).toLowerCase() + s.substring(1);
	}
	
	/**
	 * In the sent string changes the strings in toChange map
	 * for its values. <p>
	 * 
	 * @param string Not null
	 * @param toChagne Not null 
	 */
	public static String change(String string, Map toChange) {
		Iterator it = toChange.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry en = (Map.Entry) it.next();
			string = change(string, (String) en.getKey(), (String)en.getValue());
		}
		return string;				
	}
	
	/**
	 * Change in <code>string</tt> <code>original</code> by <code>newString</code>. <p>
	 * 
	 * @param string  String in which we make the changes. Not null
	 * @param original  String to search. Not null
	 * @param newString  New value to put in place of original. Not null
	 * @return String
	 */
	public static String change(String string, String original, String newString) {				
		int i = string.indexOf(original);
		if (i < 0) return string; 
		StringBuffer sb = new StringBuffer(string);		
		while (i >= 0) {
			int f = i + original.length();			
			sb.replace(i, f, newString);			
			i = sb.toString().indexOf(original, i + newString.length());
		}		
		return sb.toString();
	} 
	
	public static String lastToken(String string) {
		StringTokenizer st = new StringTokenizer(string);
		String r = null;
		while (st.hasMoreTokens()) r = st.nextToken();
		return r;
	}
	
	/**
	 * 
	 * @return If string if null or have no tokens returns empty string.
	 */
	public static String firstToken(String string, String delim) {
		if (string == null) return "";
		StringTokenizer st = new StringTokenizer(string, delim);		
		if (st.hasMoreTokens()) return st.nextToken().trim();
		return "";
	}
	
    
}
