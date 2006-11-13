/*
 * Created on 27/10/2006
 * @author Miguel Angel Embuena
 */
package org.openxava.formatters;

import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;

public class StringTimeFormatter extends TimeBaseFormatter {
	
	public Object parse(HttpServletRequest request, String string) throws ParseException {
		TimeData timeData = (TimeData)super.parse(request, string);
		return sqlTimeFormat(timeData);
	}

}
