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
 *
 */
public class OnChangeMyChartNameAction extends OnChangePropertyBaseAction {

	@Inject
	private MyChart myChart;

	@Inject
	private Tab tab;
	
	/**
	 * @see org.openxava.actions.IAction#execute()
	 */
	@Override
	public void execute() throws Exception {
		if (getNewValue() != null) {
			MyCharts.INSTANCE.loadChart(tab, myChart, (String)getNewValue());
			MyCharts.INSTANCE.setLastNodeNameUsed(tab, (String)getNewValue());
			MyCharts.INSTANCE.updateView(getRequest(), getView(), tab, myChart);
		}
	}

}
