package org.openxava.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.tab.*;

/**
 * @author Javier Paniza
 */
public class SortColumnsAction extends BaseAction {
	
	private Tab tab;
	private static Log log = LogFactory.getLog(SortColumnsAction.class);

	public void execute() throws Exception {
		// In reality, sort and unsort
		getTab().setSortRemainingProperties(!getTab().isSortRemainingProperties());
	}

	public Tab getTab() {
		return tab;
	}
	public void setTab(Tab tab) {
		this.tab = tab;
	}
}
