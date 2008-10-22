package org.openxava.formatters;

import java.text.*;

import javax.servlet.http.*;

import org.openxava.util.*;


/**
 * Date/Time (combined) formatter with multilocale support. <p>
 *
 * Although it does some refinement in Spanish case, it support formatting
 * on locale basis.<br>
 *
 * @author Peter Smith
 */

public class DateTimeCombinedFormatter implements IFormatter {

	private static DateFormat spanishDateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	private static DateFormat [] spanishDateTimeFormats = {
		spanishDateTimeFormat,
		new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"),
		new SimpleDateFormat("dd/MM/yyyy HH:mm"),
		new SimpleDateFormat("ddMMyyyy HH:mm"),
		new SimpleDateFormat("ddMMyyyy HH:mm:ss"),
		new SimpleDateFormat("dd.MM.yyyy HH:mm"),		
		new SimpleDateFormat("dd.MM.yyyy HH:mm:ss"),		
		new SimpleDateFormat("dd/MM/yyyy"),		
		new SimpleDateFormat("ddMMyyyy"),		
		new SimpleDateFormat("dd.MM.yyyy")	};

	public String format(HttpServletRequest request, Object date) {
		if (date == null) return "";
		if (Dates.getYear((java.util.Date)date) < 2) return "";
		return getDateTimeFormat(request).format(date);
	}

	public Object parse(HttpServletRequest request, String string) throws ParseException {
		if (Is.emptyString(string)) return null;
		if (string.indexOf('-') >= 0) { // SimpleDateFormat does not work well with -
			string = Strings.change(string, "-", "/");
		}
		DateFormat [] dateFormats = getDateTimeFormats(request);
		for (int i=0; i<dateFormats.length; i++) {
			try {
				java.util.Date result = (java.util.Date) dateFormats[i].parseObject(string);
				return new java.sql.Timestamp( result.getTime() );
			}
			catch (ParseException ex) {
			}
		}
		throw new ParseException(XavaResources.getString("bad_date_format",string),-1);
	}

	private DateFormat getDateTimeFormat(HttpServletRequest request) {
		if ("es".equals(request.getLocale().getLanguage()) ||
				"pl".equals(request.getLocale().getLanguage())) return spanishDateTimeFormat;
		return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, request.getLocale());
	}

	private DateFormat[] getDateTimeFormats(HttpServletRequest request) {
		if ("es".equals(request.getLocale().getLanguage()) || 
				"pl".equals(request.getLocale().getLanguage())) return spanishDateTimeFormats;
		return new DateFormat [] { getDateTimeFormat(request) };
	}

}
