/**
 * 
 */
package org.openxava.actions;

import org.openxava.session.MyChart;


/**
 * @author Federico Alcantara
 *
 */
public class OnChangeMyChartTypeAction extends OnChangeMyChartBaseAction{

	@Override
	protected void executeOnValidValues() throws Exception {
		getMyChart().setMyChartType((MyChart.MyChartType)getNewValue());
	}
	
}
