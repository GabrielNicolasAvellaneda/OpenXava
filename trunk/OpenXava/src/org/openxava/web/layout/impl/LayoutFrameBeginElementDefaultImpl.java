/**
 * 
 */
package org.openxava.web.layout.impl;

import java.util.ArrayList;
import java.util.List;

import org.openxava.util.meta.MetaElement;
import org.openxava.view.View;
import org.openxava.web.layout.ILayoutFrameBeginElement;
import org.openxava.web.layout.ILayoutPainter;
import org.openxava.web.layout.ILayoutRowBeginElement;
import org.openxava.web.layout.LayoutBaseElement;

/**
 * @author Federico Alcantara
 *
 */
public class LayoutFrameBeginElementDefaultImpl extends LayoutBaseElement
		implements ILayoutFrameBeginElement {
	
	private String propertyPrefix;
	private String label;
	private String name;
	private Integer maxFramesCount;
	private Integer maxContainerColumnsCount;
	private List<ILayoutRowBeginElement> rows;
	
	public LayoutFrameBeginElementDefaultImpl(View view, int groupLevel) {
		super(view, groupLevel);
	}

	public LayoutFrameBeginElementDefaultImpl(View view, int groupLevel, MetaElement metaElement) {
		super(view, groupLevel);
		setPropertyPrefix("");
		setLabel(metaElement.getLabel());
		setName(metaElement.getName());
		setMaxFramesCount(0);
		setMaxContainerColumnsCount(0);
	}
	
	/**
	 * @see org.openxava.web.layout.ILayoutElement#render(org.openxava.web.layout.ILayoutPainter)
	 */
	public void render(ILayoutPainter layoutPainter) {
		layoutPainter.beginFrame(this);
	}

	/**
	 * @return the propertyPrefix
	 */
	public String getPropertyPrefix() {
		return propertyPrefix;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param propertyPrefix the propertyPrefix to set
	 */
	public void setPropertyPrefix(String propertyPrefix) {
		this.propertyPrefix = propertyPrefix;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public Integer getMaxFramesCount() {
		return maxFramesCount;
	}

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
		return "FrameBegin [propertyPrefix="
				+ propertyPrefix + ", label=" + label + ", name=" + name
				+ ", maxFramesCount=" + maxFramesCount
				+ ", maxContainerColumnsCount=" + maxContainerColumnsCount
				+ ", groupLevel=" + getGroupLevel() + "]";
	}

}
