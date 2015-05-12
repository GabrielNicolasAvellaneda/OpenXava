/**
 * 
 */
package org.openxava.actions;

import javax.inject.Inject;

import org.openxava.session.Chart;
import org.openxava.util.Is;
import org.openxava.web.Charts;

/**
 * Saves current chart values.
 * @author Federico Alcantara
 *
 */
public class SaveChartAction extends TabBaseAction {
	@Inject
	private Chart chart;
	
	/**
	 * @see org.openxava.actions.IAction#execute()
	 */
	@Override
	public void execute() throws Exception {
		String name = getView().getValueString("name");
		if (!Is.emptyString(name)) {
			chart.setName(name);
			Charts.INSTANCE.saveChart(getView(), getTab(), chart);
			Charts.INSTANCE.loadChart(getTab(), chart);
			Charts.INSTANCE.updateView(getRequest(), getView(), getTab(), chart);
			String description = Charts.INSTANCE.getChartPreferenceName(getTab(), chart.getName());
			addMessage("my_chart_saved", description);
		} else {
			addError("my_chart_not_saved");
		}
	}

}
