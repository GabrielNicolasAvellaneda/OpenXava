/**
 * 
 */
package org.openxava.web.layout.impl;

import java.util.ArrayList;
import java.util.List;

import org.openxava.view.View;
import org.openxava.web.layout.ILayoutPainter;
import org.openxava.web.layout.ILayoutRowBeginElement;
import org.openxava.web.layout.ILayoutViewBeginElement;

/**
 * @author Federico Alcantara
 *
 */
public class LayoutViewBeginElementDefaultImpl extends LayoutBaseElement implements
		ILayoutViewBeginElement {
	
	private boolean representsSection;
	private Integer maxFramesCount;
	private Integer maxContainerColumnsCount;
	private List<ILayoutRowBeginElement> rows;
	
	public LayoutViewBeginElementDefaultImpl(View view, int groupLevel) {
		super(view, groupLevel);
		representsSection = false;
		maxFramesCount = 0;
		maxContainerColumnsCount = 0;
	}

	public void render(ILayoutPainter layoutPainter) {
		layoutPainter.beginView(this);
	}

	public boolean isRepresentsSection() {
		return representsSection;
	}

	public void setRepresentsSection(boolean representsSection) {
		this.representsSection = representsSection;
	}
	
	/**
	 * @see org.openxava.web.layout.ILayoutRowBeginElement#getMaxFramesCount()
	 */
	public Integer getMaxFramesCount() {
		return maxFramesCount;
	}

	/**
	 * @see org.openxava.web.layout.ILayoutRowBeginElement#setMaxFramesCount(java.lang.Integer)
	 */
	public void setMaxFramesCount(Integer maxFramesCount) {
		this.maxFramesCount = maxFramesCount;
	}

	public Integer getMaxContainerColumnsCount() {
		return maxContainerColumnsCount;
	}

	public void setMaxContainerColumnsCount(Integer maxContainerColumnsCount) {
		this.maxContainerColumnsCount = maxContainerColumnsCount;
	}

	public List<ILayoutRowBeginElement> getRows() {
		if (rows == null) {
			rows = new ArrayList<ILayoutRowBeginElement>();
		}
		return rows;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ViewBegin [representsSection="
				+ representsSection + ", maxFramesCount=" + maxFramesCount
				+ ", maxContainerColumnsCount=" + maxContainerColumnsCount
				+ ", groupLevel=" + getGroupLevel() + "]";
	}

}
