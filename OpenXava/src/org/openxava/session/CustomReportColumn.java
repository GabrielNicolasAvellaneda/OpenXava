package org.openxava.session;

import javax.persistence.*;

import org.openxava.actions.*;
import org.openxava.annotations.*;
import org.openxava.util.*;


/**
 * 
 * @author Javier Paniza
 */

public class CustomReportColumn implements java.io.Serializable {
	
	@Hidden
	private CustomReport report;
		
	@OnChange(OnChangeCustomReportColumnNameAction.class)
	@Required
	private String name;
	
	private String comparator;
	
	@Column(length=80) @DisplaySize(20)
	private String value;
	
	private Boolean booleanValue; 
		
	private int validValuesValue; 
	
	@Hidden
	private boolean calculated;
	
	
	public enum Order { ASCENDING, DESCENDING } 
	@Column(name="ORDERING")
	private Order order;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getComparator() {
		if (booleanValue != null || validValuesValue > 0) return org.openxava.tab.Tab.EQ_COMPARATOR; 				
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
	
}
