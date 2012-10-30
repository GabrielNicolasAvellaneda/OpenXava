package org.openxava.formatters;

import javax.servlet.http.*;

/**
 * tmp
 * 
 * @author Javier Paniza
 */

public class CustomReportComparatorListFormatter implements IFormatter {

	public String format(HttpServletRequest request, Object object) 	throws Exception {
		if (object == null) return "";
		if (object.equals("eq")) return "=";
		if (object.equals("ne")) return "<>";
		if (object.equals("ge")) return ">=";
		if (object.equals("le")) return "<=";
		if (object.equals("gt")) return ">";
		if (object.equals("lt")) return "<";
		if (object.equals("starts_comparator")) return "starts with";			
		if (object.equals("contains_comparator")) return "contains";
		if (object.equals("not_contains_comparator")) return "does not contain";
		return object.toString();
	}

	public Object parse(HttpServletRequest request, String string) throws Exception {
		return null;
	}

}
