package org.openxava.test.actions;

import org.openxava.test.model.*;
import org.openxava.actions.*;


/**
 * @author Mª Carmen Gimeno 
 */

public class StateHibernateSearchAction extends HibernateBaseAction {
	
	public void executeHibernate() throws Exception {
		//Query query = session.createQuery("select f from Family as f where f.oid=:oid" );	
		//query.setString("oid",getView().getValueString("oid"));
		//Family f =(Family) query.uniqueResult();
		State s = (State) getSession().get(State.class,getView().getValueString("id"));
		if (s==null) {
		  	addError("object_not_found");
		}
		else {
			getView().setValue("id",s.getId());
			getView().setValue("name",s.getName());
			getView().setEditable(true);	
			getView().setKeyEditable(false);
		}
						
	}
				
}
