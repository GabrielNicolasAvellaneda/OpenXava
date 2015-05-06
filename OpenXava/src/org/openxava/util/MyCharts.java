/**
 * 
 */
package org.openxava.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.model.meta.MetaProperty;
import org.openxava.session.MyChart;
import org.openxava.session.MyChart.MyChartType;
import org.openxava.session.MyChartColumn;
import org.openxava.session.MyChartColumn.Order;
import org.openxava.tab.NoPreferencesTab;
import org.openxava.tab.Tab;
import org.openxava.view.View;

/**
 * MyCharts helper.
 * @author Federico Alcantara
 *
 */
public enum MyCharts {
	INSTANCE;
	private final Log log = LogFactory.getLog(MyCharts.class);
	public static final String CHART_OBJECT_SEPARATOR = ";";
	public static final String CHART_DATA_SEPARATOR = ":";
	public static final String CHART_DATASET_SEPARATOR = "/";
	public static final String SHARED_SUFFIX = "_SHARED_CHART";
	public static final String SHARED_NAME_PREFIX = "(*) ";
	
	private final String KEY_LAST_NAME = "chart_used_last_name";
	private final String KEY_NAME = "name";
	private final String KEY_CHART_TYPE = "chart_type";
	private final String KEY_X_AXES = "x_axes";
	private final String KEY_Y_AXES = "y_axes";
	private final String KEY_SHARED = "shared";
	
	/**
	 * Creates an empty MyChart.
	 * @param tab Tab to read the data from.
	 * @param myChart Current chart instance.
	 */
	public void fillEmpty(Tab tab, MyChart myChart, String defaultName) {
		String name  = defaultName != null ? defaultName : tab.getTitle();
		name = nodeName(name, false);
		myChart.setName(name);
		myChart.setMyChartLabelColumn(getAxisColumns(tab));
		// Select the first column that is not the same as the label column
		List<MyChartColumn> columns = createColumns(tab, myChart, true, myChart.getMyChartLabelColumn());
		myChart.setMetaModel(tab.getMetaTab().getMetaModel());
		myChart.setMyChartType(MyChartType.BAR);
		myChart.setChanged(true);
		myChart.setNameEditable(true);
		myChart.setShared(false);
		myChart.setColumns(columns);
	}
	
	/**
	 * Fills a chart object with the data collected from the view.
	 * @param myChart Current chart object.
	 * @throws BackingStoreException 
	 * @throws XavaException 
	 */
	private void fillFromView(View view, Tab tab, MyChart myChart) throws XavaException, BackingStoreException {
		if (view.isEditable("name")) {
			myChart.setName(view.getValueString("name"));
		} else {
			String name = MyCharts.INSTANCE.getChartPreferenceName(tab, view.getValueString("name"));
			myChart.setName(name);
		}
		myChart.setMyChartType((MyChart.MyChartType)view.getValue("myChartType"));
		myChart.setMyChartLabelColumn(view.getValueString("myChartLabelColumn"));
	}
	
	/**
	 * Saves current chart value.
	 * @param myChart Current chart object.
	 * @throws XavaException 
	 * @throws BackingStoreException
	 */
	public void saveChart(View view, Tab tab, MyChart myChart) throws XavaException, BackingStoreException {
		if (!Is.emptyString(myChart.getName())) {
			fillFromView(view, tab, myChart);
			// Makes sure to delete the correct one when changing sharing state
			String nodeName = nodeName(myChart.getName(), false); 
			removeChartPreferences(tab, nodeName); 
			String name = myChart.getName().startsWith(SHARED_NAME_PREFIX)
					? myChart.getName().substring(SHARED_NAME_PREFIX.length())
					: myChart.getName();
			myChart.setName(name);
			nodeName = nodeName(myChart.getName(), myChart.getShared());		
			saveChart(myChart, getChartPreferenceNode(tab, nodeName, true));
			myChart.setChanged(false);
			myChart.setNameEditable(false);
		}
	}
	
	/**
	 * Saves the chart properties in the appropriate preferences.
	 * @param myChart Current chart object.
	 * @param chartPreferences Preferences for the chart.
	 * @param suffix string to add at the end of the name.
	 * @throws BackingStoreException
	 */
	private void saveChart(MyChart myChart, Preferences chartPreferences) throws BackingStoreException {
		if (!Is.emptyString(myChart.getName())) {
			StringBuffer datasets = new StringBuffer("");
			String name = myChart.getName().startsWith(SHARED_NAME_PREFIX)
					? myChart.getName().substring(SHARED_NAME_PREFIX.length())
					: myChart.getName();
			name = (myChart.getShared() ? SHARED_NAME_PREFIX : "") + name;
			chartPreferences.put(KEY_NAME, name);
			chartPreferences.put(KEY_CHART_TYPE, myChart.getMyChartType().name());
			chartPreferences.put(KEY_X_AXES, myChart.getMyChartLabelColumn());
			chartPreferences.put(KEY_Y_AXES, datasets.toString());
			chartPreferences.put(KEY_SHARED, myChart.getShared().toString());
			int i = 0;
			for (MyChartColumn column : myChart.getColumns()) {
				column.save(chartPreferences, i++);
			}
			while (MyChartColumn.remove(chartPreferences, i)) {
				i++;
			}
			chartPreferences.flush();
		}
	}
	
	/**
	 * Loads a MyChart instance with the previously saved value if any.
	 * @param tab Associated tab.
	 * @param myChart Current chart object.
	 * @throws XavaException
	 * @throws BackingStoreException
	 */
	public void loadChart(Tab tab, MyChart myChart) throws XavaException, BackingStoreException {
		String nodeName = nodeName(myChart.getName(), myChart.getShared());
		loadChart(tab, myChart, nodeName);
	}	
	
	/**
	 * Creates a MyChart instance with the selected view values.
	 * @param tab Associated tab.
	 * @param myChart Current chart object.
	 * @param nodeName name of the node.
	 * @throws XavaException
	 * @throws BackingStoreException
	 */
	public void loadChart(Tab tab, MyChart myChart, String nodeName) throws XavaException, BackingStoreException {
		if (!Is.emptyString(nodeName)) {
			Preferences rootPreferences = getRootPreferenceNode(tab, nodeName);
			if (rootPreferences != null &&
					rootPreferences.nodeExists(nodeName)) {
				Preferences chartPreferences = rootPreferences.node(nodeName);
				if (!readChart(tab, myChart, chartPreferences)) {
					fillEmpty(tab, myChart, nodeName);
				} else {
					int index = 0;
					MyChartColumn column = new MyChartColumn();
					while (column.load(chartPreferences, index)) {
						index++;
					}
					myChart.setNameEditable(false);
					myChart.setChanged(false);
				}
			}
		} else {
			fillEmpty(tab, myChart, nodeName);
		}
	}
	
	/**
	 * Tries to read chart properties from the preferences.
	 * @param myChart Current chart object.
	 * @param rootPreferences Preferences to read the properties from.
	 * @param name Name of the chart to load.
	 * @return Instance of MyChart or null if none is found.
	 * @throws XavaException
	 * @throws BackingStoreException
	 */
	private boolean readChart(Tab tab, MyChart myChart, Preferences chartPreferences) throws XavaException, BackingStoreException {
		boolean returnValue = false;
		try {
			if (!Is.emptyString(chartPreferences.get(KEY_NAME, null))) {
				myChart.setName(chartPreferences.name());
				myChart.setMyChartLabelColumn(chartPreferences.get(KEY_X_AXES, ""));
				myChart.setShared(chartPreferences.getBoolean(KEY_SHARED, false));
				myChart.setMyChartType(MyChart.MyChartType.valueOf(chartPreferences.get(KEY_CHART_TYPE, "BAR")));
				int index = 0;
				MyChartColumn column = new MyChartColumn();
				myChart.getColumns().clear();
				while(column.load(chartPreferences, index++)) {
					column.setChart(myChart);
					myChart.getColumns().add(column);
					column = new MyChartColumn();
				}
				returnValue = true;
			}
		} catch (Exception e) {
			log.error(e.getMessage() + ": keeping same chart");
		}
		return returnValue;
	}
			
	/**
	 * Finds the label column.
	 * @param tab Tab to read the data from.
	 * @return First column of the model.
	 */
	private String getAxisColumns(Tab tab) {
		String returnValue = "";
		for (Object metaPropertyObject : tab.getMetaProperties()) {
			MetaProperty metaProperty = (MetaProperty)metaPropertyObject;
			returnValue = metaProperty.getName();
			break;
		}
		return returnValue;
	}
	
	/**
	 * Update Actions
	 * @param request Originating request.
	 * @param view Current view
	 * @param tab current tab.
	 * @param myChart Current chart.
	 * @throws XavaException
	 * @throws BackingStoreException
	 */
	public void updateView(HttpServletRequest request, View view, Tab tab, MyChart myChart) throws XavaException, BackingStoreException {
		MetaProperty labelMetaProperty = null;
		String name = view.getValueString("name");
		view.setModel(myChart);
		try {
			labelMetaProperty = tab.getMetaTab().getMetaModel().getMetaProperty(myChart.getMyChartLabelColumn());
		} catch (Exception e) {
			log.debug(e.getMessage());
		}
		view.setEditable("name", false);
		if (Is.emptyString(view.getValueString("name"))) {
			view.setValue("name", name);
		}
		view.setValue("myChartLabelColumn", myChart.getMyChartLabelColumn());
		view.setValue("myChartType", myChart.getMyChartType());
		view.setValueNotifying("myChartData", myChart.getMyChartType().jsType()
				 + CHART_DATA_SEPARATOR 
				 + myChart.getMyChartType().grouped()
				 + CHART_DATA_SEPARATOR
				 + myChart.getMyChartType().name()
				 + CHART_DATA_SEPARATOR
				 + (labelMetaProperty != null ? labelMetaProperty.isNumber() : "false")
				 + CHART_DATA_SEPARATOR
				 + (labelMetaProperty != null ? labelMetaProperty.getLabel() : "")
				 + CHART_DATA_SEPARATOR
				 + (new Date()).getTime()
				 );
		setActions(view, tab, myChart.isNameEditable() || getAllChartNodeNames(tab).size() == 0, myChart.getShared(), myChart.isChanged());
		createTab(request, tab, myChart);
	}
	
	/**
	 * Sets the view according to the name editable value.
	 * @param view Current view.
	 * @param tab Associated tab.
	 * @param editable Indicates whether or not set the name editable.
	 * @param shared Indicates that element is shared.
	 * @param changed Indicates if the save action should be present.
	 */
	public void setActions(View view, Tab tab, boolean editable, Boolean shared, boolean changed) {
		view.removeActionForProperty("name", "MyChart.save");
		view.removeActionForProperty("name", "MyChart.share");
		view.removeActionForProperty("name", "MyChart.makePrivate");
		view.removeActionForProperty("name", "MyChart.createNew");
		view.removeActionForProperty("name", "MyChart.remove");
		if (changed) {
			view.addActionForProperty("name", "MyChart.save");
		}
		if (editable) {
			view.setEditable("name", true);
		} else {
			view.setEditable("name", false);
			view.addActionForProperty("name", "MyChart.createNew");
		}
		view.addActionForProperty("name", "MyChart.remove");
		if (shared != null && shared) {
			view.addActionForProperty("name", "MyChart.makePrivate");
		} else {
			view.addActionForProperty("name", "MyChart.share");			
		}
	}
	
	/**
	 * Removes a chart from the preferences.
	 * @param tab Associated tab.
	 * @param name Name of the chart.
	 * @param shared Shared state.
	 * @throws BackingStoreException
	 */
	public void removeChart(Tab tab, String nodeName) throws BackingStoreException {
		removeChartPreferences(tab, nodeName);
	}

	/* Preferences section */

	/**
	 * The node name for a given name.
	 * @param sentName Name of the chart.
	 * @param sentShared Indicates if the name should represent a shared chart.
	 * @return Node name, never null. Might be empty.
	 */
	private String nodeName(String sentName, boolean sentShared) {
		if (!Is.emptyString(sentName)) {
			boolean shared = sentName.startsWith(SHARED_NAME_PREFIX)
					? true : sentShared;
			String name = sentName.startsWith(SHARED_NAME_PREFIX)
					? sentName.substring(SHARED_NAME_PREFIX.length())
					: sentName;
			if (shared) {
				return (name + SHARED_SUFFIX).toUpperCase();
			} else {
				return name.toUpperCase();
			}
		} else {
			return "";
		}
	}
	
	/**
	 * Removes charts from the preferences.
	 * @param myChart Current chart object.
	 * @throws BackingStoreException
	 */
	private void removeChartPreferences(Tab tab, String nodeName) throws BackingStoreException {
		Preferences preferences = getRootPreferenceNode(tab, nodeName);
		if (!Is.emptyString(nodeName) &&
				preferences != null &&
				preferences.nodeExists(nodeName)) {
			preferences.node(nodeName).removeNode();
			preferences.flush();
		}
	}
	
	/**
	 * Gets all chart names. First the private ones then the shared one.
	 * @param tab Current tab.
	 * @return A list of all chart names.
	 * @throws BackingStoreException
	 */
	public List<String> getAllChartNodeNames(Tab tab) throws BackingStoreException {
		List<String> returnValue = new ArrayList<String>();
		for (String nodeName : getAllUserChartNames(tab)) {
			if (!Is.emptyString(nodeName) && !KEY_LAST_NAME.equals(nodeName)) {
				returnValue.add(nodeName);
			}
		}
		for (String nodeName : getAllSharedChartNames(tab)) {
			if (!Is.emptyString(nodeName) && !KEY_LAST_NAME.equals(nodeName)) {
				returnValue.add(nodeName);
			}
		}
		return returnValue;
	}

	/**
	 * Gets last name used.
	 * @param myChart Current chart object.
	 * @return Last name used or if none used before, current tab title. Never null.
	 * @throws BackingStoreException
	 */
	public String getLastNodeNameUsed(Tab tab) throws BackingStoreException {
		return getRootPreferenceNode(tab, "").node(KEY_LAST_NAME).get(KEY_NAME, null);
	}
	
	/**
	 * Changes the last name used.
	 * @param nodeName Name of the last node used.
	 * @throws BackingStoreException
	 */
	public void setLastNodeNameUsed(Tab tab, String nodeName) throws BackingStoreException {
		if (!Is.emptyString(nodeName)) {
			getRootPreferenceNode(tab, "").node(KEY_LAST_NAME).put(KEY_NAME, nodeName);
		}
	}

	/**
	 * Gets the saved name for the chart.
	 * @param tab Associated tab.
	 * @param nodeName Name of the node
	 * @return Name as saved in preferences.
	 * @throws BackingStoreException
	 */
	public String getChartPreferenceName(Tab tab, String nodeName) throws BackingStoreException {
		Preferences preferences = getChartPreferenceNode(tab, nodeName, false);
		String name = preferences != null ? preferences.get(KEY_NAME, "") : "";
		if (nodeName.endsWith(SHARED_SUFFIX)) {
			if (!name.startsWith(SHARED_NAME_PREFIX)) {
				name = SHARED_NAME_PREFIX + name;
			}
		}
		return name;
	}
	
	/**
	 * Create the columns for the current chart. Reads the current
	 * tab and creates a fresh set of columns.
	 * @param tab Current tab.
	 * @param myChart Chart object.
	 * @return A List of created columns.
	 */
	private List<MyChartColumn> createColumns(Tab tab, MyChart myChart, boolean addNumeric, String ignore) {
		List<MyChartColumn> columns = new ArrayList<MyChartColumn>();
		String [] comparators = tab.getConditionComparators();
		String [] values = tab.getConditionValues();
		boolean numericChosen = false;
		int i = 0;
		for (MetaProperty property: tab.getMetaProperties()) {		
			MyChartColumn column = new MyChartColumn();
			column.setChart(myChart);
			column.setName(property.getQualifiedName());
			column.setLabel(property.getQualifiedLabel(Locales.getCurrent()));
			column.setCalculated(property.isCalculated());
			column.setNumber(property.isNumber());
			column.setDisplayed(false);
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
			try {
				if (addNumeric 
						&& property.isNumber() 
						&& !numericChosen
						&& !property.getName().equals(ignore)) {
					column.setDisplayed(true);
					numericChosen = true;
				}
				if ((!Is.emptyString(column.getComparator()) && !Is.emptyString(column.getValue()))
						|| (column.isDisplayed() && column.isNumber())) {
					columns.add(column);
				}
			} catch (Exception e) {
				log.debug(e.getMessage());
			}
		}		
		return columns;		
	}
	
	/**
	 * Populates the tab for chart displaying purposes.
	 * @param request Originating request.
	 * @param tab Current tab.
	 * @param myChart Chart.
	 */
	private void createTab(HttpServletRequest request, Tab tab, MyChart myChart)  {		
		Tab chartTab = new NoPreferencesTab();
		chartTab.setRequest(request);
		chartTab.setModelName(tab.getModelName());
		chartTab.setTabName(tab.getTabName());
		chartTab.clearProperties();
		chartTab.clearCondition();
		Collection<String> comparators = new ArrayList<String>();
		Collection<String> values = new ArrayList<String>();
		StringBuffer order = new StringBuffer();
		for (MyChartColumn column: myChart.getColumns()) {
			addColumn(chartTab, comparators, values, order, column);
		}
		
		for (MyChartColumn column : createColumns(tab, myChart, false, null)) {
			addColumn(chartTab, comparators, values, order, column);
		}
		
		if (!Is.emptyString(myChart.getMyChartLabelColumn())) {
			MyChartColumn column = new MyChartColumn();
			MetaProperty property = tab.getMetaTab().getMetaModel().getMetaProperty(myChart.getMyChartLabelColumn());
			if (property != null) {
				column.setChart(myChart);
				column.setName(myChart.getMyChartLabelColumn());
				column.setLabel(property.getQualifiedLabel(Locales.getCurrent()));
				column.setCalculated(property.isCalculated());
				column.setNumber(property.isNumber());
				addColumn(chartTab, comparators, values, order, column);
			}
		}

		if (order.length() > 0) {
			chartTab.setDefaultOrder(order.toString());			
		}		
		chartTab.setConditionComparators(comparators);
		chartTab.setConditionValues(values);
		request.getSession().setAttribute("xava_chartTab", chartTab);
	}

	/**
	 * Adds a column to the given tab.
	 * @param tab Container tab.
	 * @param comparators List of comparators.
	 * @param values List of values.
	 * @param order String defining the order for the columns.
	 * @param column Column to be added.
	 */
	private void addColumn(Tab tab, Collection<String> comparators,
			Collection<String> values, StringBuffer order,
			MyChartColumn column) 
	{
		if (!("," + tab.getPropertiesNamesAsString() + ",").contains("," + column.getName() + ",")) {
			tab.addProperty(column.getName());
			tab.setLabel(column.getName(), column.getLabel()); 
			if (column.isCalculated())
				return;
			if (column.getComparator() != null) {
				comparators.add(column.getComparator());
				values.add(column.getValueForCondition());				
			}
			else {
				comparators.add(null);
				values.add(null);				
			}
			if (column.getOrder() != null) {
				order.append(order.length() == 0?"":", ");
				order.append("${");
				order.append(column.getName());
				order.append("} ");
				order.append(column.getOrder() == MyChartColumn.Order.ASCENDING?"ASC":"DESC");				
			}
			if (column.isSum()) {
				tab.addSumProperty(column.getName());
			}
			else {
				tab.removeSumProperty(column.getName());
			}
		}
	}
	
	/**
	 * Creates or returns the chart preference node.
	 * @param tab Associated tab.
	 * @param nodeName Node name.
	 * @param create If true the node is created.
	 * @return Chart preference node.
	 * @throws BackingStoreException
	 */
	private Preferences getChartPreferenceNode(Tab tab, String nodeName, boolean create) throws BackingStoreException {
		Preferences rootPreferences = getRootPreferenceNode(tab, nodeName);
		if (rootPreferences.nodeExists(nodeName) || create) {
			return rootPreferences.node(nodeName);
		} else {
			return null;
		}
	}
	
	/**
	 * Find the preferences node according to the name.
	 * @param tab Associated tab.
	 * @param nodeName Node name to search for.
	 * @return Found preference or null.
	 * @throws BackingStoreException
	 */
	private Preferences getRootPreferenceNode(Tab tab, String nodeName) throws BackingStoreException {
		Preferences preferences = null;
		String rootNodeName = tab.friendMyReportGetPreferencesNodeName("myChart.");
		if (nodeName.endsWith(SHARED_SUFFIX)) {
			preferences = Users.getSharedPreferences().node(rootNodeName);
		} else {
			preferences = Users.getCurrentPreferences().node(rootNodeName);
		}
		preferences.flush();
		return preferences;
	}
	
	/**
	 * Gets all the name of the user only charts.
	 * @param myChart Current chart object.
	 * @return list of names.
	 * @throws BackingStoreException
	 */
	private String[] getAllUserChartNames(Tab tab) throws BackingStoreException {
		return getRootPreferenceNode(tab, "").childrenNames();
	}
	
	/**
	 * Gets all the name of the shared charts.
	 * @param myChart Current chart object.
	 * @return List of names.
	 * @throws BackingStoreException
	 */
	private String[] getAllSharedChartNames(Tab tab) throws BackingStoreException {
		return getRootPreferenceNode(tab, SHARED_SUFFIX).childrenNames();
	}

}
