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
	
	public String format(HttpServletRequest request, Object fecha) {
		if (fecha == null) return "";
		if (Dates.getYear((java.util.Date)fecha) < 2) return "";
		return dateFormat.format(fecha);
	}
		
	public Object parse(HttpServletRequest request, String cadena) throws ParseException {
		if (Is.emptyString(cadena)) return null;
		if (cadena.indexOf('-') >= 0) { // SimpleDateFormat no va bien con -
			cadena = Strings.change(cadena, "-", "/");
		}
		for (int i=0; i<dateFormats.length; i++) {
			try {
				return dateFormats[i].parseObject(cadena);
			}
			catch (ParseException ex) {
			}						
		}
		throw new ParseException(XavaResources.getString("bad_date_format",cadena),-1);
	}
	
}
