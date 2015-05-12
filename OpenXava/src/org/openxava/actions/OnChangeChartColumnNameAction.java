package org.openxava.actions;

import org.openxava.session.ChartColumn;
import org.openxava.util.Labels;


/**
 * 
 * @author Federico Alcantara
 * @see org.openxava.actions.OnChangeMyReportColumnNameAction
 */

public class OnChangeChartColumnNameAction extends OnChangeChartColumnBaseAction { 

	@Override
	public void executeOnValidValues() throws Exception {
		String propertyName = (String)getNewValue();
		if (propertyName == null) {
			if (getChartColumn() != null) {
				getChart().getColumns().remove(getRow());
			}
		} else if (propertyName.equals(OnChangeChartLabelColumnAction.SHOW_MORE)) {
			getView().getRoot().putObject("xava.myReportColumnShowAllColumns", true);
		} else if (propertyName.equals(OnChangeChartLabelColumnAction.SHOW_LESS)) {
			getView().getRoot().putObject("xava.myReportColumnShowAllColumns", false);
		} else {
			ChartColumn column = getChartColumn();
			if (column == null) {
				column = new ChartColumn();
				column.setChart(getChart());
				column.setDisplayed(false);
				column.setName(propertyName);
				column.setNumber(true); // Only number types are added here.
				getChart().getColumns().add(column);
			}
			column.setName(propertyName);
			column.setLabel(Labels.get(propertyName));
		}
	}
	
}
