package org.openxava.test.actions;

import java.util.*;

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
		Map [] selected = targetTab.getSelectedKeys();		
		if (isSelected()) {
			Map newKey = (Map) targetTab.getTableModel().getObjectAt(getRow());
			targetTab.setAllSelectedKeys((Map[])ArrayUtils.add(selected, newKey));
		}
		else {
			targetTab.deselect(getRow());
		}
	}
	
}
