package org.openxava.formatters;

import java.text.*;

import javax.servlet.http.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.util.*;

/**
 * @author José Luis Santiago
 */

public class DateTimeFormatter implements IMultipleValuesFormatter {
	
	private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private static DateFormat dateOnlyFormat = new SimpleDateFormat("dd/MM/yyyy");
	private static DateFormat timeOnlyFormat = new SimpleDateFormat("HH:mm:ss");
	private Log log = LogFactory.getLog(DateTimeFormatter.class);
	
	private static DateFormat [] dateFormats = {
		dateFormat,
		new SimpleDateFormat("dd/MM/yyyy HH:mm"),
		new SimpleDateFormat("ddMMyyyy HH:mm"),
		new SimpleDateFormat("ddMMyyyy HH:mm:ss"),
		new SimpleDateFormat("dd.MM.yyyy HH:mm"),		
		new SimpleDateFormat("dd.MM.yyyy HH:mm:ss"),		
		new SimpleDateFormat("dd/MM/yyyy"),		
		new SimpleDateFormat("ddMMyyyy"),		
		new SimpleDateFormat("dd.MM.yyyy"),		
	};	
	
	public String [] format(HttpServletRequest request, Object date) throws Exception {	
        String[] result = new String[2];
        result[0] = "";
        result[1] = "";
		if (date == null) return result;
		result[0] = dateOnlyFormat.format(date);
		result[1] = timeOnlyFormat.format(date);
		return result;
	}

	public Object parse(HttpServletRequest request, String [] strings) throws Exception {		
		if( strings == null ) return null;
		if( strings.length < 2 ) return null;
		if( Is.emptyString(strings[0])) return null;
		if( Is.emptyString(strings[1])) return null;
	
		String fDate = strings[0];
		String fTime = strings[1];
		String string = fDate + " " + fTime;
	
        // SimpleDateFormat does not work well with -
		if (string.indexOf('-') >= 0) { 
			string = Strings.change(string, "-", "/");
		}
		
		for (int i=0; i < dateFormats.length; i++) {
			try {
				java.util.Date result =  (java.util.Date) dateFormats[i].parseObject(string);				
				return new java.sql.Timestamp( result.getTime() );
			}
			catch (ParseException ex) {
			}						
		}
		throw new ParseException(XavaResources.getString("bad_date_format",string),-1);
	}
}
