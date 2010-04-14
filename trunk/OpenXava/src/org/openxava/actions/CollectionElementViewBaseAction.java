package org.openxava.actions;

import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.view.*;

/**
 * @author Javier Paniza
 */

abstract public class CollectionElementViewBaseAction extends ViewBaseAction {
	
	private View collectionElementView;		
	private String viewObject;

	abstract public void execute() throws Exception;
	
	public View getView() { 
		if (viewObject != null) return super.getView();
		return getCollectionElementView().getRoot();		
	}
	
	protected View getCollectionElementView() throws XavaException {
		if (collectionElementView == null) {
			if (viewObject == null) collectionElementView = super.getView(); // In a dialog
			else collectionElementView = (View) getContext().get(getRequest(), viewObject);
			collectionElementView.refreshCollections(); 
		}
		return collectionElementView;
	}
		
	protected boolean isEntityReferencesCollection() throws XavaException {
		return getCollectionElementView().getMetaModel() instanceof MetaEntity;		
	}
	
	public String getViewObject() {
		return viewObject;
	}

	public void setViewObject(String viewObject) {
		this.viewObject = viewObject;
	}
		
}