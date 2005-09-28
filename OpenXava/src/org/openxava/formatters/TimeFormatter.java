package org.openxava.formatters;

import java.text.*;

import javax.servlet.http.*;
import org.openxava.formatters.IFormatter;
import org.openxava.util.*;

/**
 * @author Ivan Martín
 */

public class TimeFormatter implements IFormatter {
	
	public String format(HttpServletRequest request, Object time) {
		return time==null?"":time.toString();
	}
		
	public Object parse(HttpServletRequest request, String string) throws ParseException {
		if(Is.emptyString(string)) return null;
		try {
		    if(string.matches("^([0-9]{1,2}):([0-9]{1,2})$")) {
		        // hh:mm
		        int hours = Integer.parseInt(string.replaceAll("^([0-9]{1,2}):([0-9]{1,2})$","$1"));
		        int minutes = Integer.parseInt(string.replaceAll("^([0-9]{1,2}):([0-9]{1,2})$","$2"));
		        if(!(hours >= 0 && hours<=23 && minutes>=0 && minutes <=59)) {
	                throw new ParseException("bad_time_format",-1);
		        } else {
		            return Strings.fix(String.valueOf(hours),2,Align.RIGHT,'0')+":"+Strings.fix(String.valueOf(minutes),2,Align.RIGHT,'0');
		        }
            } else if(string.matches("^([0-9]{1,2})$")) {
		        // hh         
                int hours = Integer.parseInt(string);
                if(hours >= 0 && hours <= 23) {
                    return Strings.fix(string,2,Align.RIGHT,'0')+":00";
                } else {
                    throw new ParseException("bad_time_format",-1);
                }
            } else if(string.matches("^([0-9]{3})$")) {
		        // hmm                
                int hours = Integer.parseInt(string.substring(0,1));
		        int minutes = Integer.parseInt(string.replaceAll("^([0-9]{1})([0-9]{2})$","$2"));
		        if(!(hours >= 0 && hours<=23 && minutes>=0 && minutes <=59)) {
	                throw new ParseException("bad_time_format",-1);
		        } else {
		            return Strings.fix(String.valueOf(hours),2,Align.RIGHT,'0')+":"+Strings.fix(String.valueOf(minutes),2,Align.RIGHT,'0');
		        }
            } else if(string.matches("^([0-9]{4})$")) {
                // hhmm                     
                int hours = Integer.parseInt(string.substring(0,2));
		        int minutes = Integer.parseInt(string.substring(2,4));
		        if(!(hours >= 0 && hours<=23 && minutes>=0 && minutes <=59)) {
	                throw new ParseException("bad_time_format",-1);
		        } else {
		            return Strings.fix(String.valueOf(hours),2,Align.RIGHT,'0')+":"+Strings.fix(String.valueOf(minutes),2,Align.RIGHT,'0');
		        }
            }
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
		throw new ParseException("bad_time_format",-1);
	}
	
}
