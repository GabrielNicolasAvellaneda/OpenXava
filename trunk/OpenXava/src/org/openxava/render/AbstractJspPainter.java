/**
 * 
 */
package org.openxava.render;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.openxava.util.Messages;
import org.openxava.web.style.Style;

/**
 * Base painter for JSP type painters.
 * @author Federico Alcantara
 *
 */
public abstract class AbstractJspPainter extends AbstractBasePainter {
	private static final long serialVersionUID = 1L;

	private Style style;
	protected Map<String, String> attributes = new HashMap<String, String>();

	/**
	 * Writes to the page context output.
	 * @param value Value to be written.
	 */
	protected void write(String value) {
		try {
			getPageContext().getOut().print(value);
			System.out.println(value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Include JSP page.
	 * 
	 * @param page. Page to be included in context page.
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
	 * @return the current request.
	 */
	protected HttpServletRequest getRequest() {
		return (HttpServletRequest) getPageContext().getRequest();
	}
	
	protected Messages getErrors() {
		return (Messages)getRequest().getAttribute("errors");
	}
	/**
	 * @return the current style
	 */
	public Style getStyle() {
		if (style == null) {
			style = (Style)getRequest().getAttribute("style");
		}
		return style;
	}

	

}
