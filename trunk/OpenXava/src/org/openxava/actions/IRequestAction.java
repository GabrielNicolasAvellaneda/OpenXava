package org.openxava.actions;

import javax.servlet.http.*;

/**
 * Acción que recibo un request de servlet. <p>
 * 
 * Con esta acción podemos acceder directamente a los
 * recursos de una aplicación web (mediante el request), pero
 * está ligada a la tecnología de implementación (servlets),
 * y por ende es mejor evitarla si tenemos otro tipo de
 * acción, y pensamos en la portabilidad a otras tecnologías.<p>
 * 
 * Pero es necesaria para algunas cosas. A medida que se use
 * este tipo de acción para tareas especificas, se puede
 * refactorizar y crear acciones más específicar y abstractas
 * que no estén ligadas a servlets.<br>
 *   
 * @author Javier Paniza
 */

public interface IRequestAction {
	
	void setRequest(HttpServletRequest request);

}
