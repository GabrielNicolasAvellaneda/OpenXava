package org.openxava.test.actions;

import net.sf.hibernate.*;
import org.openxava.actions.*;
import org.openxava.test.model.*;


/**
 * @author Mª Carmen Gimeno
 */
public class StateHibernateDeleteAction extends HibernateBaseAction{
	
	public void executeHibernate() throws Exception {
		
		Query query = getSession().createQuery("select s from State as s where s.id=:id" );	
		query.setString("id",getView().getValueString("id"));
		State s =(State) query.uniqueResult();
		if (s==null) {
			addError("no_delete_not_exists");
		}
		else {
			getSession().delete(s);
		}
		getView().reset();
		getView().setKeyEditable(true);
		resetDescriptionsCache();
	}
	     	
}
