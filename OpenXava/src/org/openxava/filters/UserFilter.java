package org.openxava.filters;

import java.util.*;

import org.openxava.util.*;

/**
 * Inserts the name of the current user as first parameter. <p>
 * 
 * @author Javier Paniza
 */

public class UserFilter implements IFilter {
	
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
		String user = Users.getCurrent();
		System.out.println("[UserFilter.getUser] Users.getCurrent()=" + user); //  tmp
		return user;
	}

}
