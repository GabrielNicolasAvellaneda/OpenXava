package org.openxava.actions;

import javax.servlet.http.*;



import org.openxava.controller.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.view.*;

/**
 * @author Javier Paniza
 */

abstract public class CollectionElementViewBaseAction extends ViewBaseAction implements IModuleContextAction, IRequestAction {
	
	private View collectionElementView;		
	private String viewObject;
	private ModuleContext context;	
	private HttpServletRequest request;
	

	abstract public void execute() throws Exception;
	
	protected View getCollectionElementView() throws XavaException {
		if (collectionElementView == null) {
			collectionElementView = (View) context.get(request, viewObject);
			collectionElementView.refreshCollection(); 
		}
		return collectionElementView;
	}
	
	protected boolean isEntityReferencesCollection() throws XavaException {
		return getCollectionElementView().getMetaModel() instanceof MetaEntity;		
	}
		
	public ModuleContext getContext() {
		return context;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setContext(ModuleContext moduleContext) {
		context = moduleContext;
	}

	public void setRequest(HttpServletRequest request) {
		super.setRequest(request);
		this.request = request;
	}

	public String getViewObject() {
		return viewObject;
	}

	public void setViewObject(String string) {
		viewObject = string;
	}
	
}
