package org.openxava.test.actions;

import org.openxava.test.model.*;
import org.openxava.actions.*;


/**
 * @author Javier Paniza
 */

public class FamilyHibernateSearchAction extends HibernateBaseAction {
	
	public void executeHibernate() throws Exception {
		//Query query = session.createQuery("select f from Family as f where f.oid=:oid" );	
		//query.setString("oid",getView().getValueString("oid"));
		//Family f =(Family) query.uniqueResult();
		Family f = (Family) getSession().get(Family.class,getView().getValueString("oid"));
		if (f==null) {
		  	addError("object_not_found");
		}
		else {
			getView().setValue("number",new Integer (f.getNumber()));
			getView().setValue("description",f.getDescription());
			getView().setEditable(true);	
			getView().setKeyEditable(false);
		}
						
	}
				
}
