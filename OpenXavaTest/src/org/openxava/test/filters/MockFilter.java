package org.openxava.test.filters;

import java.util.*;

import org.openxava.filters.*;


/**
 * Only to test context filter, but it do not filter
 * 
 * @author Javier Paniza
 */

public class MockFilter extends BaseContextFilter {

	public Object filter(Object o) throws FilterException {
		getDefaultZone(); // testing if works base context
		return o;
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
