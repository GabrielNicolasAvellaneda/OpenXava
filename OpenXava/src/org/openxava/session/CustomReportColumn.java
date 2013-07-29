package org.openxava.session;

import java.util.prefs.*;

import javax.persistence.*;

import org.openxava.actions.*;
import org.openxava.annotations.*;
import org.openxava.util.*;


/**
 * 
 * @author Javier Paniza
 */

public class CustomReportColumn implements java.io.Serializable {
	
	private final static String COLUMN = "column";
	private final static String NAME = "name";
	private final static String LABEL = "label"; 
	private final static String COMPARATOR = "comparator";
	private final static String VALUE ="value";
	private final static String BOOLEAN_VALUE = "booleanValue";
	private final static String VALID_VALUES_VALUE = "validValuesValue";
	private final static String DESCRIPTIONS_LIST_VALUE = "descriptionsListValue"; 
	private final static String CALCULATED = "calculated";
	private final static String ORDER = "order";
	private final static String SUM = "sum"; 
	private final static String HIDDEN = "hidden"; 
	
	@Hidden
	private CustomReport report;
		
	@OnChange(OnChangeCustomReportColumnNameAction.class)
	@Required
	private String name;
	
	@Required
	private String label; 
	
	private String comparator;
	
	@Column(length=80) @DisplaySize(20)
	private String value;
	
	private Boolean booleanValue; 
		
	private int validValuesValue; 
	
	private String descriptionsListValue; 
	
	@Hidden
	private boolean calculated;
	
	
	public enum Order { ASCENDING, DESCENDING } 
	@Column(name="ORDERING")
	private Order order;
	
	private boolean sum; 
	
	private boolean hidden; 
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getComparator() {
		if (booleanValue != null || validValuesValue > 0 || !Is.emptyString(descriptionsListValue)) return org.openxava.tab.Tab.EQ_COMPARATOR;
		return comparator;
	}

	public void setComparator(String comparator) {
		this.comparator = comparator;
	}

	public String getValue() {
		if (booleanValue != null) {
			return booleanValue?Labels.get("yes", Locales.getCurrent()):Labels.get("no", Locales.getCurrent()); 			
		}
		if (validValuesValue > 0) {
			return getReport().getMetaModel().getMetaProperty(getName()).getValidValueLabel(getValidValuesIndex()); 
		}
		if (!Is.emptyString(descriptionsListValue)) {
			return descriptionsListValue.split("::")[1];
		}
		return value;
	}
	
	@Hidden
	public String getValueForCondition() {
		if (booleanValue != null) {
			return booleanValue.toString();
		}		
		if (validValuesValue > 0) {			
			return Integer.toString(getValidValuesIndex()); 
		}
		if (!Is.emptyString(descriptionsListValue)) {
			return descriptionsListValue.split("::")[0];
		}
		return value;
	}
	
	private int getValidValuesIndex() { 
		return getReport().getMetaModel().isAnnotatedEJB3()?validValuesValue - 1:validValuesValue;
	}
	

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isCalculated() {
		return calculated;
	}

	public void setCalculated(boolean calculated) {
		this.calculated = calculated;
	}

	public Boolean getBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	public int getValidValuesValue() {
		return validValuesValue;
	}

	public void setValidValuesValue(int validValuesValue) {
		this.validValuesValue = validValuesValue;
	}

	public CustomReport getReport() {
		return report;
	}

	public void setReport(CustomReport report) {
		this.report = report;
	}

	
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	void save(Preferences preferences, int index) { 		
		preferences.put(COLUMN + index + "." + NAME, name);
		preferences.put(COLUMN + index + "." + LABEL, label); 
		if (comparator != null) preferences.put(COLUMN + index + "." + COMPARATOR, comparator);
		else preferences.remove(COLUMN + index + "." + COMPARATOR);
		if (value != null) preferences.put(COLUMN + index + "." + VALUE, value);
		else preferences.remove(COLUMN + index + "." + VALUE);
		if (booleanValue != null) preferences.putBoolean(COLUMN + index + "." + BOOLEAN_VALUE, booleanValue);
		else preferences.remove(COLUMN + index + "." + BOOLEAN_VALUE);
		preferences.putInt(COLUMN + index + "." + VALID_VALUES_VALUE, validValuesValue);
		if (descriptionsListValue != null) preferences.put(COLUMN + index + "." + DESCRIPTIONS_LIST_VALUE, descriptionsListValue);
		else preferences.remove(COLUMN + index + "." + DESCRIPTIONS_LIST_VALUE);
		preferences.putBoolean(COLUMN + index + "." + CALCULATED, calculated);
		if (order != null) preferences.put(COLUMN + index + "." + ORDER, order.name());
		else preferences.remove(COLUMN + index + "." + ORDER);
		preferences.put(COLUMN + index + "." + SUM, Boolean.toString(sum));
		preferences.put(COLUMN + index + "." + HIDDEN, Boolean.toString(hidden)); 
	}

	boolean load(Preferences preferences, int index) { 
		String name = preferences.get(COLUMN + index + "." + NAME, null);
		if (name == null) return false;
		this.name = name;
		this.label = preferences.get(COLUMN + index + "." + LABEL, name); 
		comparator = preferences.get(COLUMN + index + "." + COMPARATOR, null);
		value = preferences.get(COLUMN + index + "." + VALUE, null);
		String booleanValue = preferences.get(COLUMN + index + "." + BOOLEAN_VALUE, null);
		this.booleanValue = booleanValue == null?null:new Boolean(booleanValue);
		validValuesValue = preferences.getInt(COLUMN + index + "." + VALID_VALUES_VALUE, 0);
		descriptionsListValue = preferences.get(COLUMN + index + "." + DESCRIPTIONS_LIST_VALUE, null);
		calculated = preferences.getBoolean(COLUMN + index + "." + CALCULATED, false);		
		String order = preferences.get(COLUMN + index + "." + ORDER, null); 
		this.order =  order == null?null:Order.valueOf(order);
		String sum = preferences.get(COLUMN + index + "." + SUM, null);
		this.sum = sum == null?false:Boolean.valueOf(sum);
		String hidden = preferences.get(COLUMN + index + "." + HIDDEN, null);
		this.hidden = hidden == null?false:Boolean.valueOf(hidden);
		return true;
	}
	
	static boolean remove(Preferences preferences, int index) { 
		String name = preferences.get(COLUMN + index + "." + NAME, null);
		if (name == null) return false;
		preferences.remove(COLUMN + index + "." + NAME);		
		preferences.remove(COLUMN + index + "." + LABEL); 
		preferences.remove(COLUMN + index + "." + COMPARATOR);
		preferences.remove(COLUMN + index + "." + VALUE);
		preferences.remove(COLUMN + index + "." + BOOLEAN_VALUE);
		preferences.remove(COLUMN + index + "." + VALID_VALUES_VALUE);
		preferences.remove(COLUMN + index + "." + DESCRIPTIONS_LIST_VALUE);
		preferences.remove(COLUMN + index + "." + CALCULATED);		
		preferences.remove(COLUMN + index + "." + ORDER); 
		preferences.remove(COLUMN + index + "." + SUM);
		preferences.remove(COLUMN + index + "." + HIDDEN);
		return true;
	}


	public boolean isSum() {
		return sum;
	}

	public void setSum(boolean sum) {
		this.sum = sum;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescriptionsListValue() {
		return descriptionsListValue;
	}

	public void setDescriptionsListValue(String descriptionsListValue) {
		this.descriptionsListValue = descriptionsListValue;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	
	
	
}
