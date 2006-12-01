package org.openxava.formatters;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.formatters.IFormatter;
import org.openxava.util.Align;
import org.openxava.util.Is;
import org.openxava.util.Strings;

/**
 * @author Ivan Martín
 */

public class TimeBaseFormatter implements IFormatter {
	
	private Log log = LogFactory.getLog(TimeBaseFormatter.class);
	
	public String format(HttpServletRequest request, Object time) {
		return time==null?"":time.toString();
	}
	
	public class TimeData {
		public int hours = 0;
		public int minutes = 0;
		public int seconds = 0;
		
		public TimeData(int hours, int minutes, int seconds) {
			this.hours = hours;
			this.minutes = minutes;
			this.seconds = seconds;
		}
		
	}
	
	public String sqlTimeFormat(TimeData timeData) {
		if (timeData == null) return "00:00:00";
		StringBuffer sb = new StringBuffer();
		sb.append(Strings.fix(String.valueOf(timeData.hours),2,Align.RIGHT,'0'));
		sb.append(":");
		sb.append(Strings.fix(String.valueOf(timeData.minutes),2,Align.RIGHT,'0'));
		sb.append(":");
		sb.append(Strings.fix(String.valueOf(timeData.seconds),2,Align.RIGHT,'0'));
		return sb.toString();
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
		        	return new TimeData(hours, minutes, 0);		            
		        }
            } else if(string.matches("^([0-9]{1,2})$")) {
		        // hh         
                int hours = Integer.parseInt(string);
                if(hours >= 0 && hours <= 23) {                	
                	return new TimeData(hours, 0, 0);
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
		        	return new TimeData(hours, minutes, 0);
		        }
            } else if(string.matches("^([0-9]{4})$")) {
                // hhmm                     
                int hours = Integer.parseInt(string.substring(0,2));
		        int minutes = Integer.parseInt(string.substring(2,4));
		        if(!(hours >= 0 && hours<=23 && minutes>=0 && minutes <=59)) {
	                throw new ParseException("bad_time_format",-1);
		        }
		        else {
		        	return new TimeData(hours, minutes, 0);
		        }
            }
            else if(string.matches("^([0-9]{1,2}):([0-9]{1,2}):([0-9]{1,2})$")) {
            	//hh:mm:ss
		        int hours = Integer.parseInt(string.replaceAll("^([0-9]{1,2}):([0-9]{1,2}):([0-9]{1,2})$","$1"));
		        int minutes = Integer.parseInt(string.replaceAll("^([0-9]{1,2}):([0-9]{1,2}):([0-9]{1,2})$","$2"));
		        int seconds = Integer.parseInt(string.replaceAll("^([0-9]{1,2}):([0-9]{1,2}):([0-9]{1,2})$","$3"));
		        if(!(hours >= 0 && hours<=23 && minutes>=0 && minutes <=59 && seconds >=0 && seconds <= 59)) {
		        	throw new ParseException("bad_time_format",-1);
		        }
		        else {
		        	return new TimeData(hours, minutes, seconds);
		        }
            }
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		throw new ParseException("bad_time_format",-1);
	}
	
}
