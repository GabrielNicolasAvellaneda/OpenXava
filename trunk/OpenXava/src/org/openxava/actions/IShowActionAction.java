package org.openxava.actions;

/**
 * Allows to show an action previously hidden. <p>
 * 
 * @author Javier Paniza
 */

public interface IShowActionAction {

	/**
	 * The action to show. <p>
	 * 
	 * @return If <code>null</code> no action is shown.
	 */
	String getActionToShow();
	
}
