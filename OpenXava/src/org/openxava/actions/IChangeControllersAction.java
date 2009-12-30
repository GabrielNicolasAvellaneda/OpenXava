package org.openxava.actions;

/**
 * @author Javier Paniza
 */

public interface IChangeControllersAction extends IAction {
				
	final static String [] EMPTY_CONTROLLER = new String [] {};
	final static String [] DEFAULT_CONTROLLERS = new String [] { "__DEFAULT_CONTROLLERS__" };	
	final static String [] SAME_CONTROLLERS = null;
	final static String [] PREVIOUS_CONTROLLERS = new String [] { "__PREVIOUS_CONTROLLERS__" };
	
	String [] getNextControllers() throws Exception;		

}
