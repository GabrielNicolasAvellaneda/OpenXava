package org.openxava.actions;

import org.openxava.view.*;

/**
 * @author Javier Paniza
 */

abstract public class ViewBaseAction extends BaseAction  {
	
	private View view;
		
	public View getView() {
		return view;
	}

	public void setView(View web) {
		view = web;
	}
			
	protected String getModelName() {
		return view.getModelName();
	}
	

}
