/**
 * 
 */
package org.openxava.actions;

import org.openxava.session.Chart;


/**
 * @author Federico Alcantara
 *
 */
public class OnChangeChartTypeAction extends OnChangeChartBaseAction{

	@Override
	protected void executeOnValidValues() throws Exception {
		getChart().setChartType((Chart.ChartType)getNewValue());
	}
	
}
