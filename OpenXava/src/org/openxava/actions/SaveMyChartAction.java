/**
 * 
 */
package org.openxava.actions;

import javax.inject.Inject;

import org.openxava.session.MyChart;
import org.openxava.util.Is;
import org.openxava.util.MyCharts;

/**
 * Saves current chart values.
 * @author Federico Alcantara
 *
 */
public class SaveMyChartAction extends TabBaseAction {
	@Inject
	private MyChart myChart;
	
	/**
	 * @see org.openxava.actions.IAction#execute()
	 */
	@Override
	public void execute() throws Exception {
		String name = getView().getValueString("name");
		if (!Is.emptyString(name)) {
			myChart.setName(name);
			MyCharts.INSTANCE.saveChart(getView(), getTab(), myChart);
			MyCharts.INSTANCE.loadChart(getTab(), myChart);
			MyCharts.INSTANCE.updateView(getRequest(), getView(), getTab(), myChart);
			String description = MyCharts.INSTANCE.getChartPreferenceName(getTab(), myChart.getName());
			addMessage("my_chart_saved", description);
		} else {
			addError("my_chart_not_saved");
		}
	}

}
