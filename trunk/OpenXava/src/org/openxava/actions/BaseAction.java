package org.openxava.actions;

import java.util.*;

import javax.servlet.http.*;

import org.openxava.controller.*;
import org.openxava.util.*;

/**
 * @author Javier Paniza
 */

abstract public class BaseAction implements IAction, IRequestAction, java.io.Serializable {
	
	private Messages errors;
	private Messages messages;
	private Environment environment;
	private transient HttpServletRequest request;
	
	public Messages getErrors() {
		return errors;
	}

	public void setErrors(Messages errors) {
		this.errors = errors;
	}
	
	public Messages getMessages() {
		return messages;
	}

	public void setMessages(Messages messages) {
		this.messages = messages;
	}
		
	protected void addErrors(Messages errors) {
		this.errors.add(errors);
	}
	
	protected void addError(String messageId) {
		errors.add(messageId);
	}
	
	protected void addError(String messageId, Object [] ids) {
		errors.add(messageId, ids);
	}
	
	protected void addError(String messageId, Object id0) {
		errors.add(messageId, new Object [] {id0});
	}
	
	protected void addError(String messageId, Object id0, Object id1) {
		errors.add(messageId, new Object [] {id0, id1});
	}
	
	protected void addError(String messageId, Object id0, Object id1, Object id2) {
		errors.add(messageId, new Object [] {id0, id1, id2});
	}
	
	protected void addError(String messageId, Object id0, Object id1, Object id2, Object id3) {
		errors.add(messageId, new Object [] {id0, id1, id2, id3});
	}
	
	protected void addError(String messageId, Object id0, Object id1, Object id2, Object id3, Object id4) {
		errors.add(messageId, new Object [] {id0, id1, id2, id3, id4});
	}
	
	protected void addError(String messageId, Object id0, Object id1, Object id2, Object id3, Object id4, Object id5) {
		errors.add(messageId, new Object [] {id0, id1, id2, id3, id4, id5});
	}
	
	protected void addMessages(Messages messages) {
		this.messages.add(messages);
	}
	
	protected void addMessage(String messajeId) {
		messages.add(messajeId);
	}
	
	protected void addMessage(String messageId, Object [] ids) {
		messages.add(messageId, ids);
	}
	
	protected void addMessage(String messageId, Object id0) {
		messages.add(messageId, new Object [] {id0});
	}
	
	protected void addMessage(String messageId, Object id0, Object id1) {
		messages.add(messageId, new Object [] {id0, id1});
	}
	
	protected void addMessage(String messageId, Object id0, Object id1, Object id2) {
		messages.add(messageId, new Object [] {id0, id1, id2});
	}
	
	protected void addMessage(String messageId, Object id0, Object id1, Object id2, Object id3) {
		messages.add(messageId, new Object [] {id0, id1, id2, id3});
	}
	
	protected void addMessage(String messageId, Object id0, Object id1, Object id2, Object id3, Object id4) {
		messages.add(messageId, new Object [] {id0, id1, id2, id3, id4});
	}
	
	protected void addMessage(String messageId, Object id0, Object id1, Object id2, Object id3, Object id4, Object id5) {
		messages.add(messageId, new Object [] {id0, id1, id2, id3, id4, id5});
	}
	
		
	public void executeBefore() throws Exception {	
	}
	
	public void executeAfter() throws Exception {	
	}
	
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
	public Environment getEnvironment() {
		return environment;
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
    
	/**
	 * The Locale of the current request. <p> 
	 */
    protected Locale getLocale() {
        return Locales.getCurrent();
    }
    
}
