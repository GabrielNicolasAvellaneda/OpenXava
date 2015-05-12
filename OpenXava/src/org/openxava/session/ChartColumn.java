/**
 * 
 */
package org.openxava.session;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.actions.OnChangeChartColumnDisplayedAction;
import org.openxava.actions.OnChangeChartColumnNameAction;
import org.openxava.actions.OnChangeMyReportColumnComparatorAction;
import org.openxava.annotations.DefaultValueCalculator;
import org.openxava.annotations.DisplaySize;
import org.openxava.annotations.Editor;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.OnChange;
import org.openxava.annotations.Required;
import org.openxava.calculators.TrueCalculator;
import org.openxava.tab.Tab;
import org.openxava.util.Is;
import org.openxava.util.Labels;
import org.openxava.util.Locales;
import org.openxava.util.XavaResources;

/**
 * @author Federico Alcantara
 *
 */
@Embeddable
public class ChartColumn implements Serializable {
	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(MyReportColumn.class); 
	
	private final static String COLUMN = "column";
	private final static String NAME = "name";
	private final static String LABEL = "label"; 
	private final static String COMPARATOR = "comparator";
	private final static String VALUE ="value";
	private final static String DATE_VALUE = "dateValue";
	private final static String BOOLEAN_VALUE = "booleanValue";
	private final static String VALID_VALUES_VALUE = "validValuesValue";
	private final static String DESCRIPTIONS_LIST_VALUE = "descriptionsListValue"; 
	private final static String CALCULATED = "calculated";
	private final static String ORDER = "order";
	private final static String SUM = "sum"; 
	private final static String DISPLAYED = "displayed";
	private final static String NUMBER = "number";
	
	private static DateFormat dateFormat = null; 
	
	@Hidden
	private Chart chart;
		
	@OnChange(OnChangeChartColumnNameAction.class)
	@Required
	private String name;
		
	@Required @Column(length=60) 
	private String label; 

	@OnChange(OnChangeMyReportColumnComparatorAction.class) 
	private String comparator;
	
	@Column(length=80) @DisplaySize(20)
	private String value;
	
	private Date dateValue; 
	
	private Boolean booleanValue; 
		
	private int validValuesValue; 
	
	private String descriptionsListValue; 
	
	@Hidden
	private boolean calculated;
	
	public enum Order { ASCENDING, DESCENDING } 
	@Column(name="ORDERING")
	private Order order;
	
	@Hidden
	private boolean sum; 
	
	@OnChange(value = OnChangeChartColumnDisplayedAction.class)
	@DefaultValueCalculator(value = TrueCalculator.class)
	private boolean displayed; 
	
	@Hidden
	private boolean number;
	
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
		if (dateValue != null) {
			return getChart().getMetaModel().getMetaProperty(getName()).format(dateValue, Locales.getCurrent()); 
		}
		if (booleanValue != null) {
			return booleanValue?Labels.get("yes", Locales.getCurrent()):Labels.get("no", Locales.getCurrent()); 			
		}
		if (validValuesValue > 0) {
			return getChart().getMetaModel().getMetaProperty(getName()).getValidValueLabel(getValidValuesIndex()); 
		}
		if (!Is.emptyString(descriptionsListValue)) {
			return descriptionsListValue.split(Tab.DESCRIPTIONS_LIST_SEPARATOR)[1];
		}
		return value;
	}
	
	@Hidden
	public String getValueForCondition() {
		if (dateValue != null) {
			return getValue(); 
		}		
		if (booleanValue != null) {
			return booleanValue.toString();
		}		
		if (validValuesValue > 0) {			
			return Integer.toString(getValidValuesIndex()); 
		}
		if (!Is.emptyString(descriptionsListValue)) {
			return descriptionsListValue.split(Tab.DESCRIPTIONS_LIST_SEPARATOR)[0];
		}
		return value;
	}
	
	private int getValidValuesIndex() { 
		return getChart().getMetaModel().isAnnotatedEJB3()?validValuesValue - 1:validValuesValue;
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

	public Chart getChart() {
		return chart;
	}

	public void setChart(Chart report) {
		this.chart = report;
	}

	
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public void save(Preferences preferences, int index) { 		
		preferences.put(COLUMN + index + "." + NAME, name);
		preferences.put(COLUMN + index + "." + LABEL, label); 
		if (comparator != null) preferences.put(COLUMN + index + "." + COMPARATOR, comparator);
		else preferences.remove(COLUMN + index + "." + COMPARATOR);
		if (value != null) preferences.put(COLUMN + index + "." + VALUE, value);
		else preferences.remove(COLUMN + index + "." + VALUE);
		if (dateValue != null) preferences.put(COLUMN + index + "." + DATE_VALUE, getDateFormat().format(dateValue));
		else preferences.remove(COLUMN + index + "." + DATE_VALUE);		
		if (booleanValue != null) preferences.putBoolean(COLUMN + index + "." + BOOLEAN_VALUE, booleanValue);
		else preferences.remove(COLUMN + index + "." + BOOLEAN_VALUE);
		preferences.putInt(COLUMN + index + "." + VALID_VALUES_VALUE, validValuesValue);
		if (descriptionsListValue != null) preferences.put(COLUMN + index + "." + DESCRIPTIONS_LIST_VALUE, descriptionsListValue);
		else preferences.remove(COLUMN + index + "." + DESCRIPTIONS_LIST_VALUE);
		preferences.putBoolean(COLUMN + index + "." + CALCULATED, calculated);
		if (order != null) preferences.put(COLUMN + index + "." + ORDER, order.name());
		else preferences.remove(COLUMN + index + "." + ORDER);
		preferences.put(COLUMN + index + "." + SUM, Boolean.toString(sum));
		preferences.put(COLUMN + index + "." + DISPLAYED, Boolean.toString(displayed));
		preferences.put(COLUMN + index + "." + NUMBER, Boolean.toString(number));
	}

	public boolean load(Preferences preferences, int index) throws BackingStoreException {   
		String name = preferences.get(COLUMN + index + "." + NAME, null);
		if (name == null) return false;
		this.name = name;
		this.label = preferences.get(COLUMN + index + "." + LABEL, name); 
		comparator = preferences.get(COLUMN + index + "." + COMPARATOR, null);
		value = preferences.get(COLUMN + index + "." + VALUE, null);
		String dateValue = preferences.get(COLUMN + index + "." + DATE_VALUE, null);
		try {
			this.dateValue = dateValue == null?null:getDateFormat().parse(dateValue);
		}
		catch (ParseException ex) {
			String message = XavaResources.getString("myreport_date_column_not_loaded", this.name);
			log.warn(message, ex); 
			throw new BackingStoreException(message);
		}
		String booleanValue = preferences.get(COLUMN + index + "." + BOOLEAN_VALUE, null);
		this.booleanValue = booleanValue == null?null:new Boolean(booleanValue);
		validValuesValue = preferences.getInt(COLUMN + index + "." + VALID_VALUES_VALUE, 0);
		descriptionsListValue = preferences.get(COLUMN + index + "." + DESCRIPTIONS_LIST_VALUE, null);
		calculated = preferences.getBoolean(COLUMN + index + "." + CALCULATED, false);		
		String order = preferences.get(COLUMN + index + "." + ORDER, null); 
		this.order =  order == null?null:Order.valueOf(order);
		String sum = preferences.get(COLUMN + index + "." + SUM, null);
		this.sum = sum == null?false:Boolean.valueOf(sum);
		String hidden = preferences.get(COLUMN + index + "." + DISPLAYED, null);
		this.displayed = hidden == null?false:Boolean.valueOf(hidden);
		String number = preferences.get(COLUMN + index + "." + NUMBER, null);
		this.number = number == null?false:Boolean.valueOf(number);
		return true;
	}
	
	public static boolean remove(Preferences preferences, int index) { 
		String name = preferences.get(COLUMN + index + "." + NAME, null);
		if (name == null) return false;
		preferences.remove(COLUMN + index + "." + NAME);		
		preferences.remove(COLUMN + index + "." + LABEL); 
		preferences.remove(COLUMN + index + "." + COMPARATOR);
		preferences.remove(COLUMN + index + "." + VALUE);
		preferences.remove(COLUMN + index + "." + DATE_VALUE); 
		preferences.remove(COLUMN + index + "." + BOOLEAN_VALUE);
		preferences.remove(COLUMN + index + "." + VALID_VALUES_VALUE);
		preferences.remove(COLUMN + index + "." + DESCRIPTIONS_LIST_VALUE);
		preferences.remove(COLUMN + index + "." + CALCULATED);		
		preferences.remove(COLUMN + index + "." + ORDER); 
		preferences.remove(COLUMN + index + "." + SUM);
		preferences.remove(COLUMN + index + "." + DISPLAYED);
		preferences.remove(COLUMN + index + "." + NUMBER);
		return true;
	}
	
	private static DateFormat getDateFormat() { 
		if (dateFormat == null) {
			dateFormat = new SimpleDateFormat("yyyyMMdd");
		}
		return dateFormat;
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

	public boolean isDisplayed() {
		return displayed;
	}

	public void setDisplayed(boolean hidden) {
		this.displayed = hidden;
	}

	public Date getDateValue() {
		return dateValue;
	}

	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}

	public boolean isNumber() {
		return number;
	}

	public void setNumber(boolean number) {
		this.number = number;
	}
	
}
