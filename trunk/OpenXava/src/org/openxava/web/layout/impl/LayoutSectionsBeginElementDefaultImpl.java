/**
 * 
 */
package org.openxava.web.layout.impl;

import org.openxava.view.View;
import org.openxava.web.layout.ILayoutPainter;
import org.openxava.web.layout.ILayoutSectionsBeginElement;

/**
 * @author Federico Alcantara
 *
 */
public class LayoutSectionsBeginElementDefaultImpl extends
		LayoutViewBeginElementDefaultImpl implements
		ILayoutSectionsBeginElement {

	public LayoutSectionsBeginElementDefaultImpl(View view, int groupLevel) {
		super(view, groupLevel);
	}

	@Override
	public void render(ILayoutPainter layoutPainter) {
		layoutPainter.beginSections(this);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SectionsBegin [groupLevel="
				+ getGroupLevel() + "]";
	}
	
}
