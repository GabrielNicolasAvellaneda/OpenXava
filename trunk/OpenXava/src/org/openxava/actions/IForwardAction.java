package org.openxava.actions;

/**
 * Permite hacer un <i>forward</i> a un jsp o servlet de la
 * misma aplicación.
 * 
 * @author Javier Paniza
 */
public interface IForwardAction extends IAction {
	
	String getForwardURI();
	boolean inNewWindow();

}
