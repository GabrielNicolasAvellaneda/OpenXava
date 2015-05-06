/**
 * 
 */
package org.openxava.actions;

import org.openxava.util.MyCharts;

/**
 * @author Federico Alcantara
 *
 */
public class OnChangeMyChartLabelColumnAction extends OnChangeMyChartBaseAction {
	public final static String SHOW_MORE="__MORE__";
	public final static String SHOW_LESS="__LESS__";
	
	/**
	 * @see org.openxava.actions.OnChangeMyChartBaseAction#executeOnValidValues()
	 */
	@Override
	public void executeOnValidValues() throws Exception {
		if (getNewValue() != null) {
			String propertyName = (String)getNewValue();
			if (propertyName.equals(SHOW_MORE)) {
				getView().getRoot().putObject("xava.myReportColumnShowAllColumns", true);
			} else if (propertyName.equals(SHOW_LESS)) {
				getView().getRoot().putObject("xava.myReportColumnShowAllColumns", false);
			} else {
				getMyChart().setMyChartLabelColumn(propertyName);
			}
		}
	}

}
