package org.openxava.actions;

/**
 * @author Javier Paniza
 */

public interface IRemoteAction extends IAction {
	
	void executeBefore() throws Exception;
	
	void executeAfter() throws Exception;
	
	boolean isExecuteOnServer();
	
	/**
	 * Package, usually the project name in lowercase. 
	 */
	String getPackageName();

}
