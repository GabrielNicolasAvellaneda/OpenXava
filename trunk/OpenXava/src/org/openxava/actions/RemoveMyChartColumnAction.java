package org.openxava.actions;

import javax.inject.Inject;

import org.openxava.session.MyChart;
import org.openxava.tab.Tab;
import org.openxava.util.MyCharts;

/**
 * 
 * @author Federico Alcantara
 * @see org.openxava.actions.RemoveMyReportColumnAction
 */
public class RemoveMyChartColumnAction extends CollectionBaseAction {
	
	@Inject
	private Tab tab;
	
	@Inject
	private MyChart myChart; 
	
	public void execute() throws Exception {
		for (Object o: getSelectedObjects()) {
			myChart.getColumns().remove(o);
		}
		MyCharts.INSTANCE.updateView(getRequest(), getView(), tab, myChart);
	}

}
