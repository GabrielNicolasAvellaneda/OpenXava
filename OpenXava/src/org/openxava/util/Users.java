package org.openxava.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.*;

/**
 * Utilities to work with users. <p>
 * 
 * @author Javier Paniza
 */

public class Users {
	
	private static Log log = LogFactory.getLog(Users.class);
	private static ThreadLocal current = new ThreadLocal();
	private static ThreadLocal currentUserInfo = new ThreadLocal();
		
	/**
	 * The user name associated to the current thread. <p>
	 * 
	 * @return <code>null</code> if no user logged.
	 */
	public static String getCurrent() {
		return (String) current.get();
	}
	
	/**
	 * Info about the current logged user. <p>
	 * 
	 * It only returns meaningful info if we are inside a portal.<br>
	 * 
	 * @return Not null. If not info available it returns a UserInfo object with no info.
	 */
	public static UserInfo getCurrentUserInfo() {
		UserInfo userInfo = (UserInfo) currentUserInfo.get();
		if (userInfo == null) userInfo = new UserInfo();
		userInfo.setId(getCurrent());
		return userInfo;
	}	
	
	/**
	 * Associated an user to the current thread. <p>
	 */	
	public static void setCurrent(String userName) {
		current.set(userName);
		currentUserInfo.set(null); 
	}
	
	/**
	 * Associated the user of the request to the current thread. <p>
	 * 
	 * Takes into account JetSpeed 1.5 user managament, althought
	 * it's generic enought to work in any servlet container.
	 */
	public static void setCurrent(HttpServletRequest request) {		
        Object rundata = request.getAttribute("rundata");
        String portalUser = (String) request.getAttribute("xava.portal.user");
        String user = portalUser == null?request.getRemoteUser():portalUser;
        if (Is.emptyString(user) && rundata != null) {
			PropertiesManager pmRundata = new PropertiesManager(rundata);
			try {
				// Using introspection for not link OpenXava to turbine and jetspeed1.x
				// This is temporal. In future JSR-168 compatible, and remove this code 
				Object jetspeedUser = pmRundata.executeGet("user");
				PropertiesManager pmUser = new PropertiesManager(jetspeedUser);
				user = (String) pmUser.executeGet("userName");
			}
			catch (Exception ex) {				
				log.warn(XavaResources.getString("warning_get_user"),ex);
				user = null;
			}			
		}		
		current.set(user);
		request.getSession().setAttribute("xava.user", user);
				
		currentUserInfo.set(request.getAttribute("xava.portal.userinfo")); 
	}

} 
