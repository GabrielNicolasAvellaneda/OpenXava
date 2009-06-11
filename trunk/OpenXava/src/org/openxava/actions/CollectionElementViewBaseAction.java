package org.openxava.actions;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.controller.ModuleContext;
import org.openxava.model.meta.MetaEntity;
import org.openxava.util.XavaException;
import org.openxava.view.View;

/**
 * @author Javier Paniza
 */

abstract public class CollectionElementViewBaseAction extends ViewBaseAction implements IModuleContextAction, IRequestAction {
	private static Log log = LogFactory.getLog(CollectionElementViewBaseAction.class);
	
	private View collectionElementView;		
	private String viewObject;
	private ModuleContext context;	
	private HttpServletRequest request;
	

	abstract public void execute() throws Exception;
	
	protected View getCollectionElementView() throws XavaException {
		if (collectionElementView == null) {
			collectionElementView = (View) context.get(request, viewObject);
			collectionElementView.refreshCollections(); 
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
