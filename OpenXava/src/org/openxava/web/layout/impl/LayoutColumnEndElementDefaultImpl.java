/**
 * 
 */
package org.openxava.web.layout.impl;

import org.openxava.view.View;
import org.openxava.web.layout.ILayoutColumnEndElement;
import org.openxava.web.layout.ILayoutPainter;

/**
 * @author Federico Alcantara
 *
 */
public class LayoutColumnEndElementDefaultImpl extends LayoutBaseElement
		implements ILayoutColumnEndElement {

	public LayoutColumnEndElementDefaultImpl(View view, int groupLevel) {
		super(view, groupLevel);
	}

	/**
	 * @see org.openxava.web.layout.ILayoutElement#render(org.openxava.web.layout.ILayoutPainter)
	 */
	public void render(ILayoutPainter layoutPainter) {
		layoutPainter.endColumn(this);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ColumnEnd [groupLevel="
				+ getGroupLevel() + "]";
	}
}
