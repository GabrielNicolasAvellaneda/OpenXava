package org.openxava.formatters;

import java.text.*;

import javax.servlet.http.*;

import org.openxava.util.*;

/**
 * Date formatter with multilocale support. <p>
 * 
 * Although it does some refinement in Spanish case, it support formatting
 * on locale basis.<br>
 *  
 * @author Javier Paniza
 */

public class DateFormatter implements IFormatter {
	
	private static DateFormat spanishDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	private static DateFormat [] spanishDateFormats = {
		new SimpleDateFormat("dd/MM/yy"), 
		new SimpleDateFormat("ddMMyy"),
		new SimpleDateFormat("dd.MM.yy")				
	};
	
	public String format(HttpServletRequest request, Object date) {
		if (date == null) return "";
		if (Dates.getYear((java.util.Date)date) < 2) return "";
		return getDateFormat().format(date);
	}
		
	public Object parse(HttpServletRequest request, String string) throws ParseException {
		if (Is.emptyString(string)) return null;				
		if (!"sv".equals(Locales.getCurrent().getLanguage())) { // Patch by Apoteket: bad for Swedish format
			if (string.indexOf('-') >= 0) { // SimpleDateFormat does not work well with -
				string = Strings.change(string, "-", "/");
			}		
		}
		DateFormat [] dateFormats = getDateFormats(); 
		for (int i=0; i<dateFormats.length; i++) {
			try {
				dateFormats[i].setLenient(false);
				return dateFormats[i].parseObject(string);
			}
			catch (ParseException ex) {
			}						
		}
		throw new ParseException(XavaResources.getString("bad_date_format",string),-1);
	}
	
	private DateFormat getDateFormat() {
		if ("es".equals(Locales.getCurrent().getLanguage()) ||
				"pl".equals(Locales.getCurrent().getLanguage())) return spanishDateFormat;
		return DateFormat.getDateInstance(DateFormat.SHORT, Locales.getCurrent());		
	}
	
	private DateFormat[] getDateFormats() {
		if ("es".equals(Locales.getCurrent().getLanguage()) ||
				"pl".equals(Locales.getCurrent().getLanguage())) return spanishDateFormats;
		return new DateFormat [] { getDateFormat() };
	}
		
}
