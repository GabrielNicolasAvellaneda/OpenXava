package org.openxava.actions;

/**
 * It allows to do a <i>forward</i> to a jsp or servlet in the
 * same application.
 * 
 * @author Javier Paniza
 */

public interface IForwardAction extends IAction {
	
	String getForwardURI();
	boolean inNewWindow();

}
