package org.openxava.model.inner;

import javax.persistence.*;

import org.openxava.actions.*;
import org.openxava.annotations.*;

/**
 * 
 * @author Javier Paniza
 */

public class CustomReportColumn implements java.io.Serializable {
		
	@Column(length=40) // tmp Revisar tamaño
	@OnChange(OnChangeCustomReportColumnNameAction.class)
	@Required
	private String columnName;
	
	private String comparator;
	
	@Column(length=40)
	private String value;
	
	/*
	public enum Order { ASCENDING, DESCENDING }
	@Column(name="ORDERING")
	private Order order;
	
	public enum Total { SUM }
	private Total total;
	
	@Column(length=40)
	private String label;
	
	@Column(length=2)
	private int size;
	
	private boolean hidden;
	*/
	
	@Hidden
	public String getComparatorSign() {
		// Esta lógica está también en Tab.convertComparator
		if ("eq".equals(comparator)) return "=";
		if ("ne".equals(comparator)) return "<>";
		if ("ge".equals(comparator)) return ">=";
		if ("le".equals(comparator)) return "<=";
		if ("gt".equals(comparator)) return ">";
		if ("lt".equals(comparator)) return "<";		
		if ("starts_comparator".equals(comparator)) return "like";			
		if ("contains_comparator".equals(comparator)) return "like";
		if ("not_contains_comparator".equals(comparator)) return "not like";
		/* Estos faltan
		String year = "year_comparator".equals(comparator)?"selected='selected'":"";
		String month = "month_comparator".equals(comparator)?"selected='selected'":"";
		String yearMonth = "year_month_comparator".equals(comparator)?"selected='selected'":"";
		*/
		return comparator;	
	}
	
	@Hidden
	public String getDecoratedValue() {
		if ("starts_comparator".equals(comparator)) return "'" + value + "%'";			
		if ("contains_comparator".equals(comparator)) return "'%" + value + "%'";
		if ("not_contains_comparator".equals(comparator)) return "'%" + value + "%'";
		return value;
	}
	
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	
	public String getComparator() {
		return comparator;
	}

	public void setComparator(String comparator) {
		this.comparator = comparator;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


	/*
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public Total getTotal() {
		return total;
	}

	public void setTotal(Total total) {
		this.total = total;
	}
	*/

}
