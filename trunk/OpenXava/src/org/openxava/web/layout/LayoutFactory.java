/**
 * 
 */
package org.openxava.web.layout;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.util.Is;
import org.openxava.util.XavaPreferences;

/**
 * Factory to get instances of layout parsers and layout painter.
 * The instances are session dependent.
 * @author Federico Alcantara
 *
 */
public class LayoutFactory {
	private static final Log LOG = LogFactory.getLog(LayoutFactory.class);
	
	/**
	 * 
	 * @return A single instance of the layout parser. Might return null.
	 */
	public static ILayoutParser getLayoutParserInstance(HttpServletRequest request) {
		ILayoutParser instance = (ILayoutParser) request.getSession().getAttribute(LayoutKeys.LAYOUT_MANAGER_INSTANCE);
		if (instance == null) {
			instance = getLayoutParserInstance();
			if (instance != null) {
				request.getSession().setAttribute(LayoutKeys.LAYOUT_MANAGER_INSTANCE, instance);
			}
		}
		return instance;
	}

	/**
	 * 
	 * @return A single instance of a layout painter. Might return null.
	 */
	public static ILayoutPainter getLayoutPainterInstance(HttpServletRequest request) {
		ILayoutPainter instance = (ILayoutPainter) request.getSession().getAttribute(LayoutKeys.LAYOUT_PAINTER_INSTANCE);
		if (instance == null) {
			instance = getLayoutPainterInstance();
			if (instance != null) {
				request.getSession().setAttribute(LayoutKeys.LAYOUT_PAINTER_INSTANCE, instance);
			}
		}
		return instance;
	}
	
	
	/**
	 * 
	 * @return An instance of the layout parser. Might return null.
	 */
	private static ILayoutParser getLayoutParserInstance() {
		ILayoutParser instance = null;
		String layoutParserName = XavaPreferences.getInstance().getLayoutParser();
		if (!Is.emptyString(layoutParserName)) {
			try {
				instance = (ILayoutParser)Class.forName(layoutParserName).newInstance();
			} catch (ClassNotFoundException e) {
				LOG.debug(e.getMessage());
			} catch (InstantiationException e) {
				LOG.debug(e.getMessage());
			} catch (IllegalAccessException e) {
				LOG.debug(e.getMessage());
			}
		}
		return instance;
	}
	
	/**
	 * 
	 * @return An instance of a layout painter. Might return null.
	 */
	private static ILayoutPainter getLayoutPainterInstance() {
		ILayoutPainter instance = null;
		String layoutPainterName = XavaPreferences.getInstance().getLayoutPainter();
		if (!Is.emptyString(layoutPainterName)) {
			try {
				instance = (ILayoutPainter)Class.forName(layoutPainterName).newInstance();
			} catch (ClassNotFoundException e) {
				LOG.debug(e.getMessage());
			} catch (InstantiationException e) {
				LOG.debug(e.getMessage());
			} catch (IllegalAccessException e) {
				LOG.debug(e.getMessage());
			}
		}
		return instance;
	}
	
	/**
	 * 
	 * @return true if a layout parser / painter is defined.
	 */
	public static boolean rendererDefined() {
		if (getLayoutParserInstance() != null 
				&& getLayoutPainterInstance() != null) {
			return true;
		}
		return false;
	}
}
