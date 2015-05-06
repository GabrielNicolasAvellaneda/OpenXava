/**
 * 
 */
package org.openxava.actions;

import java.util.List;

import javax.inject.Inject;

import org.openxava.session.MyChart;
import org.openxava.util.Is;
import org.openxava.util.MyCharts;

/**
 * Removes a given chart.
 * @author Federico Alcantara
 *
 */
public class RemoveMyChartAction extends TabBaseAction {
	@Inject
	private MyChart myChart;

	/**
	 * @see org.openxava.actions.IAction#execute()
	 */
	@Override
	public void execute() throws Exception {
		String nodeNameToLoad = "";
		String removedName = myChart.getName();
		if (!Is.emptyString(removedName)) {
			MyCharts.INSTANCE.removeChart(getTab(), myChart.getName());
			List<String>nodeNames = MyCharts.INSTANCE.getAllChartNodeNames(getTab());
			if (!nodeNames.isEmpty()) {
				nodeNameToLoad = nodeNames.get(0);
				MyCharts.INSTANCE.loadChart(getTab(), myChart, nodeNameToLoad);
			} else {
				MyCharts.INSTANCE.fillEmpty(getTab(), myChart, null);
			}
			MyCharts.INSTANCE.updateView(getRequest(), getView(), getTab(), myChart);
			addMessage("my_chart_removed", removedName);
		} else {
			addWarning("my_chart_not_removed");
		}
	}

}
