/*
 * Created on 27/10/2006
 * @author Miguel Angel Embuena
 */
package org.openxava.formatters;

import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SQLTimeFormatter extends TimeBaseFormatter {
	
	private static Log log = LogFactory.getLog(SQLTimeFormatter.class);
	
	public Object parse(HttpServletRequest request, String string) throws ParseException {
		TimeData timeData = (TimeData)super.parse(request, string);
		String timeString = sqlTimeFormat(timeData); 
		return java.sql.Time.valueOf(timeString);
	}

}
