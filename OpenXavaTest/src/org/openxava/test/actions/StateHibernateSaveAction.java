package org.openxava.test.actions;

import org.openxava.test.model.*;
import org.openxava.actions.*;


/**
 * @author Javier Paniza
 */
public class StateHibernateSaveAction extends HibernateBaseAction{
	
	public void executeHibernate() throws Exception {
		if (getView().isKeyEditable()) {
			// Create
			State s = new State();
			s.setId(getView().getValueString("id"));
			s.setName(getView().getValueString("name"));
			getSession().save(s);			
		}
		else {
			// Modify				
			//Query query = session.createQuery("select f from Family as f where f.oid=:oid" );	
			//query.setString("oid",getView().getValueString("oid"));
			//Family f =(Family) query.uniqueResult();
			State s = (State) getSession().get(State.class,getView().getValueString("id"));
			if (s==null) {
				addError("no_modify_no_exists");
			}
			else {
				s.setId(getView().getValueString("id"));
				s.setName(getView().getValueString("name"));
			}	
		}
		getView().reset();
		getView().setKeyEditable(true);
		resetDescriptionsCache();
	}
	     	
}
