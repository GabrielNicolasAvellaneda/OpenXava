package org.openxava.actions;

/**
 * @author Javier Paniza
 */

public class CreateNewElementInCollectionAction extends CollectionElementViewBaseAction {
	
	public void execute() throws Exception {									
		if (isEntityReferencesCollection()) { 
			getCollectionElementView().setEditable(false);
			getCollectionElementView().setKeyEditable(true);			
		}
		else {
			getCollectionElementView().reset();				
		}
		getCollectionElementView().setCollectionDetailVisible(true);
		getCollectionElementView().setCollectionEditingRow(-1);		
	}

}
