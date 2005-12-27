package org.openxava.test.actions;

import org.openxava.actions.*;
import org.openxava.tab.*;

public class ChangePageRowCountAction extends BaseAction {
	
	private Tab tab;
	private int rowCount;

	public void execute() throws Exception {
		getTab().setPageRowCount(getRowCount());
	}

	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int size) {
		this.rowCount = size;
	}

}
