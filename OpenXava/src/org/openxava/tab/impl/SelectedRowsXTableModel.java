package org.openxava.tab.impl;

import java.util.*;

/**
 * 
 * @author Javier Paniza
 */

public class SelectedRowsXTableModel extends XTableModelDecoratorBase {
	
	private Map [] selectedRows;

	public SelectedRowsXTableModel(IXTableModel toDecorate, Map [] selectedRows) {
		super(toDecorate);
		this.selectedRows = selectedRows == null?new Map[0]:selectedRows; 
	}
		
	public int getRowCount() {
		return selectedRows.length;
	}
	
}
