package org.openxava.actions;

/**
 * @author Javier Paniza
 */

public interface IRemoteAction extends IAction {
	
	void executeBefore() throws Exception;
	
	void executeAfter() throws Exception;
	
	boolean isExecuteOnServer();
	
	/**
	 * Paquete, normalmente el nombre del proyecto en minusculas. 
	 */
	String getPackageName();

}
