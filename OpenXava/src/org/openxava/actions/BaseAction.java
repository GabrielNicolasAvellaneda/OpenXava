package org.openxava.actions;

import java.util.*;

import javax.servlet.http.*;

import org.openxava.controller.*;
import org.openxava.util.*;



/**
 * @author Javier Paniza
 */

abstract public class BaseAction implements IAction, IRequestAction, java.io.Serializable {
	
	private Messages errores;
	private Messages mensajes;
	private Environment entorno;
	private transient HttpServletRequest request;
	
	public Messages getErrors() {
		return errores;
	}

	public void setErrors(Messages errores) {
		this.errores = errores;
	}
	
	public Messages getMessages() {
		return mensajes;
	}

	public void setMessages(Messages mensajes) {
		this.mensajes = mensajes;
	}
		
	protected void addErrors(Messages errores) {
		this.errores.add(errores);
	}
	
	protected void addError(String idMensaje) {
		errores.add(idMensaje);
	}
	
	protected void addError(String idMensaje, Object [] ids) {
		errores.add(idMensaje, ids);
	}
	
	protected void addError(String idMensaje, Object id0) {
		errores.add(idMensaje, new Object [] {id0});
	}
	
	protected void addError(String idMensaje, Object id0, Object id1) {
		errores.add(idMensaje, new Object [] {id0, id1});
	}
	
	protected void addError(String idMensaje, Object id0, Object id1, Object id2) {
		errores.add(idMensaje, new Object [] {id0, id1, id2});
	}
	
	protected void addError(String idMensaje, Object id0, Object id1, Object id2, Object id3) {
		errores.add(idMensaje, new Object [] {id0, id1, id2, id3});
	}
	
	protected void addError(String idMensaje, Object id0, Object id1, Object id2, Object id3, Object id4) {
		errores.add(idMensaje, new Object [] {id0, id1, id2, id3, id4});
	}
	
	protected void addError(String idMensaje, Object id0, Object id1, Object id2, Object id3, Object id4, Object id5) {
		errores.add(idMensaje, new Object [] {id0, id1, id2, id3, id4, id5});
	}
	
	protected void addMessages(Messages mensajes) {
		this.mensajes.add(mensajes);
	}
	
	protected void addMessage(String idMensaje) {
		mensajes.add(idMensaje);
	}
	
	protected void addMessage(String idMensaje, Object [] ids) {
		mensajes.add(idMensaje, ids);
	}
	
	protected void addMessage(String idMensaje, Object id0) {
		mensajes.add(idMensaje, new Object [] {id0});
	}
	
	protected void addMessage(String idMensaje, Object id0, Object id1) {
		mensajes.add(idMensaje, new Object [] {id0, id1});
	}
	
	protected void addMessage(String idMensaje, Object id0, Object id1, Object id2) {
		mensajes.add(idMensaje, new Object [] {id0, id1, id2});
	}
	
	protected void addMessage(String idMensaje, Object id0, Object id1, Object id2, Object id3) {
		mensajes.add(idMensaje, new Object [] {id0, id1, id2, id3});
	}
	
	protected void addMessage(String idMensaje, Object id0, Object id1, Object id2, Object id3, Object id4) {
		mensajes.add(idMensaje, new Object [] {id0, id1, id2, id3, id4});
	}
	
	protected void addMessage(String idMensaje, Object id0, Object id1, Object id2, Object id3, Object id4, Object id5) {
		mensajes.add(idMensaje, new Object [] {id0, id1, id2, id3, id4, id5});
	}
	
		
	public void executeBefore() throws Exception {	
	}
	
	public void executeAfter() throws Exception {	
	}
	
	public void setEnvironment(Environment entorno) {
		this.entorno = entorno;
	}
	public Environment getEnvironment() {
		return entorno;
	}
	/**
	 * Reset de cache of all descriptions-list and 
	 * others uses of descriptionsEditors.	 
	 */
	protected void resetDescriptionsCache() {
		Enumeration e = request.getSession().getAttributeNames();
		while (e.hasMoreElements()) {
			String name = (String) e.nextElement();
			if (name.endsWith(".descriptionsCalculator")) {
				request.getSession().removeAttribute(name);
			}
		}		
	}
	
	public void setRequest(HttpServletRequest request) {
		// the request always private. If one descendent
		// action need request, then that implements IRequestAction
		// For no promote the easy use of request, and hence
		// the creation of no portable (out of http) actions.
		this.request= request;
	}
	
}
