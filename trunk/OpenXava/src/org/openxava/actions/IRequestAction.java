package org.openxava.actions;

import javax.servlet.http.*;

/**
 * Acci�n que recibo un request de servlet. <p>
 * 
 * Con esta acci�n podemos acceder directamente a los
 * recursos de una aplicaci�n web (mediante el request), pero
 * est� ligada a la tecnolog�a de implementaci�n (servlets),
 * y por ende es mejor evitarla si tenemos otro tipo de
 * acci�n, y pensamos en la portabilidad a otras tecnolog�as.<p>
 * 
 * Pero es necesaria para algunas cosas. A medida que se use
 * este tipo de acci�n para tareas especificas, se puede
 * refactorizar y crear acciones m�s espec�ficar y abstractas
 * que no est�n ligadas a servlets.<br>
 *   
 * @author Javier Paniza
 */

public interface IRequestAction {
	
	void setRequest(HttpServletRequest request);

}
