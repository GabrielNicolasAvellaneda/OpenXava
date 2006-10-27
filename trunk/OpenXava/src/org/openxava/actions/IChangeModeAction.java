package org.openxava.actions;

/**
 * @author Javier Paniza
 */

public interface IChangeModeAction extends IAction {
	
	public final static String LIST = "list";
	public final static String DETAIL = "detail";
	public final static String PREVIOUS_MODE = "__PREVIOUS_MODE__";
	
	String getNextMode();

}
