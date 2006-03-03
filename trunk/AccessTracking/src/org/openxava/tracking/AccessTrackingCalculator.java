package org.openxava.tracking;

import java.rmi.*;
import java.text.*;
import java.util.Date;

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
public class AccessTrackingCalculator implements IEntityCalculator {
	
	private static DateFormat timeFormat = new SimpleDateFormat("HH:mm");	
		
	private IModel entity;
	private String accessType;	

	public Object calculate() throws Exception {		
		Session session = null;
		try {
			Access access = newAccess();
			session = XHibernate.createSession();
			Transaction tx = session.beginTransaction(); 
			session.save(access);
			session.flush();
			try {
				tx.commit();
			}
			catch (Exception ex) {
				// Possibly inside of an EJB with CMT
			}			
			session.close();
		}							
		catch (Exception ex) {
			ex.printStackTrace();
			if (session != null) {
				try { session.close(); } catch (Exception ex2) {}
			}
			System.err.println(XavaResources.getString("tracking_warning", ex.getLocalizedMessage())); // tracking is not so critial to abort the user work.
		}								
		return null;
	}
	
	private Access newAccess() throws Exception {
		MetaModel metaModel = entity.getMetaModel();
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
		String key = MapFacade.getKeyValues(metaModel.getName(), entity).toString();
		access.setRecordId(key);
		return access;
	}	
	
	private String getApplicationName(MetaModel metaModel) throws Exception {
		return Strings.lastToken(metaModel.getMetaComponent().getPackageNameWithSlashWithoutModel(), "/");		
	}

	private String getUser() {
		String user = Users.getCurrent();
		return user==null?"nobody":user;
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

}
