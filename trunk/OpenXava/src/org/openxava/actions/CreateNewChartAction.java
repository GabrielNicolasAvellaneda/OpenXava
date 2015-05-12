/**
 * 
 */
package org.openxava.actions;

import javax.inject.Inject;

import org.openxava.session.Chart;
import org.openxava.web.Charts;

/**
 * Creates a new instance of my chart action.
 * @author Federico Alcantara
 *
 */
public class CreateNewChartAction extends TabBaseAction {
	@Inject
	private Chart chart;
	
	/**
	 * @see org.openxava.actions.IAction#execute()
	 */
	@Override
	public void execute() throws Exception {
		Charts.INSTANCE.saveChart(getView(), getTab(), chart);
		getView().setValue("name", "");
		Charts.INSTANCE.fillEmpty(getTab(), chart, "");
		Charts.INSTANCE.updateView(getRequest(), getView(), getTab(), chart);
		Charts.INSTANCE.setActions(getView(), chart.isNameEditable(), false, true);
	}

}
