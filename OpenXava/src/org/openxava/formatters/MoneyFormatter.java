package org.openxava.formatters;

import java.math.*;
import java.text.*;

import javax.servlet.http.*;

import org.openxava.util.*;

/**
 * For MONEY and DINERO stereotypes. <p>
 * 
 * @author Javier Paniza
 */

public class MoneyFormatter implements IFormatter {

	public String format(HttpServletRequest request, Object object)	throws Exception {
		if (object == null) return "";
		return getFormat(request).format(object);
	}

	public Object parse(HttpServletRequest request, String string) throws Exception {
		if (Is.emptyString(string)) return null; 
		string = Strings.change(string, " ", ""); // In order to work with Polish		
		return new BigDecimal(getFormat(request).parse(string).toString()).setScale(2);
	}
	
	private NumberFormat getFormat(HttpServletRequest request) {
		NumberFormat f = DecimalFormat.getNumberInstance(request.getLocale());
		f.setMinimumFractionDigits(2);
		f.setMaximumFractionDigits(2);
		return f;
	}


}
