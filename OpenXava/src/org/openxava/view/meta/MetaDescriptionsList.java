package org.openxava.view.meta;

import java.util.*;

import org.openxava.model.meta.*;
import org.openxava.util.*;

/**
 * @author Javier Paniza
 */
public class MetaDescriptionsList implements java.io.Serializable {
	
	private String descriptionPropertyName;
	private String descriptionPropertiesNames;	
	private String depends;
	private String condition;
	private boolean orderByKey;
	private Collection dependsNames;
		
	public String getDescriptionPropertyName() {
		return descriptionPropertyName;
	}
	
	public void setDescriptionPropertyName(String string) {
		descriptionPropertyName = string;
	}

	public String getCondition() {
		return condition;
	}

	public String getDepends() {
		return depends;
	}

	public void setCondition(String string) {
		condition = string;
	}

	public void setDepends(String string) {
		depends = string;
	}

	public boolean isOrderByKey() {
		return orderByKey;
	}

	public void setOrderByKey(boolean b) {
		orderByKey = b;
	}

	public String getDescriptionPropertiesNames() {
		return descriptionPropertiesNames==null?"":descriptionPropertiesNames;
	}
	public void setDescriptionPropertiesNames(
			String nombresPropiedadesDescripcion) {		
		this.descriptionPropertiesNames = nombresPropiedadesDescripcion;
	}

	public boolean dependsOn(MetaProperty p) { 
		return getDependsNames().contains(p.getName());		
	}

	private Collection getDependsNames() {
		if (dependsNames == null) {
			if (Is.emptyString(getDepends())) return Collections.EMPTY_LIST;
			dependsNames = new ArrayList();
			StringTokenizer st = new StringTokenizer(getDepends(), ",");
			while (st.hasMoreTokens()) {
				String name = st.nextToken().trim();
				dependsNames.add(name);
			}
		}
		return dependsNames;
	}
	
	
}
