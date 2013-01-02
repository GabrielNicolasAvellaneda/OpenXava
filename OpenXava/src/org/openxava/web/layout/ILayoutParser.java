/**
 * 
 */
package org.openxava.web.layout;

import java.util.List;

import javax.servlet.jsp.PageContext;

import org.openxava.view.View;

/**
 * Layout manager interface.
 * @author Federico Alcantara
 *
 */
public interface ILayoutParser {
	/**
	 * Prepares the layout, so that future requests aren't processed.
	 * @param view Originating view.
	 * @param pageContext Where page are rendered.
	 */
	public List<LayoutElement> parseView(View view, PageContext pageContext);
}
