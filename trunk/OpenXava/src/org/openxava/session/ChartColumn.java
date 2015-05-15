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
	private final static String DISPLAYED = "displayed";	
	private final static String NUMBER = "number";
	
	@Hidden
	private Chart chart;
		
	@OnChange(OnChangeChartColumnNameAction.class)
	@Required
	private String name;
		
	@Required @Column(length=60) 
	private String label; 
	
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

	public Chart getChart() {
		return chart;
	}

	public void setChart(Chart report) {
		this.chart = report;
	}

	public void save(Preferences preferences, int index) { 		
		preferences.put(COLUMN + index + "." + NAME, name);		
		preferences.put(COLUMN + index + "." + LABEL, label);
		preferences.put(COLUMN + index + "." + DISPLAYED, Boolean.toString(displayed));
		preferences.put(COLUMN + index + "." + NUMBER, Boolean.toString(number));
	}

	public boolean load(Preferences preferences, int index) throws BackingStoreException {   
		String name = preferences.get(COLUMN + index + "." + NAME, null);
		if (name == null) return false;
		this.name = name;		
		this.label = preferences.get(COLUMN + index + "." + LABEL, name);
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
		preferences.remove(COLUMN + index + "." + DISPLAYED);
		preferences.remove(COLUMN + index + "." + NUMBER);
		return true;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isDisplayed() {
		return displayed;
	}

	public void setDisplayed(boolean hidden) {
		this.displayed = hidden;
	}

	public boolean isNumber() {
		return number;
	}

	public void setNumber(boolean number) {
		this.number = number;
	}
	
}
