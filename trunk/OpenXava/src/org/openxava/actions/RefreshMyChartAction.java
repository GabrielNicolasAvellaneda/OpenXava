/**
 * 
 */
package org.openxava.actions;

import javax.inject.Inject;

import org.openxava.session.MyChart;
import org.openxava.tab.Tab;
import org.openxava.util.MyCharts;

/**
 * @author Federico Alcantara
 * Refreshes the chart.
 *
 */
public class RefreshMyChartAction extends ViewBaseAction {

	@Inject
	private Tab tab;
	
	@Inject
	private MyChart myChart;

	/**
	 * @see org.openxava.actions.IAction#execute()
	 */
	@Override
	public void execute() throws Exception {
		MyCharts.INSTANCE.updateView(getRequest(), getView(), tab, myChart);
	}

}
