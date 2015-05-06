/**
 * 
 */
package org.openxava.actions;

import javax.inject.Inject;

import org.openxava.session.MyChart;
import org.openxava.util.MyCharts;
import org.openxava.util.Users;

/**
 * @author Federico Alcantara
 *
 */
public class ShareMyChartAction extends TabBaseAction {
	@Inject
	private MyChart myChart;
	
	private boolean shared;
	/**
	 * @see org.openxava.actions.IAction#execute()
	 */
	@Override
	public void execute() throws Exception {
		myChart.setShared(isShared());
		MyCharts.INSTANCE.saveChart(getView(), getTab(), myChart);
		MyCharts.INSTANCE.updateView(getRequest(), getView(), getTab(), myChart);
		if (myChart.getShared()) {
			addInfo("my_chart_shared", myChart.getName());
		} else {
			addInfo("my_chart_private", myChart.getName(), Users.getCurrent());
		}
	}
	
	public boolean isShared() {
		return shared;
	}
	public void setShared(boolean share) {
		this.shared = share;
	}

}
