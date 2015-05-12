/**
 * 
 */
package org.openxava.actions;


/**
 * @author Federico Alcantara
 *
 */
public class OnChangeChartColumnSelectedAction extends OnChangePropertyBaseAction {
	public final static String SHOW_MORE="__MORE__";
	public final static String SHOW_LESS="__LESS__";
	
	/**
	 * @see org.openxava.actions.OnChangeChartBaseAction#executeOnValidValues()
	 */
	@Override
	public void execute() throws Exception {
		if (getNewValue() != null) {
			String propertyName = (String)getNewValue();
			if (propertyName.equals(SHOW_MORE)) {
				getView().getRoot().putObject("xava.myReportColumnShowAllColumns", true);
				getView().setValue(getChangedProperty(), "");
			} else if (propertyName.equals(SHOW_LESS)) {
				getView().getRoot().putObject("xava.myReportColumnShowAllColumns", false);
				getView().setValue(getChangedProperty(), "");
			}
		}
	}

}
