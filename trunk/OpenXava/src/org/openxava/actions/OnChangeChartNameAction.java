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
 *
 */
public class OnChangeChartNameAction extends OnChangePropertyBaseAction {

	@Inject
	private Chart chart;

	@Inject
	private Tab tab;
	
	/**
	 * @see org.openxava.actions.IAction#execute()
	 */
	@Override
	public void execute() throws Exception {
		if (getNewValue() != null) {
			Charts.INSTANCE.loadChart(tab, chart, (String)getNewValue());
			Charts.INSTANCE.setLastNodeNameUsed(tab, (String)getNewValue());
			Charts.INSTANCE.updateView(getRequest(), getView(), tab, chart);
		}
	}

}
