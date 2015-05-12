/**
 * 
 */
package org.openxava.actions;

import java.util.List;

import javax.inject.Inject;

import org.openxava.session.Chart;
import org.openxava.util.Is;
import org.openxava.web.Charts;

/**
 * Removes a given chart.
 * @author Federico Alcantara
 *
 */
public class RemoveChartAction extends TabBaseAction {
	@Inject
	private Chart chart;

	/**
	 * @see org.openxava.actions.IAction#execute()
	 */
	@Override
	public void execute() throws Exception {
		String nodeNameToLoad = "";
		String removedName = chart.getName();
		if (!Is.emptyString(removedName)) {
			Charts.INSTANCE.removeChart(getTab(), chart.getName());
			List<String>nodeNames = Charts.INSTANCE.getAllChartNodeNames(getTab());
			if (!nodeNames.isEmpty()) {
				nodeNameToLoad = nodeNames.get(0);
				Charts.INSTANCE.loadChart(getTab(), chart, nodeNameToLoad);
			} else {
				Charts.INSTANCE.fillEmpty(getTab(), chart, null);
			}
			Charts.INSTANCE.updateView(getRequest(), getView(), getTab(), chart);
			addMessage("my_chart_removed", removedName);
		} else {
			addWarning("my_chart_not_removed");
		}
	}

}
