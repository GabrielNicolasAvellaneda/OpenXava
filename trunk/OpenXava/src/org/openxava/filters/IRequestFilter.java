package org.openxava.filters;

import javax.servlet.http.*;



/**
 * Filtro que recibe una request http antes de filtrar. <p>
 * 
 * Evidentemente estos filtros no funcionaran en una
 * versión swing, pero ofrecen una flexibilidad necesaria.<br>
 *  
 * @author Javier Paniza
 */

public interface IRequestFilter extends IFilter {
				
	public void setRequest(HttpServletRequest request); 

}
