package org.openxava.util;

import java.util.*;

/**
 * Utilities to work with collections, enumerations and iterators. <p> 
 * 
 * @author Javier Paniza
 */
public class XCollections {
	

	/**
	 * Returns the last element of sent collection. <p>
	 * @param collection  Cannot be null
	 */
	public static Object last(Collection collection) {
		List list = collection instanceof List?(List) collection:new ArrayList(collection);
		return list.get(list.size() - 1);
	}


	/**
	 * Adds elements from the enumeration to the collection. <p>
	 * 	 
	 * @param collection Not null
	 * @param toAdd  If null no elements are added.
	 */
	public static void add(Collection collection, Enumeration toAdd) {
		Assert.arg(collection);	
		if (toAdd == null) return;
		while (toAdd.hasMoreElements()) {
			collection.add(toAdd.nextElement());
		}	
	}
	
	/**
	 * Print in standard output the collection elements. <p> 
	 * 
	 * Util to debug.<br>
	 * @param c  Can be null.
	 */
	public static void println(Collection c) {
		if (c == null) return;
		println(c.iterator());
	}
	
	/**
	 * Print in standard output the elements by it iterate. <p> 
	 * 
	 * Util to debug.<br>
	 * @param c  Can be null.
	 */
	public static void println(Iterator it) {
		if (it == null) return;
		while (it.hasNext()) {
			System.out.println(" - " + it.next());
		}
	}
	
	/**
	 * Returns a collection from a enumeration. <p>
	 * 
	 * @return  Not null.
	 * @param e  If null then returns a empty collection
	 */
	public Collection toCollection(Enumeration e) {
		Collection result = new ArrayList();
		if (e == null) return result;
		while (e.hasMoreElements()) {
			result.add(e.nextElement());
		}	
		return result;
	}
	
}
