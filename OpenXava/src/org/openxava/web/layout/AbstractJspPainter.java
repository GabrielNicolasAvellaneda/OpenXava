/**
 * 
 */
package org.openxava.web.layout;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.util.Messages;
import org.openxava.web.style.Style;

/**
 * Base painter for JSP type painters.
 * @author Federico Alcantara
 *
 */
public abstract class AbstractJspPainter extends AbstractBasePainter {
	private Log LOG = LogFactory.getLog(AbstractJspPainter.class);
	
	private Style style;
	private StringBuffer logMessage;
	protected Map<String, String> attributes = new HashMap<String, String>();
	protected int level = 0;
	private JspWriter writer = null;

	/**
	 * Writes to the page context output.
	 * @param value Value to be written.
	 */
	protected void write(String value) {
		try {
			getPageContext().getOut().print(value);
			if (LOG.isDebugEnabled()) {
				String[] tags = value.replaceAll("\\n", "").split(">");
				for (String tagValue : tags) {
					if (!tagValue.equals("")) {
						String tag = tagValue + ">";
						int closeBrackets = StringUtils.countMatches(tag, "</") * 2;
						int totalBrackets = StringUtils.countMatches(tag, "<");
						int difBrackets = totalBrackets - closeBrackets;
						if (difBrackets < 0) {
							level += difBrackets;
						}
						if (level < 0) {
							level = 0;
						}
						logMessage.append(StringUtils.repeat("  ", level))
								.append(tag)
								.append('\n');
						if (difBrackets > 0) {
							level += difBrackets;
						}
						if (level < 0) level = 0;
					}
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * Include a JSP page.
	 * 
	 * @param page. URL of page to be included in context page.
	 */
	protected void includeJspPage(String page) {
		try {
			getPageContext().include(page, true);
			if (LOG.isDebugEnabled()) {
				logMessage.append(StringUtils.repeat("  ", level))
						.append(page)
						.append('\n');
			}
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

	protected JspWriter getWriter() {
		if (writer == null) {
			writer = getPageContext().getOut();
		}
		return writer;
	}
	
	protected void resetLog() {
		logMessage = new StringBuffer("");
	}
	
	protected void outputLog() {
		if (LOG.isDebugEnabled() && logMessage != null) { 
			LOG.debug(logMessage.toString());
		}
	}
}
