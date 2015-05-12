/**
 * 
 */
package org.openxava.actions;

import javax.inject.Inject;

import org.openxava.session.Chart;
import org.openxava.util.Users;
import org.openxava.web.Charts;

/**
 * @author Federico Alcantara
 *
 */
public class ShareChartAction extends TabBaseAction {
	@Inject
	private Chart chart;
	
	private boolean shared;
	/**
	 * @see org.openxava.actions.IAction#execute()
	 */
	@Override
	public void execute() throws Exception {
		chart.setShared(isShared());
		Charts.INSTANCE.saveChart(getView(), getTab(), chart);
		Charts.INSTANCE.updateView(getRequest(), getView(), getTab(), chart);
		if (chart.getShared()) {
			addInfo("my_chart_shared", chart.getName());
		} else {
			addInfo("my_chart_private", chart.getName(), Users.getCurrent());
		}
	}
	
	public boolean isShared() {
		return shared;
	}
	public void setShared(boolean share) {
		this.shared = share;
	}

}
