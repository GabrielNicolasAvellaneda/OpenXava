package org.openxava.actions;


/**
 * @author Javier Paniza
 */

public interface IChangeModuleAction extends IAction {
	
	final static String PREVIOUS_MODULE = "__PREVIOUS_MODULE__";
	
	String getNextModule();
	
	boolean hasReinitNextModule();

}
