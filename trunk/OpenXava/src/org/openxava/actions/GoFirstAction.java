package org.openxava.actions;

import java.util.*;

import org.openxava.tab.*;


/**
 * @author Javier Paniza
 */

public class GoFirstAction extends BaseAction {
		
	
	private Map key;
	private transient Tab tab;
	
	public void executeBefore() throws Exception {
		key = (Map) tab.getTableModel().getObjectAt(0);
	}	

	public void execute() throws Exception {
		//buscarYEstablecer(clave);
	}

	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab web) {
		tab = web;
	}

}
