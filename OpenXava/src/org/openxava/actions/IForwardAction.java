package org.openxava.actions;

/**
 * Permite hacer un <i>forward</i> a un jsp o servlet de la
 * misma aplicaci�n.
 * 
 * @author Javier Paniza
 */
public interface IForwardAction extends IAction {
	
	String getForwardURI();
	boolean inNewWindow();

}
