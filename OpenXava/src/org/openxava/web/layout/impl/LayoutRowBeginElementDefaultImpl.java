/**
 * 
 */
package org.openxava.web.layout.impl;

import org.openxava.view.View;
import org.openxava.web.layout.ILayoutPainter;
import org.openxava.web.layout.ILayoutRowBeginElement;
import org.openxava.web.layout.LayoutBaseElement;

/**
 * @author Federico Alcantara
 *
 */
public class LayoutRowBeginElementDefaultImpl extends LayoutBaseElement
		implements ILayoutRowBeginElement {

	private Integer maxFramesCount;
	private Integer rowIndex;
	private Integer maxRowColumnsCount;
	private Integer rowCurrentColumnsCount;
	private boolean freeForm;
	
	public LayoutRowBeginElementDefaultImpl(View view, int groupLevel) {
		super(view, groupLevel);
		maxFramesCount = 0;
		rowIndex = -1;
		maxRowColumnsCount = 0;
		rowCurrentColumnsCount = 0;
		freeForm = true; // it should change to false if more than one row is displayed.
	}

	/**
	 * @see org.openxava.web.layout.ILayoutElement#render(org.openxava.web.layout.ILayoutPainter)
	 */
	public void render(ILayoutPainter layoutPainter) {
		layoutPainter.beginRow(this);
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

	/**
	 * @see org.openxava.web.layout.ILayoutRowBeginElement#getRowIndex()
	 */
	public Integer getRowIndex() {
		return rowIndex;
	}

	/**
	 * @see org.openxava.web.layout.ILayoutRowBeginElement#setRowIndex(java.lang.Integer)
	 */
	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}

	public Integer getMaxRowColumnsCount() {
		return maxRowColumnsCount;
	}

	public void setMaxRowColumnsCount(Integer maxRowColumnsCount) {
		this.maxRowColumnsCount = maxRowColumnsCount;
	}

	public Integer getRowCurrentColumnsCount() {
		return rowCurrentColumnsCount;
	}

	public void setRowCurrentColumnsCount(Integer rowCurrentColumnsCount) {
		this.rowCurrentColumnsCount = rowCurrentColumnsCount;
	}

	public boolean isFreeForm() {
		return freeForm;
	}

	public void setFreeForm(boolean freeForm) {
		this.freeForm = freeForm;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RowBegin [maxFramesCount="
				+ maxFramesCount + ", rowIndex=" + rowIndex
				+ ", maxRowColumnsCount=" + maxRowColumnsCount
				+ ", rowCurrentColumnsCount=" + rowCurrentColumnsCount
				+ ", freeForm=" + freeForm + ", groupLevel="
				+ getGroupLevel() + "]";
	}

}
