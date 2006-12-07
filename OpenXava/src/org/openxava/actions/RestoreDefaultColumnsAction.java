package org.openxava.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.tab.*;

/**
 * @author Javier Paniza
 */

public class RestoreDefaultColumnsAction extends BaseAction implements INavigationAction, IChangeModeAction  {

	private Tab tab;
	private static Log log = LogFactory.getLog(RestoreDefaultColumnsAction.class);

	public void execute() throws Exception {		
		getTab().restoreDefaultProperties();		
	}
	
	public Tab getTab() {
		return tab;
	}
	public void setTab(Tab tab) {
		this.tab = tab;
	}

	
	public String[] getNextControllers() throws Exception {
		return PREVIOUS_CONTROLLERS; 
	}
	

	public String getCustomView() throws Exception {
		return PREVIOUS_VIEW; 
	}

	
	public String getNextMode() {
		return PREVIOUS_MODE; 
	}
	
}
