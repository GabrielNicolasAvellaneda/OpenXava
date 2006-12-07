package org.openxava.filters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Put a % before and after.
 * 
 * @author Javier Paniza
 */
public class TotalLikeFilter implements IFilter {
	
	private static Log log = LogFactory.getLog(TotalLikeFilter.class);
	
	/**
	 * @see org.openxava.filters.IFilter#filter(java.lang.Object)
	 */
	public Object filter(Object o) throws FilterException {
		if (o == null) return "%";
		return "%" + o.toString() + "%";
	}


}
