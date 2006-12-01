package org.openxava.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Javier Paniza
 */

public class SearchAction extends BaseAction implements IChainAction {

	private Log log = LogFactory.getLog(SearchAction.class);
	
	public void execute() throws Exception {
	}
	
	public String getNextAction() throws Exception {
		return getEnvironment().getValue("XAVA_SEARCH_ACTION");		
	} 		

}
