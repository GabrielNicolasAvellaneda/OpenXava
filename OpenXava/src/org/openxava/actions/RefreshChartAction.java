/**
 * 
 */
package org.openxava.actions;

import javax.inject.Inject;

import org.openxava.session.Chart;
import org.openxava.tab.Tab;
import org.openxava.web.Charts;

/**
 * @author Federico Alcantara
 * Refreshes the chart.
 *
 */
public class RefreshChartAction extends ViewBaseAction {

	@Inject
	private Tab tab;
	
	@Inject
	private Chart chart;

	/**
	 * @see org.openxava.actions.IAction#execute()
	 */
	@Override
	public void execute() throws Exception {
		Charts.INSTANCE.updateView(getRequest(), getView(), tab, chart);
	}

}
