package org.openxava.actions;


/**
 * @author Javier Paniza
 */

public interface ICustomViewAction extends IAction {		
			
	final static String DEFAULT_VIEW = "__DEFAULT_VIEW__";
	final static String SAME_VIEW = null;
		
	/**
	 * The id of a view make directly by developer (not OpenXava view). <p>
	 * 
	 * In web versi�n is the name of jsp page (without .jsp extension).
	 * This is for inserting jsp (o swing) hand made view in our OpenXava application.
	 */
	String getCustomView() throws Exception;	

}
