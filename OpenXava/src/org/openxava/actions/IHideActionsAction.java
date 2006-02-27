package org.openxava.actions;

/**
 * Allows to hide a group of actions. <p>
 * 
 * @author Javier Paniza
 */

public interface IHideActionsAction {
	
	/**
	 * The list of actions to hide. <p>
	 * 
	 * @return If <code>null</code> no actions are hidden.
	 */
	String [] getActionsToHide();

}
