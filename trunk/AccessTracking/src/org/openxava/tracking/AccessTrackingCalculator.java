package org.openxava.tracking;

import java.rmi.*;
import java.text.*;
import java.util.Date;

import org.hibernate.*;
import org.openxava.calculators.*;
import org.openxava.model.*;
import org.openxava.model.impl.*;
import org.openxava.model.meta.*;
import org.openxava.tracking.model.*;
import org.openxava.util.*;
import org.openxava.validators.*;

/**
 * tmp
 * 
 * @author Javier Paniza
 */
public class AccessTrackingCalculator implements IEntityCalculator, IRemoveValidator {
	
	private IModel entity;
	private String accessType; 	 

	public Object calculate() throws Exception {
		Transaction tx = null;
		Session session = null;
		try {
			session = HibernatePersistenceProvider.getSessionFactory().openSession();
			//tx = session.beginTransaction();				
			Access access = newAccess();
			session.save(access);
			session.flush();
			//tx.commit();
			session.close();
		}							
		catch (Exception ex) {
			//if (tx != null)	tx.rollback();
			if (session != null) session.close();
			// tmp throw ex; // tmp Puede que no
		}								
		return null;
	}
	
	private Access newAccess() throws Exception {
		MetaModel metaModel = entity.getMetaModel();
		Access access = new Access();
		access.setModel(metaModel.getName());
		access.setTable(metaModel.getMapping().getTable());
		Date date = new Date();
		access.setDate(date);		
		access.setTime(DateFormat.getTimeInstance(DateFormat.SHORT).format(date));
		int type = "CRUD".indexOf(getAccessType().substring(0, 1).toUpperCase()) + 1;
		access.setType(type);
		access.setAuthorized(true);
		String key = MapFacade.getKeyValues(metaModel.getName(), entity).toString();
		access.setRecordId(key);
		return access;
	}	

	public void setEntity(Object entity) throws RemoteException {
		this.entity = (IModel) entity;
	}	
	
	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public void validate(Messages errors) throws Exception {
		setAccessType("Delete");		
		calculate();
	}

}
