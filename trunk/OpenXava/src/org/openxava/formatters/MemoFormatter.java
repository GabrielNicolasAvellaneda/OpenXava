package org.openxava.formatters;

import javax.servlet.http.HttpServletRequest;

import org.openxava.formatters.IFormatter;
import org.openxava.util.Is;

/**
 * Created on 15/07/2009
 * @autor Ana Andres
 */
public class MemoFormatter implements IFormatter{

	private int maxLength = 50;
	
	public String format(HttpServletRequest request, Object object) throws Exception {
		String text = Is.empty(object) ? "" : object.toString();
		return text.length() > getMaxLength() ?
			"<abbr title='" + text + "'>" + text.substring(0, getMaxLength()) + "..." + "</abbr>" : 
			text;
	}

	public Object parse(HttpServletRequest request, String string) throws Exception {
		return null;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	
}
