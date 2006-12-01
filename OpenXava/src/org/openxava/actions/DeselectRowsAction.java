package org.openxava.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.tab.*;

/**
 * @author Javier Paniza
 */

public class DeselectRowsAction extends BaseAction implements IAction {

	private Tab tab;
	private Log log = LogFactory.getLog(DeselectRowsAction.class);

	public void execute() throws Exception {
		tab.deselectAll();
	}

	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}

}
