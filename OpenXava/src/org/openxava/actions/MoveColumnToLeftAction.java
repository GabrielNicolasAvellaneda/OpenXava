package org.openxava.actions;

import org.openxava.tab.*;

/**
 * @author Javier Paniza
 */

public class MoveColumnToLeftAction extends BaseAction {
	
	private Tab tab;
	private int columnIndex;

	public void execute() throws Exception {
		getTab().movePropertyToLeft(columnIndex);
	}

	public Tab getTab() {
		return tab;
	}
	public void setTab(Tab tab) {
		this.tab = tab;
	}
	public int getColumnIndex() {
		return columnIndex;
	}
	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}
}
