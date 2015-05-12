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
 * @author Federico Alcantara
 *
 */
public class ChartsAction extends TabBaseAction {
	
	@Inject
	private Chart chart;
	
	@Override
	public void execute() throws Exception {
		setNextMode(DETAIL);
		showDialog();	
		getView().setModelName("Chart");
		getView().setTitleId("charts");
		loadLastNode();
		setControllers("Chart", "Dialog");
	}
	
	protected void loadLastNode() throws Exception {
		String nodeName = Charts.INSTANCE.getLastNodeNameUsed(getTab());
		if (chart == null) {
			chart = new Chart();
		}
		List<String> charts = Charts.INSTANCE.getAllChartNodeNames(getTab());
		if (Is.emptyString(nodeName) || !charts.contains(nodeName)) {
			if (!charts.isEmpty()) {
				nodeName = charts.get(0);
			} else {
				nodeName = null;
			}
		}
		Charts.INSTANCE.loadChart(getTab(), chart, nodeName);
		Charts.INSTANCE.updateView(getRequest(), getView(), getTab(), chart);
	}
}
