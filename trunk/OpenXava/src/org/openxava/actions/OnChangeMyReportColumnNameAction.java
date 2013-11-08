package org.openxava.actions;

import org.openxava.model.meta.*;
import org.openxava.session.*;
import org.openxava.util.*;
import org.openxava.web.*;

/**
 * 
 * @author Javier Paniza 
 */

public class OnChangeMyReportColumnNameAction extends TabBaseAction implements IOnChangePropertyAction {
	
	public final static String STRING_COMPARATOR = "__STRING__";
	public final static String DATE_COMPARATOR = "__DATE__";
	public final static String EMPTY_COMPARATOR = "__EMPTY__";
	public final static String OTHER_COMPARATOR = "__OTHER__";
	public final static String SHOW_MORE="__MORE__";
		
	private Object newValue;
	
	public void execute() throws Exception {		
		String propertyName = (String) newValue;
		if (SHOW_MORE.equals(propertyName)) {
			getView().putObject("xava.myReportColumnShowAllColumns", true);
			propertyName = null;
		}
		if (Is.emptyString(propertyName)) {
			getView().setValue("label", ""); 
			getView().setValue("comparator", EMPTY_COMPARATOR);
			getView().setHidden("sum", true); // We hide it by default, because there are more non-summable properties than summable ones
			showStandardMembers();
			return;
		}		
		MetaProperty property = getTab().getMetaTab().getMetaModel().getMetaProperty(propertyName);
		MyReportColumn column = (MyReportColumn) getView().getModel();
		if (column != null && propertyName.equals(column.getName())) {
			getView().setValue("label", column.getLabel());
		}
		else {
			property = property.cloneMetaProperty();
			property.setQualifiedName(propertyName);
			getView().setValue("label", property.getQualifiedLabel(Locales.getCurrent()));
		}
		getView().setHidden("sum", !getTab().isTotalCapable(property));
		if (property.isCalculated()) {
			hideMembers();
			return;
		}
		
		if ("boolean".equals(property.getType().getName()) || "java.lang.Boolean".equals(property.getType().getName())) {
			showBooleanValue();
			return;
		}
		
		if (property.hasValidValues()) {
			showValidValuesValue();
			return;
		}
		
		String descriptionsListEditorURL = WebEditors.getEditorURLDescriptionsList(
				getTab().getTabName(), getTab().getModelName(), "${propertyKey}", 
				-1, "", propertyName, property.getName());
		
		if (!Is.emptyString(descriptionsListEditorURL)) {
			getView().putObject("xava.myReportColumnDescriptionsListEditorURL", descriptionsListEditorURL); 
			showDescriptionsListValue();
			return;
		}
				
		String value = getView().getValueString("value"); 
		String comparatorValue = Is.emptyString(value)?"":getView().getValueString("comparator"); 
		if ("java.lang.String".equals(property.getType().getName())) {
			getView().setValue("comparator", STRING_COMPARATOR + ":" + comparatorValue); 			
		}
		else if (java.util.Date.class.isAssignableFrom(property.getType()) && 
			!property.getType().equals(java.sql.Time.class)) 
		{ 			
			getView().setValue("comparator", DATE_COMPARATOR + ":" + comparatorValue); 			
		}
		else {			
			getView().setValue("comparator", OTHER_COMPARATOR + ":" + comparatorValue); 			
		}
		showStandardMembers();
	}

	private void showBooleanValue() {
		getView().setHidden("comparator", true);
		getView().setHidden("value", true);
		getView().setHidden("descriptionsListValue", true); 
		getView().setHidden("booleanValue", false);
		getView().setHidden("validValuesValue", true);
		getView().setHidden("order", false);
	}
	
	private void showValidValuesValue() {
		getView().setHidden("comparator", true);
		getView().setHidden("value", true);
		getView().setHidden("descriptionsListValue", true); 
		getView().setHidden("booleanValue", true);
		getView().setHidden("validValuesValue", false);
		getView().setHidden("order", false);
	}
	
	private void showDescriptionsListValue() { 
		getView().setHidden("comparator", true);
		getView().setHidden("value", true);
		getView().setHidden("descriptionsListValue", false); 
		getView().setHidden("booleanValue", true);
		getView().setHidden("validValuesValue", true);
		getView().setHidden("order", false);
	}	

	private void hideMembers() {
		getView().setHidden("comparator", true);
		getView().setHidden("value", true);
		getView().setHidden("descriptionsListValue", true); 
		getView().setHidden("booleanValue", true);
		getView().setHidden("validValuesValue", true);
		getView().setHidden("order", true);
		getView().setHidden("sum", true); 
	}

	private void showStandardMembers() {
		getView().setHidden("comparator", false);
		getView().setHidden("value", false);			
		getView().setHidden("descriptionsListValue", true); 
		getView().setHidden("booleanValue", true);
		getView().setHidden("validValuesValue", true);
		getView().setHidden("order", false);
	}

	public void setChangedProperty(String propertyName) {		
	}

	public void setNewValue(Object value) {
		newValue = value;		
	}

}
