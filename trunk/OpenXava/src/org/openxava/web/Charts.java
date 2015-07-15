/**
 * 
 */
package org.openxava.web;

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
import org.openxava.session.Chart;
import org.openxava.session.Chart.ChartType;
import org.openxava.session.ChartColumn;
import org.openxava.tab.Tab;
import org.openxava.util.Is;
import org.openxava.util.Locales;
import org.openxava.util.Users;
import org.openxava.util.XavaException;
import org.openxava.util.XavaResources;
import org.openxava.view.View;

/**
 * Charts helper.
 * @author Federico Alcantara
 *
 */
public enum Charts {
	INSTANCE;
	private final Log log = LogFactory.getLog(Charts.class);
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
	 * Creates an empty Chart.
	 * @param tab Tab to read the data from.
	 * @param chart Current chart instance.
	 */
	public void fillEmpty(Tab tab, Chart chart, String defaultName) {
		String name  = defaultName != null ? defaultName : tab.getTitle();
		name = nodeName(name, false);
		chart.setName(name);
		chart.setyColumn(getAxisColumns(tab));
		// Select the first column that is not the same as the label column
		List<ChartColumn> columns = createColumns(tab, chart, true, chart.getyColumn());
		chart.setMetaModel(tab.getMetaTab().getMetaModel());
		chart.setChartType(ChartType.BAR);
		chart.setChanged(true);
		chart.setNameEditable(true);
		chart.setShared(false);
		chart.setColumns(columns);
	}
	
	/**
	 * Fills a chart object with the data collected from the view.
	 * @param chart Current chart object.
	 * @throws BackingStoreException 
	 * @throws XavaException 
	 */
	private void fillFromView(View view, Tab tab, Chart chart) throws XavaException, BackingStoreException {
		if (view.isEditable("name")) {
			chart.setName(view.getValueString("name"));
		} else {
			String name = Charts.INSTANCE.getChartPreferenceName(tab, view.getValueString("name"));
			chart.setName(name);
		}
		chart.setChartType((Chart.ChartType)view.getValue("chartType"));
		chart.setyColumn(view.getValueString("yColumn"));
	}
	
	/**
	 * Saves current chart value.
	 * @param chart Current chart object.
	 * @throws XavaException 
	 * @throws BackingStoreException
	 */
	public void saveChart(View view, Tab tab, Chart chart) throws XavaException, BackingStoreException {
		if (!Is.emptyString(chart.getName())) {
			fillFromView(view, tab, chart);
			// Makes sure to delete the correct one when changing sharing state
			String nodeName = nodeName(chart.getName(), false); 
			removeChartPreferences(tab, nodeName); 
			String name = chart.getName().startsWith(SHARED_NAME_PREFIX)
					? chart.getName().substring(SHARED_NAME_PREFIX.length())
					: chart.getName();
			chart.setName(name);
			nodeName = nodeName(chart.getName(), chart.getShared());		
			saveChart(chart, getChartPreferenceNode(tab, nodeName, true));
			chart.setChanged(false);
			chart.setNameEditable(false);
		}
	}
	
	/**
	 * Saves the chart properties in the appropriate preferences.
	 * @param chart Current chart object.
	 * @param chartPreferences Preferences for the chart.
	 * @param suffix string to add at the end of the name.
	 * @throws BackingStoreException
	 */
	private void saveChart(Chart chart, Preferences chartPreferences) throws BackingStoreException {
		if (!Is.emptyString(chart.getName())) {
			String name = chart.getName().startsWith(SHARED_NAME_PREFIX)
					? chart.getName().substring(SHARED_NAME_PREFIX.length())
					: chart.getName();
			name = (chart.getShared() ? SHARED_NAME_PREFIX : "") + name;
			chartPreferences.put(KEY_NAME, name);
			chartPreferences.put(KEY_CHART_TYPE, chart.getChartType().name());
			chartPreferences.put(KEY_X_AXES, chart.getyColumn());
			chartPreferences.put(KEY_SHARED, chart.getShared().toString());
			int i = 0;
			for (ChartColumn column : chart.getColumns()) {
				column.save(chartPreferences, i++);
			}
			while (ChartColumn.remove(chartPreferences, i)) {
				i++;
			}
			chartPreferences.flush();
		}
	}
	
	/**
	 * Loads a Chart instance with the previously saved value if any.
	 * @param tab Associated tab.
	 * @param chart Current chart object.
	 * @throws XavaException
	 * @throws BackingStoreException
	 */
	public void loadChart(Tab tab, Chart chart) throws XavaException, BackingStoreException {
		String nodeName = nodeName(chart.getName(), chart.getShared());
		loadChart(tab, chart, nodeName);
	}	
	
	/**
	 * Creates a Chart instance with the selected view values.
	 * @param tab Associated tab.
	 * @param chart Current chart object.
	 * @param nodeName name of the node.
	 * @throws XavaException
	 * @throws BackingStoreException
	 */
	public void loadChart(Tab tab, Chart chart, String nodeName) throws XavaException, BackingStoreException {
		if (!Is.emptyString(nodeName)) {
			Preferences rootPreferences = getRootPreferenceNode(tab, nodeName);
			if (rootPreferences != null &&
					rootPreferences.nodeExists(nodeName)) {
				Preferences chartPreferences = rootPreferences.node(nodeName);
				if (!readChart(tab, chart, chartPreferences)) {
					fillEmpty(tab, chart, nodeName);
				} else {
					int index = 0;
					ChartColumn column = new ChartColumn();
					while (column.load(chartPreferences, index)) {
						index++;
					}
					chart.setNameEditable(false);
					chart.setChanged(false);
				}
			}
		} else {
			fillEmpty(tab, chart, nodeName);
		}
	}
	
	/**
	 * Tries to read chart properties from the preferences.
	 * @param chart Current chart object.
	 * @param rootPreferences Preferences to read the properties from.
	 * @param name Name of the chart to load.
	 * @return Instance of Chart or null if none is found.
	 * @throws XavaException
	 * @throws BackingStoreException
	 */
	private boolean readChart(Tab tab, Chart chart, Preferences chartPreferences) throws XavaException, BackingStoreException {
		boolean returnValue = false;
		try {
			if (!Is.emptyString(chartPreferences.get(KEY_NAME, null))) {
				chart.setName(chartPreferences.name());
				chart.setyColumn(chartPreferences.get(KEY_X_AXES, ""));
				chart.setShared(chartPreferences.getBoolean(KEY_SHARED, false));
				chart.setChartType(Chart.ChartType.valueOf(chartPreferences.get(KEY_CHART_TYPE, "BAR")));
				int index = 0;
				ChartColumn column = new ChartColumn();
				chart.setColumns(new ArrayList<ChartColumn>());
				while(column.load(chartPreferences, index++)) {
					column.setChart(chart);
					chart.getColumns().add(column);
					column = new ChartColumn();
				}
				returnValue = true;
			}
		} catch (Exception e) { 
			log.warn(XavaResources.getString("chart_not_loaded"), e);
		}
		return returnValue;
	}
			
	/**
	 * Finds the label column.
	 * @param tab Tab to read the data from.
	 * @return First column of the model.
	 */
	private String getAxisColumns(Tab tab) {
		for (Object metaPropertyObject : tab.getMetaProperties()) {
			MetaProperty metaProperty = (MetaProperty)metaPropertyObject;
			return metaProperty.getQualifiedName(); 			
		}
		return "";	
	}
	
	/**
	 * Update Actions
	 * @param request Originating request.
	 * @param view Current view
	 * @param tab current tab.
	 * @param chart Current chart.
	 * @throws XavaException
	 * @throws BackingStoreException
	 */
	public void updateView(HttpServletRequest request, View view, Tab tab, Chart chart) throws XavaException, BackingStoreException {
		if (!chart.isRendered()) {
			chart.setRendered(true);
			MetaProperty labelMetaProperty = null;
			String name = view.getValueString("name");
			view.setModel(chart);
			try {
				labelMetaProperty = tab.getMetaTab().getMetaModel().getMetaProperty(chart.getyColumn());
			} catch (Exception e) {
				log.debug(e.getMessage());
			}
			view.setEditable("name", false);
			if (Is.emptyString(view.getValueString("name"))) {
				view.setValue("name", name);
			}
			view.setValue("yColumn", chart.getyColumn());
			view.setValue("chartType", chart.getChartType());
			view.setValueNotifying("chartData", chart.getChartType().jsType()
					 + CHART_DATA_SEPARATOR 
					 + chart.getChartType().grouped()
					 + CHART_DATA_SEPARATOR
					 + chart.getChartType().name()
					 + CHART_DATA_SEPARATOR
					 + (labelMetaProperty != null ? labelMetaProperty.isNumber() : "false")
					 + CHART_DATA_SEPARATOR
					 + (labelMetaProperty != null ? labelMetaProperty.getLabel() : "")
					 + CHART_DATA_SEPARATOR
					 + (new Date()).getTime()
					 );
			setActions(view, chart.isNameEditable() || getAllChartNodeNames(tab).size() == 0, chart.getShared(), chart.isChanged());
			createTab(request, tab, chart);
			view.refreshCollections();
			chart.setRendered(false);
		}
	}
	
	/**
	 * Sets the view according to the name editable value.
	 * @param view Current view.
	 * @param tab Associated tab.
	 * @param editable Indicates whether or not set the name editable.
	 * @param shared Indicates that element is shared.
	 * @param changed Indicates if the save action should be present.
	 */
	public void setActions(View view, boolean editable, Boolean shared, boolean changed) {
		view.removeActionForProperty("name", "Chart.save");
		view.removeActionForProperty("name", "Chart.share");
		view.removeActionForProperty("name", "Chart.makePrivate");
		view.removeActionForProperty("name", "Chart.createNew");
		view.removeActionForProperty("name", "Chart.remove");
		if (changed) {
			view.addActionForProperty("name", "Chart.save");
		}
		if (editable) {
			view.setEditable("name", true);
		} else {
			view.setEditable("name", false);
			view.addActionForProperty("name", "Chart.createNew");
		}
		view.addActionForProperty("name", "Chart.remove");
		if (shared != null && shared) {
			view.addActionForProperty("name", "Chart.makePrivate");
		} else {
			view.addActionForProperty("name", "Chart.share");			
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
	 * @param tab Current tab
	 * @param nodeName node name for the chart.
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
	 * @param tab Tab to get the last node used from.
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
	 * @param chart Chart object.
	 * @return A List of created columns.
	 */
	private List<ChartColumn> createColumns(Tab tab, Chart chart, boolean addNumeric, String ignore) {
		List<ChartColumn> columns = new ArrayList<ChartColumn>();
		String [] comparators = tab.getConditionComparators();
		String [] values = tab.getConditionValues();
		boolean numericChosen = false;
		int i = 0;
		for (MetaProperty property: tab.getMetaProperties()) {
			if (!property.isNumber()) {
				continue;
			}
			ChartColumn column = new ChartColumn();
			column.setChart(chart);			
			column.setName(property.getQualifiedName());
			column.setLabel(property.getQualifiedLabel(Locales.getCurrent()));
			column.setNumber(property.isNumber());
			
			column.setDisplayed(false);
			try {
				if (addNumeric 
						&& property.isNumber() 
						&& !numericChosen
						&& !property.getName().equals(ignore)) {
					column.setDisplayed(true);
					numericChosen = true;
				}
				columns.add(column);
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
	 * @param chart Chart.
	 */
	private void createTab(HttpServletRequest request, Tab tab, Chart chart)  {		
		Tab chartTab = new Tab(true);
		chartTab.setRequest(request);
		chartTab.setModelName(tab.getModelName());
		chartTab.setTabName(tab.getTabName());
		chartTab.clearProperties();
		chartTab.clearCondition();
		Collection<String> comparators = new ArrayList<String>();
		Collection<String> values = new ArrayList<String>();
		StringBuffer order = new StringBuffer();
		for (ChartColumn column: chart.getColumns()) {
			addColumn(chartTab, comparators, values, order, column);
		}
		
		for (ChartColumn column : createColumns(tab, chart, false, null)) {
			addColumn(chartTab, comparators, values, order, column);
		}
		
		if (!Is.emptyString(chart.getyColumn())) {
			ChartColumn column = new ChartColumn();
			MetaProperty property = tab.getMetaTab().getMetaModel().getMetaProperty(chart.getyColumn());
			if (property != null) {
				column.setChart(chart);
				column.setName(chart.getyColumn());				
				column.setLabel(property.getQualifiedLabel(Locales.getCurrent()));
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
			ChartColumn column) 
	{
		if (!("," + tab.getPropertiesNamesAsString() + ",").contains("," + column.getName() + ",")) {
			tab.addProperty(column.getName());			
			tab.setLabel(column.getName(), column.getLabel());
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
		String rootNodeName = tab.friendMyReportGetPreferencesNodeName("chart.");
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
	 * @param tab Tab associated with the chart.
	 * @return list of names.
	 * @throws BackingStoreException
	 */
	private String[] getAllUserChartNames(Tab tab) throws BackingStoreException {
		return getRootPreferenceNode(tab, "").childrenNames();
	}
	
	/**
	 * Gets all the name of the shared charts.
	 * @param tab Tab associated with the chart.
	 * @return List of names.
	 * @throws BackingStoreException
	 */
	private String[] getAllSharedChartNames(Tab tab) throws BackingStoreException {
		return getRootPreferenceNode(tab, SHARED_SUFFIX).childrenNames();
	}

}
