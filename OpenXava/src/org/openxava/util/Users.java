package org.openxava.util;

/**
 * Utilities to work with users. <p>
 * 
 * @author Javier Paniza
 */

public class Users {
	
	private static ThreadLocal current = new ThreadLocal();
	
	/**
	 * The user name associated to the current thread. <p>
	 * 
	 * @return <code>null</code> if no user logged.
	 */
	public static String getCurrent() {
		return (String) current.get();
	}
	/**
	 * Associated an user to the current thread. <p>
	 */	
	public static void setCurrent(String userName) {
		current.set(userName);
	}

}
