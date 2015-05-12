/**
 * 
 */
package org.openxava.actions;

import javax.inject.Inject;

import org.openxava.session.Chart;
import org.openxava.session.ChartColumn;
import org.openxava.tab.Tab;
import org.openxava.web.Charts;

/**
 * @author Federico Alcantara
 *
 */
public abstract class OnChangeChartColumnBaseAction extends
		OnChangeElementCollectionBaseAction {
	@Inject
	private Chart chart;

	private Tab collectionTab;
	
	private ChartColumn column;
	/**
	 * @see org.openxava.actions.IAction#execute()
	 */
	@Override
	public void execute() throws Exception {
		getChart().setChanged(true);
		executeOnValidValues();
		Tab tab = (Tab) getContext().get(getRequest(), "xava_tab");
		if (getView() != null && tab != null) {
			Charts.INSTANCE.updateView(getRequest(), getView(), tab, getChart());
		}
	}
	
	public Chart getChart() {
		return chart;
	}
	
	/**
	 * Gets the chart column object associated with the changed property.
	 * @return Chart column object. Null if not found.
	 * @throws Exception
	 */
	public ChartColumn getChartColumn() throws Exception {
		if (column == null &&
				getRow() > -1 &&
				getChart() != null &&
				!getChart().getColumns().isEmpty() &&
				getRow() < getChart().getColumns().size()) {
			column = getChart().getColumns().get(getRow());
		}
		return column;
	}
	
	public Tab getCollectionTab() {
		if (collectionTab == null) {
			collectionTab = getCollectionElementView().getCollectionTab();
		}
		return collectionTab;
	}
	
	public abstract void executeOnValidValues() throws Exception;
}
