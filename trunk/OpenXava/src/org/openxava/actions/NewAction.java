package org.openxava.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Javier Paniza
 */

public class NewAction extends ViewBaseAction implements IChangeModeAction {
	
	private static Log log = LogFactory.getLog(NewAction.class);
	
	public void execute() throws Exception {
		getView().setKeyEditable(true);
		getView().setEditable(true);
		getView().reset();
	}
	
	public String getNextMode() {
		return IChangeModeAction.DETAIL;
	}
	
}
