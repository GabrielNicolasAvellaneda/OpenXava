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

	public void setView(View view) {
		this.view = view;
	}
			
	protected String getModelName() {
		return view.getModelName();
	}
	

}
