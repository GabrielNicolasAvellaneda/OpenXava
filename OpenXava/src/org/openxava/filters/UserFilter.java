package org.openxava.filters;

import java.util.*;
import javax.servlet.http.*;

/**
 * Inserts the name of the current user as first parameter. <p>
 * 
 * @author Javier Paniza
 */

public class UserFilter implements IRequestFilter {
	
	private HttpServletRequest request; 

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public Object filter(Object o) throws FilterException {
		if (o == null) {
			return new Object [] { getUser() };
		}		
		if (o instanceof Object []) {			
			List c = new ArrayList(Arrays.asList((Object []) o));
			c.add(0, getUser());
			return c.toArray();			
		} 
		else {
			return new Object [] { getUser(), o	};
		}		
	}

	private String getUser() {
		return request.getRemoteUser();
	}

}
