package org.openxava.actions;


/**
 * @author Javier Paniza
 */

public interface IChangeModeAction extends IAction {
	
	public final static String LIST = "list";
	public final static String DETAIL = "detail";
	
	String getNextMode();

}
