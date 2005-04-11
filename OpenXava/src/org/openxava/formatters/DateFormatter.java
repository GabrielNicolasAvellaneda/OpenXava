package org.openxava.formatters;

import java.text.*;

import javax.servlet.http.*;

import org.openxava.util.*;

/**
 * @author Javier Paniza
 */

public class DateFormatter implements IFormatter {
	
	private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	private static DateFormat [] dateFormats = {
		dateFormat,
		new SimpleDateFormat("ddMMyyyy"),
		new SimpleDateFormat("dd.MM.yyyy"),		
	};	
	
	public String format(HttpServletRequest request, Object date) {
		if (date == null) return "";
		if (Dates.getYear((java.util.Date)date) < 2) return "";
		return dateFormat.format(date);
	}
		
	public Object parse(HttpServletRequest request, String string) throws ParseException {
		if (Is.emptyString(string)) return null;
		if (string.indexOf('-') >= 0) { // SimpleDateFormat does not work well with -
			string = Strings.change(string, "-", "/");
		}
		for (int i=0; i<dateFormats.length; i++) {
			try {
				return dateFormats[i].parseObject(string);
			}
			catch (ParseException ex) {
			}						
		}
		throw new ParseException(XavaResources.getString("bad_date_format",string),-1);
	}
	
}
