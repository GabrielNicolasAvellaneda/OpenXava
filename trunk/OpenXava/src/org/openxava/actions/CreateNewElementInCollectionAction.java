package org.openxava.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Javier Paniza
 */

public class CreateNewElementInCollectionAction extends CollectionElementViewBaseAction {
	
	private Log log = LogFactory.getLog(CreateNewElementInCollectionAction.class);
	
	public void execute() throws Exception {
		if (!isEntityReferencesCollection()) { 
			getCollectionElementView().reset();				
		}
		getCollectionElementView().setCollectionDetailVisible(true);
		getCollectionElementView().setCollectionEditingRow(-1);		
	}

}
