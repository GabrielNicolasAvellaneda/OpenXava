package org.openxava.actions;

import javax.servlet.http.*;

/**
 * Action that receive a servlet request. <p>
 * 
 * With this action we can access directily to the
 * web application resources (by means request), but
 * its link to implementation tecnology (servlets),
 * hence it's better to elude it if we have another
 * way and we are thinking in migrate to another tecnology. <p>
 * 
 * But it's needed form some issues. As this action type is
 * used for specific task, it's possible refactoring and create
 * more specific (in functional terms) and abstracts (in tecnologic terms)
 * actions that it's not link to servlets tecnology.<br>
 *   
 * @author Javier Paniza
 */

public interface IRequestAction {
	
	void setRequest(HttpServletRequest request);

}
