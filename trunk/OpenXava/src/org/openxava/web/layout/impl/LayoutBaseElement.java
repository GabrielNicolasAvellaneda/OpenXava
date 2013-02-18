/**
 * 
 */
package org.openxava.web.layout.impl;

import org.openxava.view.View;
import org.openxava.web.layout.ILayoutElement;

/**
 * Default implementation for Layout element
 * @author Federico Alcantara
 *
 */
public abstract class LayoutBaseElement implements ILayoutElement {

	private View view;
	private int groupLevel;
	
	public LayoutBaseElement(View view, int groupLevel){
		this.view = view;
		this.groupLevel = groupLevel;
	}
	
	/**
	 * @see org.openxava.web.layout.ILayoutElement#setView(org.openxava.view.View)
	 */
	public void setView(View view) {
		this.view = view;
	}

	/**
	 * @see org.openxava.web.layout.ILayoutElement#getView()
	 */
	public View getView() {
		return view;
	}


	/**
	 * @see org.openxava.web.layout.ILayoutElement#getGroupLevel()
	 */
	public int getGroupLevel() {
		return groupLevel;
	}

}
