package org.openxava.actions;

import java.util.Date;
import java.util.Map;

import javax.inject.Inject;

import org.openxava.model.MapFacade;
import org.openxava.model.meta.MetaProperty;
import org.openxava.session.MyChart;
import org.openxava.session.MyChartColumn;
import org.openxava.tab.Tab;
import org.openxava.util.Is;
import org.openxava.util.Messages;
import org.openxava.util.MyCharts;

/**
 * 
 * @author Federico Alcantara
 * @see org.openxava.actions.SaveMyReportColumnAction 
 */

public class SaveMyChartColumnAction extends CollectionElementViewBaseAction {
	
	@Inject
	private MyChart myChart; 

	@SuppressWarnings("rawtypes")
	public void execute() throws Exception {
		Map values = getCollectionElementView().getValues();
		Messages errors = MapFacade.validate("MyChartColumn", values);
		if (!errors.isEmpty()) {
			addErrors(errors);
			return;
		}
		MyChartColumn column = new MyChartColumn();
		column.setChart(myChart);
		String columnName = (String) values.get("name"); 
		column.setName(columnName);
		String columnLabel = (String) values.get("label"); 
		column.setLabel(columnLabel);
		if (getCollectionElementView().getMembersNames().containsKey("value")) {
			String value = (String) values.get("value");
			column.setValue(value);
		}
		Date dateValue = (Date) values.get("dateValue");
		column.setDateValue(dateValue);		
		if (getCollectionElementView().getMembersNames().containsKey("comparator")) {
			String comparator = (String) values.get("comparator");
			if (!Is.emptyString(column.getValue())) {
				column.setComparator(comparator);
			}
		}
		Boolean booleanValue = (Boolean) values.get("booleanValue");
		column.setBooleanValue(booleanValue);
		Integer validValuesValue = (Integer) values.get("validValuesValue");
		column.setValidValuesValue(validValuesValue==null?0:validValuesValue);		
		String descriptionsListValue = (String) values.get("descriptionsListValue"); 		
		column.setDescriptionsListValue(descriptionsListValue);
		MyChartColumn.Order order = (MyChartColumn.Order) values.get("order"); 
		column.setOrder(order);	
		Boolean sum = (Boolean) values.get("sum"); 
		column.setSum(sum==null?false:sum);
		Boolean displayed = (Boolean) values.get("displayed"); 
		column.setDisplayed(displayed==null?false:displayed);		

		if (myChart.getMetaModel() != null) {
			MetaProperty property = myChart.getMetaModel().getMetaProperty(columnName);
			if (property != null) {
				column.setCalculated(property.isCalculated());
				column.setNumber(property.isNumber());
				if (!property.isNumber() && column.isDisplayed()) {
					column.setDisplayed(false);
					addWarning("chart_column_changed_to_undisplayed", "'" + columnName + "'");
				}
			}
		}

		if (getCollectionElementView().getCollectionEditingRow() < 0) {
			if (!alreadyExists(columnName)) {
				myChart.getColumns().add(column);
				addMessage("column_added_to_chart", "'" + columnName + "'");						
			}			
		}
		else {
			myChart.getColumns().set(getCollectionElementView().getCollectionEditingRow(), column);
			addMessage("chart_column_modified", "'" + columnName + "'");			
		}
		closeDialog();
		Tab tab = (Tab) getContext().get(getRequest(), "xava_tab");
		if (tab != null) {
			MyCharts.INSTANCE.updateView(getRequest(), getView(), tab, myChart);
		}
	}

	private boolean alreadyExists(String columnName) {
		for (MyChartColumn column: myChart.getColumns()) {
			if (column.getName().equals(columnName)) return true;
		}
		return false;
	}

}
