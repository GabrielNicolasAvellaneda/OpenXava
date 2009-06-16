package org.openxava.actions;

import org.openxava.tab.*;
import org.openxava.util.*;

/**
 * 
 * 
 * @author Javier Paniza
 */

public class GoAddElementsToCollectionAction extends CollectionElementViewBaseAction implements INavigationAction {
	
			
	private Tab tab;		
	private String currentCollectionLabel;  
	private String collectionViewObject;
	private String nextController = "AddToCollection"; // If you change the default value change setter and getter doc too
	
	
	public void execute() throws Exception {		
		Tab tab = new Tab();
		tab.setRequest(getTab().getRequest());
		tab.setModelName(getCollectionElementView().getModelName());	
		setTab(tab);				
		setCurrentCollectionLabel("'" + 
				Labels.get(getCollectionElementView().getMemberName(), getRequest().getLocale()) +
				" " + XavaResources.getString(getRequest(), "of") + " " +
				Labels.get(getCollectionElementView().getParent().getModelName(), getRequest().getLocale()) + "'");
		setCollectionViewObject(getViewObject());		
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
