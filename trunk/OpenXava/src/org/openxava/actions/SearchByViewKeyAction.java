package org.openxava.actions;

import java.util.Iterator;
import java.util.Map;

import javax.ejb.ObjectNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.component.MetaComponent;
import org.openxava.model.MapFacade;
import org.openxava.model.meta.MetaEntity;
import org.openxava.model.meta.MetaProperty;
import org.openxava.util.*;
import org.openxava.web.WebEditors;

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
 * @author Ana Andr�s
 */

public class SearchByViewKeyAction extends ViewBaseAction {
	
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(SearchByViewKeyAction.class);
	private HttpServletRequest request;

	public void execute() throws Exception {				
		try {									
			Map keys = getKeyValuesFromView();
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
			
			if (XSystem.isJava5OrBetter()) {
				try {
					// For inheritance support
					// A new find is not so slow, possibly because of persistence engine cache
					Object entity = MapFacade.findEntity(getModelName(), values); 
					String modelName = Classes.getSimpleName(entity.getClass());
					if (!modelName.equals(getModelName())) {
						getView().setModelName(modelName);				
						values = MapFacade.getValues(modelName, entity, getMemberNames());
					}
				}
				catch (ObjectNotFoundException ex) { // For some special case, as null reference keys				
				}
			}
						
			getView().setEditable(true);	
			getView().setKeyEditable(false);			
			setValuesToView(values); 		
		}
		catch (ObjectNotFoundException ex) {
			String searchPropertiesAndValues = getSearchPropertiesAndValues();
			getView().clear();
			addError("object_not_found", getModelName(), searchPropertiesAndValues);			
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

	private String getSearchPropertiesAndValues() throws Exception{
		StringBuffer sb = new StringBuffer("");
		Map mapToSearch = Maps.treeToPlain(
				Maps.isEmptyOrZero(getKeyValuesFromView()) ? getValuesFromView() : getKeyValuesFromView());

		MetaEntity metaEntity = MetaComponent.get(getModelName()).getMetaEntity();
		String separator = "";		
		Iterator it = mapToSearch.entrySet().iterator();		
		while (it.hasNext()){
			Map.Entry entry = (Map.Entry) it.next();
			Object property = entry.getKey();
			if (!Is.empty(entry.getValue())) {
				MetaProperty mp = metaEntity.getMetaProperty(property.toString());
				String propertyName = mp.getQualifiedLabel(request);
				String value = WebEditors.format(request, mp, entry.getValue(), getErrors(), getView().getViewName());
				separator = sb.length() == 0 ? "" : ", ";
				sb.append(separator + propertyName + ":" + value);
			}
		}
		return sb.toString().trim();
	}
	
	public void setRequest(HttpServletRequest request) {	
		super.setRequest(request);
		this.request = request;
	}
		
}
