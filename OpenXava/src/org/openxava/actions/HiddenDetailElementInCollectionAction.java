package org.openxava.actions;

/**
 * @author Javier Paniza
 */

public class HiddenDetailElementInCollectionAction extends CollectionElementViewBaseAction {
	
	public void execute() throws Exception {							
		getCollectionElementView().setCollectionDetailVisible(false);
		getCollectionElementView().setCollectionEditingRow(-1);
		if (isEntityReferencesCollection()) { 
			getCollectionElementView().setEditable(false);
			getCollectionElementView().setKeyEditable(true);			
		}				
	}

}
