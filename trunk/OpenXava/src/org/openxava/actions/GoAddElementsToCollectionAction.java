package org.openxava.actions;

import javax.inject.*;

import org.openxava.tab.*;
import org.openxava.util.*;

/**
 * 
 * 
 * @author Javier Paniza
 */

public class GoAddElementsToCollectionAction extends CollectionElementViewBaseAction implements INavigationAction {
	
	@Inject 		
	private Tab tab;
	@Inject
	private String currentCollectionLabel;
	@Inject 
	private String collectionViewObject;
	private String nextController = "AddToCollection"; // If you change the default value change setter and getter doc too
	
	
	public void execute() throws Exception {		
		Tab tab = new Tab();
		tab.setRequest(getTab().getRequest());
		tab.setModelName(getCollectionElementView().getModelName());	
		setTab(tab);
		currentCollectionLabel = "'" + 
			Labels.get(getCollectionElementView().getMemberName(), getRequest().getLocale()) +
			" " + XavaResources.getString(getRequest(), "of") + " " +
			Labels.get(getCollectionElementView().getParent().getModelName(), getRequest().getLocale()) + "'";
		getCollectionElementView().setTitleId("add_to_collection_prompt", currentCollectionLabel);
		setCollectionViewObject(getViewObject());
		showDialog(getCollectionElementView()); 		
	}
	
	public String[] getNextControllers() {		
		return new String[]{ getNextController() }; 
	}

	public String getCustomView() {		
		return "xava/addToCollection.jsp?rowAction=" + getNextController() + ".add";  
	}

	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}

	public String getCurrentCollectionLabel() {
		return currentCollectionLabel;
	}

	public void setCurrentCollectionLabel(String string) {
		currentCollectionLabel = string;
	}

	/**
	 * By default "AddToCollection".
	 */
	public String getNextController() {
		return nextController;
	}
	
	/**
	 * By default "AddToCollection".
	 */
	public void setNextController(String nextController) {
		this.nextController = nextController;
	}

	public String getCollectionViewObject() {
		return collectionViewObject;
	}

	public void setCollectionViewObject(String collectionViewObject) {
		this.collectionViewObject = collectionViewObject;
	}

}
