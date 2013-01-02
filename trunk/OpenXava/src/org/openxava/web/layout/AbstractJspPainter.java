/**
 * 
 */
package org.openxava.web.layout;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.openxava.util.Messages;
import org.openxava.web.style.Style;

/**
 * Base painter for JSP type painters.
 * @author Federico Alcantara
 *
 */
public abstract class AbstractJspPainter extends AbstractBasePainter {
	private Style style;
	protected Map<String, String> attributes = new HashMap<String, String>();
	protected int level = 0;

	/**
	 * Writes to the page context output.
	 * @param value Value to be written.
	 */
	protected void write(String value) {
		try {
			getPageContext().getOut().print(value);
			
			String[] values = value.split(">");
			for (String aValue : values) {
				String cValue = aValue + ">";
				int closeBrackets = StringUtils.countMatches(cValue, "</") * 2;
				int totalBrackets = StringUtils.countMatches(cValue, "<");
				int difBrackets = totalBrackets - closeBrackets;
				if (difBrackets < 0) {
					level += difBrackets;
				}
				if (level < 0) level = 0;
				System.out.print(StringUtils.repeat("  ", level));
				System.out.println(cValue);
				if (difBrackets > 0) {
					level += difBrackets;
				}
				if (level < 0) level = 0;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Include a JSP page.
	 * 
	 * @param page. URL of page to be included in context page.
	 */
	protected void includeJspPage(String page) {
		try {
			getPageContext().include(page);
			System.out.println(page);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return The current request.
	 */
	protected HttpServletRequest getRequest() {
		return (HttpServletRequest) getPageContext().getRequest();
	}
	
	/**
	 * 
	 * @return The errors messages.
	 */
	protected Messages getErrors() {
		return (Messages)getRequest().getAttribute("errors");
	}
	
	/**
	 * @return The current style.
	 */
	protected Style getStyle() {
		if (style == null) {
			style = (Style)getRequest().getAttribute("style");
		}
		return style;
	}

	

}
