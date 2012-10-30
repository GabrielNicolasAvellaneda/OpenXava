package org.openxava.actions;

import org.openxava.model.meta.*;
import org.openxava.util.*;

/**
 * tmp
 * 
 * @author Javier Paniza 
 */

public class OnChangeCustomReportColumnNameAction extends TabBaseAction implements IOnChangePropertyAction {
		
	private Object newValue;
	
	public void execute() throws Exception {
		String propertyName = (String) newValue;
		if (Is.emptyString(propertyName)) {
			System.out.println("[OnChangeCustomReportColumnNameAction.execute] propertyName=" + propertyName); // tmp
			getView().setValue("comparator", "Empty" + System.currentTimeMillis());
			return;
		}
		System.out.println("[OnChangeCustomReportColumnNameAction.execute] propertyName=" + propertyName); // tmp
		MetaProperty property = getTab().getMetaTab().getMetaModel().getMetaProperty(propertyName);
		System.out.println("[OnChangeCustomReportColumnNameAction.execute] type=" + property.getTypeName()); // tmp
		if ("java.lang.String".equals(property.getType().getName())) {
			getView().setValue("comparator", "String" + System.currentTimeMillis()); // tmp String como una variable FINAL
		}
		else if (java.util.Date.class.isAssignableFrom(property.getType()) && 
			!property.getType().equals(java.sql.Time.class)) 
		{
			getView().setValue("comparator", "Date" + System.currentTimeMillis()); // tmp Date como una variable FINAL
		}
		else {
			getView().setValue("comparator", "Other" + System.currentTimeMillis()); 
		}
	}

	public void setChangedProperty(String propertyName) {		
	}

	public void setNewValue(Object value) {
		newValue = value;		
	}

}
