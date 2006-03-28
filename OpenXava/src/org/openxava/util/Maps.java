package org.openxava.util;

import java.util.*;

/**
 * Utilities to work with maps. <p> 
 * 
 * @author Javier Paniza
 */
public class Maps {

	/**
	 * Create a clone. <p>
	 *
	 * Tries to make a clone using the <code>clone()</code> method of
	 * the origin if this is <code>Cloneable</tt>, otherwise do a manual clon
	 * of the map, without clone the elements (it's to say, it does <b>not</b>
	 * make a <b>deep</b> clone).<br>
	 * 
	 * @return Can be null (if the original is null), and the same type of original.
	 * @param m  Original map to clone. Has to be <tt>Cloneable</tt> or has a
	 * 																	default contructor and implementing <code>putAll</code> method.
	 * @exception IllegalArgumentException If precondition is broken
	 */
	public static Map clone(Map m) {
		if (m == null)
			return null;
		try {
			return (Map) Objects.clone(m);
		}
		catch (CloneNotSupportedException ex) {
			return manualClone(m);
		}
	}
	
	/**
	 * Obtain a value in a map with nested maps from a qualified name.
	 * 
	 * For example:
	 * <code>(((Map) mymap.get("a")).get("b")).get("c")</code>
	 * is equal to:
	 * <code>Maps.getValueFromQualifiedName(mymap, "a.b.c")</code> 
	 * 
	 * @param tree Map with map in some values, hence in tree-form.
	 * @param qualifiedName Name in form a.b.c.  
	 */
	public static Object getValueFromQualifiedName(Map tree, String qualifiedName) {
		int idx = qualifiedName.indexOf('.'); 
		if (idx < 0) return tree.get(qualifiedName);
		Map subtree = (Map) tree.get(qualifiedName.substring(0, idx));
		if (subtree == null) return null;
		return  getValueFromQualifiedName(subtree, qualifiedName.substring(idx + 1));		
	}
		
	/**
	 * Makes a clone of map manually. <p>  
	 * 
	 * @return <tt>instanceof  origen.getClass()</tt>
	 * @param origin  Cannot be null.  Must to have a default constructor 
	 */
	private static Map manualClone(Map origin) {
		try {
			Map result = (Map) origin.getClass().newInstance();
			result.putAll(origin);
			return result;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException(XavaResources.getString("clone_map_require_default_constructor"));
		}
	}
	
	/**
	 * Does a recursive clone of map. <p>
	 * 
	 * A recursive clone means that if some value is a map itself this method is
	 * applied to it. 
	 * 
	 * @return <tt>instanceof  origen.getClass()</tt>
	 * @param origin Cannot be null. Must to have a default constructor 
	 */
	public static Map recursiveClone(Map origin) {
		try {
			Map result = (Map) origin.getClass().newInstance();
			Iterator it = origin.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry en = (Map.Entry) it.next();
				Object value = en.getValue();
				if (value instanceof Map) {
					result.put(en.getKey(), recursiveClone((Map)value));
				}
				else {
					result.put(en.getKey(), value);
				}
			}
			return result;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException(XavaResources.getString("clone_map_require_default_constructor"));
		}
	}
	
	
	/**
	 * It's empty if is null, without elements, with null elements or
	 * elements with neutral value (empty strings, collections, nulls). <p>
	 * 
	 * Numeric values with value 0 not are considered empty. <br>
	 * 
	 * @param values Can be null.
	 */
	public static boolean isEmpty(Map values) {
		if (values == null) return true;
		if (values.size() == 0) return true;
		Iterator it = values.values().iterator();
		while (it.hasNext()) {
			Object value = it.next();					
			if (value instanceof String) {
				 if (!((String) value).trim().equals("")) return false;
			}
			else if (value instanceof Map) {				
				if (!isEmpty((Map) value)) return false;				
			}
			else if (value instanceof Collection) {
				 if (((Collection) value).size() > 0) return false;			
			}
			else if (value != null) return false;			 			
		}		
		return true;
	}
	
	/**
	 * Converts a plain map (without levels) in a tree map. <p>
	 * 
	 * That is, convert:
	 * <pre>
	 * {invoice.year=2006, invoice.number=1, number=3}
	 * </pre>
	 * in
	 * <pre>
	 * {invoice={year=2006, number=1}, number=3}
	 * </pre>
	 *  
	 * @param plainMap This argument is not changed. The keys must be strings.
	 * @return A map with the data in tree format.
	 */
	public static Map plainToTree(Map plainMap) {
		Map result = new HashMap();
		for (Iterator it = plainMap.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			int idx = key.indexOf('.'); 
			if (idx < 0) {
				result.put(key, plainMap.get(key));
			}
			else {
				String branchName = key.substring(0, idx);
				String subKey = key.substring(idx + 1);
				Map branch = (Map) result.get(branchName);
				if (branch == null) {
					branch = new HashMap();
					result.put(branchName, branch);
				}
				branch.put(subKey, plainMap.get(key));
			}
		}
		
		// Next level
		for (Iterator it = plainMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry en = (Map.Entry) it.next();
			if (en.getValue() instanceof Map) {
				result.put(en.getKey(), plainToTree((Map) en.getValue()));
			}
		}
		
		return result;
	}
	
}
