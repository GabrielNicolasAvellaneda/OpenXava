/**
 * 
 */
package org.openxava.render;

import java.util.Collection;

import org.openxava.controller.ModuleContext;
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
	 * @param context OpenXava context where views and elements are handled.
	 */
	public Collection<LayoutElement> parseView(View view, ModuleContext context);
}
