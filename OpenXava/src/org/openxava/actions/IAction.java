package org.openxava.actions;


import org.openxava.controller.*;
import org.openxava.util.*;


/**
 * Acción OpenXava.
 * 
 * @author Javier Paniza
 */

public interface IAction {
	
	void execute() throws Exception;
	
	void setErrors(Messages errores);
	Messages getErrors();
	
	void setMessages(Messages mensajes);
	Messages getMessages();
	
	void setEnvironment(Environment entorno);
	
	
}
