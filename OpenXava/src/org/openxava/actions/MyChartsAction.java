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
 * @author Federico Alcantara
 *
 */
public class MyChartsAction extends TabBaseAction {
	
	@Inject
	private MyChart myChart;
	
	@Override
	public void execute() throws Exception {
		setNextMode(DETAIL);
		showDialog();	
		getView().setModelName("MyChart");
		getView().setTitleId("myCharts");
		loadLastNode();
		setControllers("MyChart", "Dialog");
	}
	
	protected void loadLastNode() throws Exception {
		String nodeName = MyCharts.INSTANCE.getLastNodeNameUsed(getTab());
		if (myChart == null) {
			myChart = new MyChart();
		}
		List<String> charts = MyCharts.INSTANCE.getAllChartNodeNames(getTab());
		if (Is.emptyString(nodeName) || !charts.contains(nodeName)) {
			if (!charts.isEmpty()) {
				nodeName = charts.get(0);
			} else {
				nodeName = null;
			}
		}
		MyCharts.INSTANCE.loadChart(getTab(), myChart, nodeName);
		MyCharts.INSTANCE.updateView(getRequest(), getView(), getTab(), myChart);
	}
}
