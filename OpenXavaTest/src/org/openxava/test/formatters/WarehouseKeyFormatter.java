package org.openxava.test.formatters;

import java.text.*;
import java.util.*;

import javax.servlet.http.*;

import org.openxava.formatters.*;
import org.openxava.test.ejb.*;
import org.openxava.util.*;


/**
 * @author Javier Paniza
 */

public class WarehouseKeyFormatter implements IFormatter {

	private final static String BAD_STRING =
		"String for create WarehouseKey must have format: 'Warehouse: zoneNumber/number'";
		

	public String format(HttpServletRequest request,	Object object) {		
		return object==null?"":object.toString();
	}
	
	public Object parse(HttpServletRequest request, String string) throws ParseException {
		if (Is.emptyString(string) || "0".equals(string)) {
			return null; 
		}		
		StringTokenizer st = new StringTokenizer(string, "[.");
		if (!st.hasMoreTokens()) {
			throw new ParseException(BAD_STRING, 0);			
		}
		String szoneNumber = st.nextToken().trim();
		if (!st.hasMoreTokens()) {
			throw new ParseException(BAD_STRING, 0);			
		}
		String snumber = st.nextToken().trim();
		
		WarehouseKey key = new WarehouseKey();
		try {
			key.zoneNumber  = Integer.parseInt(szoneNumber);
			key._Number = new Integer(snumber);
		}
		catch (NumberFormatException ex) {
			throw new ParseException("Impossible to parse WarehouseKey: zoneNumber and number must be numerics", 0);			
		}		
				
		return key;
		
	}

}
