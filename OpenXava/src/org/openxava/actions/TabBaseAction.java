package org.openxava.actions;

import javax.inject.*;
import javax.servlet.http.*;

import org.openxava.controller.*;
import org.openxava.tab.*;
import org.openxava.util.*;

/**
 * Base class for actions that works with Tabs. <p>
 * 
 * It's not needed to inject the <code>xava_tab</code> objects to these actions.
 * These actions obtain the appropriate tab object depend on the current collection
 * or if not a collection from main list mode.<br>
 * 
 * @author Javier Paniza
 */
abstract public class TabBaseAction extends ViewBaseAction implements IModuleContextAction {
		
	private Tab tab;	
	
	private ModuleContext context; 
	private HttpServletRequest request;
	private String collection;

	protected Tab getTab() throws XavaException {
		if (tab == null ) {			
			String tabObject = Is.emptyString(collection)?"xava_tab":Tab.COLLECTION_PREFIX + Strings.change(collection, ".", "_");
			tab = (Tab) context.get(request, tabObject);
			if (tab.getCollectionView() != null) {				
				tab.getCollectionView().refreshCollections(); 				
			}
		}
		return tab;
	}

	public void setContext(ModuleContext context) {
		this.context = context;
		
	}
	
	public void setRequest(HttpServletRequest request) { 
		super.setRequest(request);
		this.request = request; 
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;		
	}

	/**
	 * Extract from the viewObject the name of the collection. <p>
	 * 
	 * Useful for using Tab actions for collections. <br> 
	 */
	public void setViewObject(String viewObject) { 
		if (Is.emptyString(this.collection)) {					
			this.collection = viewObject.substring("xava_view_".length());									
			while (this.collection.startsWith("section")) {
				this.collection = this.collection.substring(this.collection.indexOf('_') + 1);				
			}
		}
	}

}
