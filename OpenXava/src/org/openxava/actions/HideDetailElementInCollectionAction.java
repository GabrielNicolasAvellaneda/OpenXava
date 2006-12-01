package org.openxava.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Javier Paniza
 */

public class HideDetailElementInCollectionAction extends CollectionElementViewBaseAction {
	
	private Log log = LogFactory.getLog(HideDetailElementInCollectionAction.class);
	
	public void execute() throws Exception {							
		getCollectionElementView().setCollectionDetailVisible(false);
		getCollectionElementView().setCollectionEditingRow(-1);
	}

}
