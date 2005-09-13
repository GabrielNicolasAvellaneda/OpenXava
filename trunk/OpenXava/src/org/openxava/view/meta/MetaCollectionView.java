package org.openxava.view.meta;

import java.io.*;
import java.util.*;

import org.openxava.util.*;


/**
 * @author Javier Paniza
 */
public class MetaCollectionView implements Serializable {
	
	private String editActionName;
	private Collection actionsDetailNames;
	private Collection actionsListNames;
	private List propertiesListNames;
	private String collectionName;
	private String mediatorClassName;
	private String viewName;
	private boolean readOnly;
	private boolean editOnly; 
	private boolean createReference;
	
	public void addActionDetailName(String nombreAccion) {
		if (actionsDetailNames == null) actionsDetailNames = new ArrayList();
		actionsDetailNames.add(nombreAccion);
	}
	
	public void addActionListName(String nombreAccion) {
		if (actionsListNames == null) actionsListNames = new ArrayList();
		actionsListNames.add(nombreAccion);
	}
	
	/**
	 * Returns the nombreClaseMediador.
	 * @return String
	 */
	public String getMediatorClassName() {
		return mediatorClassName;
	}

	/**
	 * Sets the nombreClaseMediador.
	 * @param nombreClaseMediador The nombreClaseMediador to set
	 */
	public void setMediatorClassName(String nombreClaseMediador) {
		this.mediatorClassName = nombreClaseMediador;
	}

	/**
	 * Returns the nombreColeccion.
	 * @return String
	 */
	public String getCollectionName() {
		return collectionName;
	}

	/**
	 * Sets the nombreColeccion.
	 * @param nombreColeccion The nombreColeccion to set
	 */
	public void setCollectionName(String nombreColeccion) {
		this.collectionName = nombreColeccion;
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

	public void _setPropertiesList(String propiedadesLista) {				 		
		if (!Is.emptyString(propiedadesLista)) {
			propertiesListNames = new ArrayList();
			StringTokenizer st = new StringTokenizer(propiedadesLista, ",");
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
}
