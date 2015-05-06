/**
 * 
 */
package org.openxava.actions;

import javax.inject.Inject;

import org.openxava.session.MyChart;
import org.openxava.util.MyCharts;

/**
 * Creates a new instance of my chart action.
 * @author Federico Alcantara
 *
 */
public class CreateNewMyChartAction extends TabBaseAction {
	@Inject
	private MyChart myChart;
	
	/**
	 * @see org.openxava.actions.IAction#execute()
	 */
	@Override
	public void execute() throws Exception {
		MyCharts.INSTANCE.saveChart(getView(), getTab(), myChart);
		getView().setValue("name", "");
		MyCharts.INSTANCE.fillEmpty(getTab(), myChart, "");
		MyCharts.INSTANCE.updateView(getRequest(), getView(), getTab(), myChart);
		MyCharts.INSTANCE.setActions(getView(), getTab(), myChart.isNameEditable(), false, true);
	}

}
