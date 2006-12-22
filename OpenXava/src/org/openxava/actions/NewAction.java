package org.openxava.actions;

import org.openxava.util.*;




/**
 * @author Javier Paniza
 */

public class NewAction extends ViewBaseAction implements IChangeModeAction {
	
	public void execute() throws Exception {
		getView().setKeyEditable(true);
		getView().setEditable(true);
		getView().reset();
	}
	
	public String getNextMode() {
		return IChangeModeAction.DETAIL;
	}
	
}
