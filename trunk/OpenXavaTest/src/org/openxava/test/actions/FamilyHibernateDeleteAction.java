package org.openxava.test.actions;

import net.sf.hibernate.*;
import org.openxava.actions.*;
import org.openxava.test.model.*;


/**
 * @author Javier Paniza
 */
public class FamilyHibernateDeleteAction extends HibernateBaseAction{
	
	public void executeHibernate() throws Exception {
		
		Query query = getSession().createQuery("select f from Family as f where f.oid=:oid" );	
		query.setString("oid",getView().getValueString("oid"));
		Family f =(Family) query.uniqueResult();
		if (f==null) {
			addError("no_delete_not_exists");
		}
		else {
			getSession().delete(f);
		}
		getView().reset();
		getView().setKeyEditable(true);
		resetDescriptionsCache();
	}
	     	
}
