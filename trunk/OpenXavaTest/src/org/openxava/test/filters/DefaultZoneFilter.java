package org.openxava.test.filters;

import java.util.*;

import org.openxava.filters.*;


/**
 * @author Javier Paniza
 */

public class DefaultZoneFilter extends BaseContextFilter {

	public Object filter(Object o) throws FilterException {		
		if (o == null) {
			return new Object [] { getDefaultZone() };
		}		
		if (o instanceof Object []) {			
			List c = new ArrayList(Arrays.asList((Object []) o));
			c.add(0, getDefaultZone());
			return c.toArray();			
		} 
		else {
			return new Object [] { getDefaultZone(), o	};
		}		
	}

	private Integer getDefaultZone() throws FilterException {
		try {
			return getInteger("xavatest_defaultZone");			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new FilterException("Impossible to obtain default zone associated to session");
		}
	}
	
}
