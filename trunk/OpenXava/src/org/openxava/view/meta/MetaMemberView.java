package org.openxava.view.meta;

import java.util.*;

/**
 * A little minimum common denominator for 
 * <code>MetaPropertyView</code> and <code>MetaReferenceView</code>. <p>
 * 
 * @author Javier Paniza
 */

public class MetaMemberView {
	
	private Collection actionsNames;
	
	public void addActionName(String actionName) {
		if (actionsNames == null) actionsNames = new ArrayList();
		actionsNames.add(actionName);
	}
	public Collection getActionsNames() {		
		return actionsNames==null?Collections.EMPTY_LIST:actionsNames;
	}		

}
