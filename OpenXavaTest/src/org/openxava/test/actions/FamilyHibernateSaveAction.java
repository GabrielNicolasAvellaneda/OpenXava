package org.openxava.test.actions;

import org.openxava.test.model.*;
import org.openxava.actions.*;


/**
 * @author Javier Paniza
 */
public class FamilyHibernateSaveAction extends HibernateBaseAction{
	
	public void executeHibernate() throws Exception {
		if (getView().isKeyEditable()) {
			// Create
			Family f = new Family();
			f.setNumber(getView().getValueInt("number"));
			f.setDescription(getView().getValueString("description"));
			getSession().save(f);			
		}
		else {
			// Modify				
			//Query query = session.createQuery("select f from Family as f where f.oid=:oid" );	
			//query.setString("oid",getView().getValueString("oid"));
			//Family f =(Family) query.uniqueResult();
			Family f = (Family) getSession().get(Family.class,getView().getValueString("oid"));
			if (f==null) {
				addError("no_modify_no_exists");
			}
			else {
				f.setNumber(getView().getValueInt("number"));
				f.setDescription(getView().getValueString("description"));
			}	
		}
		getView().reset();
		getView().setKeyEditable(true);
		resetDescriptionsCache();
	}
	     	
}
