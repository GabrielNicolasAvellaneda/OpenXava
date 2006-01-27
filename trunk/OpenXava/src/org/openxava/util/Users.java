package org.openxava.util;

import javax.servlet.http.HttpServletRequest;

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
	
	/**
	 * Associated the user of the request to the current thread. <p>
	 * 
	 * Takes into account JetSpeed 1.5 user managament, althought
	 * it's generic enought to work in any servlet container.
	 */
	public static void setCurrent(HttpServletRequest request) {
        Object rundata = request.getAttribute("rundata");
		if (!Is.emptyString(request.getRemoteUser())) {
			current.set(request.getRemoteUser());
		}
		else if (rundata != null) {
			PropertiesManager pmRundata = new PropertiesManager(rundata);
			try {
				// Using introspection for no link OpenXava to turbine and jetspeed1.x
				// This is temporal. In future JSR-168 compatible, and remove this code 
				Object jetspeedUser = pmRundata.executeGet("user");
				PropertiesManager pmUser = new PropertiesManager(jetspeedUser);
				current.set((String) pmUser.executeGet("userName"));
			}
			catch (Exception ex) {				
				ex.printStackTrace(); 
				System.err.println(XavaResources.getString("warning_get_user"));
				current.set(null);
			}			
		}
		else {
			current.set(null);
		}
	}


} 
