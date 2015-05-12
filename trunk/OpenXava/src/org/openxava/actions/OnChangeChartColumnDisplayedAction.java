/**
 * 
 */
package org.openxava.actions;



/**
 * @author Federico Alcantara
 *
 */
public class OnChangeChartColumnDisplayedAction extends
		OnChangeChartColumnBaseAction implements IOnChangePropertyAction {
	/**
	 * @see org.openxava.actions.IAction#execute()
	 */
	@Override
	public void executeOnValidValues() throws Exception {
		if (getChartColumn() != null) {
			getChartColumn().setDisplayed(Boolean.TRUE.equals(getNewValue()));
		}
	}
	
}
