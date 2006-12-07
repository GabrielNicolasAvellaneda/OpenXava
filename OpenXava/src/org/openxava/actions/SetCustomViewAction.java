package org.openxava.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Javier Paniza
 */

public class SetCustomViewAction extends BaseAction implements ICustomViewAction {
	
	private String customView;
	private static Log log = LogFactory.getLog(SetCustomViewAction.class);
	
	public void execute() throws Exception {
	}


	public String getCustomView() {
		return customView;
	}

	public void setCustomView(String customView) {
		this.customView = customView;
	}
	
}
