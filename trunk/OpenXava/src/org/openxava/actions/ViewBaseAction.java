package org.openxava.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.view.*;

/**
 * @author Javier Paniza
 */

abstract public class ViewBaseAction extends BaseAction  {
	
	private View view;
	private static Log log = LogFactory.getLog(ViewBaseAction.class);
	
		
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
