/**
 * 
 */
package org.openxava.actions;

import javax.inject.Inject;

import org.openxava.session.MyChart;
import org.openxava.tab.Tab;
import org.openxava.util.MyCharts;

/**
 * Used for changes of the chart properties
 * @author Federico Alcantara
 *
 */
public abstract class OnChangeMyChartBaseAction extends OnChangePropertyBaseAction {
	@Inject
	private MyChart myChart;
	
	@Inject
	private Tab tab;
	
	protected MyChart getMyChart() {
		return myChart;
	}
	
	protected Tab getTab() {
		return tab;
	}
	
	/**
	 * @see org.openxava.actions.IAction#execute()
	 */
	@Override
	public void execute() throws Exception {
		if (getNewValue() != null) {
			getMyChart().setChanged(true);
			executeOnValidValues();
			MyCharts.INSTANCE.updateView(getRequest(), getView(), getTab(), myChart);
		}
	}
	
	/**
	 * Calls upon finding valid new values.
	 * @throws Exception
	 */
	protected abstract void executeOnValidValues() throws Exception;
}
