package org.openxava.actions;

/**
 * 
 * @author Federico Alcantara
 * 
 */
public class CreateNewMyChartColumnAction extends CreateNewElementInCollectionAction {

	public void execute() throws Exception {
		super.execute();
		getCollectionElementView().removeObject("xava.myReportColumnShowAllColumns");
	}
	
}
