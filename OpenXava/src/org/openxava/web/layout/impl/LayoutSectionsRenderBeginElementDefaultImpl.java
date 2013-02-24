/**
 * 
 */
package org.openxava.web.layout.impl;

import org.openxava.view.View;
import org.openxava.web.layout.ILayoutPainter;
import org.openxava.web.layout.ILayoutSectionsRenderBeginElement;
import org.openxava.web.layout.LayoutBaseContainerElement;

/**
 * @author Federico Alcantara
 *
 */
public class LayoutSectionsRenderBeginElementDefaultImpl extends
		LayoutBaseContainerElement implements ILayoutSectionsRenderBeginElement {

	public LayoutSectionsRenderBeginElementDefaultImpl(View view, int groupLevel) {
		super(view, groupLevel);
	}

	/**
	 * @see org.openxava.web.layout.ILayoutElement#render(org.openxava.web.layout.ILayoutPainter)
	 */
	public void render(ILayoutPainter layoutPainter) {
		layoutPainter.beginSectionsRender(this);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SectionsRenderBegin [maxFramesCount="
				+ getMaxFramesCount()
				+ ", maxContainerColumnsCount="
				+ getMaxContainerColumnsCount()
				+ ", groupLevel="
				+ getGroupLevel() + "]";
	}

}
