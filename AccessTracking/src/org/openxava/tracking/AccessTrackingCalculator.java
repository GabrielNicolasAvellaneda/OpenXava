package org.openxava.tracking;

import java.rmi.*;
import java.text.*;
import java.util.Date;

import org.apache.commons.logging.*;
import org.hibernate.*;
import org.openxava.calculators.*;
import org.openxava.hibernate.XHibernate;
import org.openxava.model.*;
import org.openxava.model.meta.*;
import org.openxava.tracking.model.*;
import org.openxava.util.*;

/**
 * Calculator to track the accesses to the object. <p>
 * 
 * You can assign this calculator to your component in 
 * <code>postcreate-calculator, postload-calculator,
 * postmodify-calculator</code> or <code>preremove-calculator</code>.<br>
 * You need to give some of this values: <code>Create, Read, 
 * Update</code> or <code>Delete</code> to
 * the property <code>accessType</code>.<br> 
 * 
 * 
 * @author Javier Paniza
 */
public class AccessTrackingCalculator implements IModelCalculator {
	
	private static Log log = LogFactory.getLog(AccessTrackingCalculator.class);
	private static DateFormat timeFormat = new SimpleDateFormat("HH:mm");
	private static Access lastAccess = null; 
			
	private Object model;
	private String accessType;	

	public Object calculate() throws Exception {
		Session session = null;
		try {
			Access access = newAccess();			
			if (areEqual(access, lastAccess)) return null; 			
			session = XHibernate.createSession();
			Transaction tx = beginTransaction(session); 
			session.save(access);
			session.flush();				
			if (tx != null) tx.commit();
			session.close();			
			lastAccess = access; 
		}							
		catch (Exception ex) {
			ex.printStackTrace();
			if (session != null) {
				try { session.close(); } catch (Exception ex2) {}
			}
			log.warn(XavaResources.getString("tracking_warning", ex.getLocalizedMessage())); // tracking is not so critical to abort the user work.
		}								
		return null;		
	}
	
	private Transaction beginTransaction(Session session) {		
		if (XHibernate.isCmt()) return null;
		try {
			return session.beginTransaction();
		}
		catch (Exception ex) {  
			// Maybe we are unexpectly in a CMT environment  
			return null;
		}		
	}

	private Access newAccess() throws Exception {
		MetaModel metaModel = getMetaModel();
		Access access = new Access();
		access.setApplication(getApplicationName(metaModel));
		access.setModel(metaModel.getName());
		access.setUser(getUser());
		access.setTable(metaModel.getMapping().getTable());
		Date date = new Date();
		access.setDate(date);		
		access.setTime(timeFormat.format(date));
		int type = "CRUD".indexOf(getAccessType().substring(0, 1).toUpperCase()) + 1;
		if (type == 0) {
			new XavaException("illegal_property_value", "accessType", "AccessTracking", "Create, Read, Update or Delete");
		}
		access.setType(type);
		access.setAuthorized(true);
		String key = MapFacade.getKeyValues(metaModel.getName(), model).toString();
		access.setRecordId(key);
		return access;
	}		
	
	private MetaModel getMetaModel() throws Exception {
		if (model instanceof IModel) return ((IModel) model).getMetaModel();
		return MetaModel.getForPOJO(model);
	}

	private String getApplicationName(MetaModel metaModel) throws Exception {
		return Strings.lastToken(metaModel.getMetaComponent().getPackageNameWithSlashWithoutModel(), "/");		
	}
	
	private boolean areEqual(Access a1, Access a2) {
		return toString(a1).equals(toString(a2));
	}
	
	private String toString(Access access) { 
		if (access == null) return "";
		StringBuffer r = new StringBuffer();
		r.append(access.getApplication());
		r.append("::");
		r.append(access.getModel());
		r.append("::");
		r.append(access.getUser());
		r.append("::");
		r.append(access.getTable());
		r.append("::");
		r.append(DateFormat.getDateInstance(DateFormat.SHORT).format(access.getDate()));
		r.append("::");
		r.append(access.getTime());
		r.append("::");
		r.append(access.getType());
		r.append("::");
		r.append(access.isAuthorized());
		r.append("::");
		r.append(access.getRecordId());
		return r.toString();
	}
	
	private String getUser() {
		String user = Users.getCurrent();
		return user==null?"nobody":user;
	}	
	
	public void setModel(Object model) throws RemoteException {
		this.model = model;
	}	
	
	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

}
