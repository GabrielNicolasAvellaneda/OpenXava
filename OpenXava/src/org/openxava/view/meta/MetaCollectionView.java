package org.openxava.view.meta;

import java.io.*;
import java.util.*;

import org.openxava.util.*;


/**
 * @author Javier Paniza
 */
public class MetaCollectionView implements Serializable {
	
	private String editActionName;
	private String viewActionName; 
	private Collection actionsDetailNames;
	private Collection actionsListNames;
	private List propertiesListNames;
	private String collectionName;
	private String mediatorClassName;
	private String viewName;
	private boolean readOnly;
	private boolean editOnly; 
	private boolean createReference;
	
	public void addActionDetailName(String actionName) {
		if (actionsDetailNames == null) actionsDetailNames = new ArrayList();
		actionsDetailNames.add(actionName);
	}
	
	public void addActionListName(String actionName) {
		if (actionsListNames == null) actionsListNames = new ArrayList();
		actionsListNames.add(actionName);
	}
	
	public String getMediatorClassName() {
		return mediatorClassName;
	}

	public void setMediatorClassName(String mediatorClassName) {
		this.mediatorClassName = mediatorClassName;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
		
	public String getViewName() {		
		return viewName;
	}
		
	public void setViewName(String string) {
		viewName = string;
	}

	public boolean hasListProperties() {		
		return propertiesListNames != null && !propertiesListNames.isEmpty();
	}
	
	public List getPropertiesListNames() {
		return propertiesListNames == null?Collections.EMPTY_LIST:propertiesListNames;
	}

	public void _setPropertiesList(String listProperties) {				 		
		if (!Is.emptyString(listProperties)) {
			propertiesListNames = new ArrayList();
			StringTokenizer st = new StringTokenizer(listProperties, ",;");
			while (st.hasMoreTokens()) {
				String propiedad = st.nextToken().trim();
				propertiesListNames.add(propiedad); 
			}
		} 
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean b) {
		readOnly = b;
	}

	public boolean isEditOnly() {
		return editOnly;
	}

	public void setEditOnly(boolean b) {
		editOnly = b;
	}

	public Collection getActionsDetailNames() {		
		return actionsDetailNames==null?Collections.EMPTY_LIST:actionsDetailNames;
	}
	
	public Collection getActionsListNames() {		
		return actionsListNames==null?Collections.EMPTY_LIST:actionsListNames;
	}
	
	public String getEditActionName() {
		return editActionName;
	}
	public void setEditActionName(String editActionName) {
		this.editActionName = editActionName;
	}

	public boolean isCreateReference() {
		return createReference;
	}

	public void setCreateReference(boolean createReference) {
		this.createReference = createReference;
	}

	public String getViewActionName() {
		return viewActionName;
	}

	public void setViewActionName(String viewActionName) {
		this.viewActionName = viewActionName;
	}
}
