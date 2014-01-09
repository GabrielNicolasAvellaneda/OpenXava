package org.openxava.session;

import java.util.*;
import java.util.prefs.*;

import javax.persistence.*;

import org.apache.commons.logging.*;
import org.openxava.annotations.*;
import org.openxava.model.meta.*;
import org.openxava.session.MyReportColumn.*;
import org.openxava.tab.Tab;
import org.openxava.util.*;
import org.openxava.web.*;

/**
 * 
 * @author Javier Paniza 
 */

public class MyReport implements java.io.Serializable {
	
	private static Log log = LogFactory.getLog(MyReport.class); 
	
	private static final String NAME = "name";
	private static final String LAST_NAME = "lastName"; 
	private static final String MODEL_NAME = "modelName";
			
	@Required @Column(length=80) 
	@OnChange(org.openxava.actions.OnChangeMyReportNameAction.class) // It's only thrown in combo format, this is controlled from the editor 
	private String name;
	
	@Hidden
	private MetaModel metaModel;
	
	@RowActions({
		@RowAction("MyReport.columnUp"),
		@RowAction("MyReport.columnDown")
	})
	@RemoveSelectedAction("MyReport.removeColumn")
	@AsEmbedded 
	@SaveAction("MyReport.saveColumn")
	@NewAction("MyReport.newColumn") 
	@EditAction("MyReport.editColumn")
	@ListProperties("label, comparator, value, order, sum, hidden") 
	private List<MyReportColumn> columns;
	
	private String rootNodeName;
	
	public static MyReport create(org.openxava.tab.Tab tab) {  
		MyReport report = createEmpty(tab);
		report.setColumns(createColumns(report, tab));
		return report;
	}
	
	public static MyReport createEmpty(Tab tab) {
		MyReport report = new MyReport();
		report.setName(tab.getTitle()); 	
		report.setMetaModel(tab.getMetaTab().getMetaModel());
		report.setNodeName(tab);
		return report;
	}
	
	public static MyReport find(org.openxava.tab.Tab tab, String name) throws BackingStoreException {   
		MyReport report = new MyReport();	
		report.setName(name);
		report.setNodeName(tab);
		report.load();
		return report;
	}
		
	/**
	 * The names of all the reports of the same Tab of the current one. 
	 */
	@Hidden
	public String [] getAllNames() throws BackingStoreException { 
		return Users.getCurrentPreferences().node(rootNodeName).childrenNames();
	}
	
	/**
	 * The name of the last generated report of the same Tab of the current one. 
	 */	
	@Hidden
	public String getLastName() throws BackingStoreException { 
		String lastName = getRootPreferences().get(LAST_NAME, ""); 
		String [] allNames = getAllNames();
		if (Arrays.binarySearch(allNames, lastName) >= 0) return lastName; 
		return allNames.length > 0?allNames[0]:""; 
	}
	
	private static List<MyReportColumn> createColumns(MyReport report, org.openxava.tab.Tab tab) {
		List<MyReportColumn> columns = new ArrayList<MyReportColumn>();
		String [] comparators = tab.getConditionComparators();
		String [] values = tab.getConditionValues();
		int i = 0;
		for (MetaProperty property: tab.getMetaProperties()) {		
			MyReportColumn column = new MyReportColumn();
			column.setReport(report);
			column.setName(property.getQualifiedName());
			column.setLabel(property.getQualifiedLabel(Locales.getCurrent()));
			column.setCalculated(property.isCalculated());
			columns.add(column);
			if (!column.isCalculated()) {
				try {
					if (!Is.emptyString(values[i]) && !Is.emptyString(comparators[i])) { 
						column.setComparator(comparators[i]);
						if ("boolean".equals(property.getType().getName()) || "java.lang.Boolean".equals(property.getType().getName())) {
							column.setBooleanValue(Tab.EQ_COMPARATOR.equals(comparators[i]));							
						}
						else if (property.hasValidValues()) {							
							int validValue = Integer.parseInt(values[i]);
							if (property.getMetaModel().isAnnotatedEJB3()) validValue++;		
							column.setValidValuesValue(validValue); 
						}
						else if (values[i].contains(Tab.DESCRIPTIONS_LIST_SEPARATOR)) {
							column.setDescriptionsListValue(values[i]);
						}
						else {
							column.setValue(values[i]);
						} 
					}
				}
				catch (Exception ex) {
					log.warn(XavaResources.getString("initial_value_for_my_report_column_not_set", column.getName()), ex);					
				}				
				i++;				
			}
			if (tab.isOrderAscending(column.getName())) column.setOrder(Order.ASCENDING);
			else if (tab.isOrderDescending(column.getName())) column.setOrder(Order.DESCENDING);
		}		
		return columns;		
	}
		
	public void load() throws BackingStoreException { 
		Preferences preferences = getPreferences();
		name = preferences.get(NAME, name);
		String modelName = preferences.get(MODEL_NAME, "Unknown MetaModel");
		setMetaModel(MetaModel.get(modelName));
		int i = 0;
		MyReportColumn column = new MyReportColumn();
		columns = new ArrayList();
		while (column.load(preferences, i++)) {
			columns.add(column);
			column.setReport(this);
			column = new MyReportColumn();
		}
		preferences.flush();
	}	
	
	public void save() throws BackingStoreException { 
		Preferences preferences = getPreferences();
		preferences.put(NAME, name);		
		preferences.put(MODEL_NAME, getMetaModel().getName());
		int i = 0;
		for (MyReportColumn column: columns) {
			column.save(preferences, i++);
		}
		while (MyReportColumn.remove(preferences, i)) i++; 		
		preferences.flush();
		Preferences rootPreferences = getRootPreferences();
		rootPreferences.put(LAST_NAME, name);
		rootPreferences.flush();
	}
	
	public void remove() throws BackingStoreException { 
		getPreferences().removeNode();		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MyReportColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<MyReportColumn> columns) {
		this.columns = columns;
	}

	public MetaModel getMetaModel() {
		return metaModel;
	}

	public void setMetaModel(MetaModel metaModel) {
		this.metaModel = metaModel;
	}
	
	private void setNodeName(org.openxava.tab.Tab tab) { 
		rootNodeName = tab.friendMyReportGetPreferencesNodeName("myReport."); 
	}
	
	private Preferences getPreferences() throws BackingStoreException { 
		return Users.getCurrentPreferences().node(rootNodeName).node(name);
	}
	
	private Preferences getRootPreferences() throws BackingStoreException {		
		return Users.getCurrentPreferences().node(rootNodeName);
	}

	@Override
	public String toString() {
		return "CustomReport: " + name;
	}
		
}
