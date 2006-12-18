package org.openxava.actions;

import java.util.Map;

import javax.ejb.ObjectNotFoundException;



import org.apache.commons.logging.*;
import org.openxava.model.MapFacade;
import org.openxava.util.Maps;

/**
 * Search using as key the data displayed in the view. <p>
 * 
 * First try to use the key value, if they are filled, 
 * otherwise uses the values of any properties filled for searching
 * the data, and return the first matched object.<p>
 * 
 * You can refine the behaviour of this action extending it and overwrite
 * its protected methods.<p>
 * 
 * @author Javier Paniza
 */

public class SearchByViewKeyAction extends ViewBaseAction {
	
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(SearchByViewKeyAction.class);
	

	public void execute() throws Exception {				
		try {						
			Map keys = getView().getKeyValues();
			Map values = null;
			if (Maps.isEmptyOrZero(keys)) { 
				try {
					values = MapFacade.getValuesByAnyProperty(getModelName(), getValuesFromView(), getMemberNames());
				}
				catch (ObjectNotFoundException ex) {
					// This is for the case of key with 0 as valid value
					values = MapFacade.getValues(getModelName(), keys, getMemberNames());
				}
			}
			else {				
				values = MapFacade.getValues(getModelName(), keys, getMemberNames());
			}
			getView().setEditable(true);	
			getView().setKeyEditable(false);			
			setValuesToView(values); 		
		}
		catch (ObjectNotFoundException ex) {
			getView().clear();
			addError("object_not_found", getModelName());			
		}						
		catch (Exception ex) {
			log.error(ex.getMessage(),ex);
			addError("system_error");			
		}						
	}
	
	/**
	 * Executed after searching is done, in order to assign the searched
	 * values to the view. <p>
	 * 
	 * @param values The values to assign to the view
	 * @throws Exception If some is wrong.
	 */
	protected void setValuesToView(Map values) throws Exception {
		getView().setValues(values);
	}

	/** 
	 * Names of the members to be retrieve from object model (at the end from database). <p>
	 * 
	 * By default, they are the members shown by the view.  
	 */
	protected Map getMemberNames() throws Exception{
		return getView().getMembersNamesWithHidden();
	}
	
	/**
	 * Values obtained from the view, used to do a search by any filled value. <p>
	 * 
	 * By default assumed all data currently displayed to the user.<br>
	 */
	protected Map getValuesFromView() throws Exception {
		return getView().getValues();
	}

	/**
	 * Key values obtained from the view, used to do a search by key. <p>
	 * 
	 * By default assumed key data in the view.<br> 
	 */
	protected Map getKeyValuesFromView() throws Exception {
		return getView().getKeyValues();
	}

}
