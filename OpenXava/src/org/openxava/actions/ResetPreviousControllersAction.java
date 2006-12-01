package org.openxava.actions;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Delete the stack of all visited controllers. <p>
 * 
 * After you execute this action if you try to
 * return to the previous controller then you return
 * to the default controllers of module.<br>
 *  
 * @author Javier Paniza
 */


public class ResetPreviousControllersAction extends BaseAction {
	
	private Stack previousControllers;
	private Log log = LogFactory.getLog(ResetPreviousControllersAction.class);
	
	public void execute() throws Exception {
		previousControllers.clear();
	}

	public Stack getPreviousControllers() {
		return previousControllers;
	}

	public void setPreviousControllers(Stack previousControllers) {
		this.previousControllers = previousControllers;
	}

}
