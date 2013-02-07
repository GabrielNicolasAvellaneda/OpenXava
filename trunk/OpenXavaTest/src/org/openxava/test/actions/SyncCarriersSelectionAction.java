package org.openxava.test.actions;

import org.apache.commons.lang.*;
import org.apache.commons.logging.*;
import org.openxava.actions.*;
import org.openxava.tab.*;

/**
 * 
 * @author Javier Paniza 
 */
public class SyncCarriersSelectionAction extends OnSelectElementBaseAction {
	private static Log log = LogFactory.getLog(SyncCarriersSelectionAction.class);
	
	public void execute() throws Exception {
		Tab targetTab = getView().getSubview("fellowCarriers").getCollectionTab();
		int [] selected = targetTab.getSelected();		
		if (isSelected()) {
			targetTab.setAllSelected(ArrayUtils.add(selected, getRow()));
		}
		else {
			targetTab.deselected(getRow());
		}
	}

}
