package org.openxava.actions;

/**
 * @author Javier Paniza
 */

public class MoveColumnToLeftAction extends TabBaseAction {
	
	private int columnIndex;

	public void execute() throws Exception {
		getTab().movePropertyToLeft(columnIndex);
	}

	public int getColumnIndex() {
		return columnIndex;
	}
	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}
	
}
