package org.openxava.test.actions;

import org.openxava.actions.*;
import org.openxava.tab.*;

/**
 * @author Javier Paniza
 */

public class InitDefaultYearTo2002Action extends BaseAction {

	private int defaultYear;
	private Tab tab;

	public void execute() throws Exception {
		setDefaultYear(2002);
		tab.setTitleVisible(true);
		tab.setTitleArgument(new Integer(2002));
	}

	public int getDefaultYear() {
		return defaultYear;
	}

	public void setDefaultYear(int i) {
		defaultYear = i;		
	}

	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}

}
