package org.openxava.model.meta;


import java.util.*;

import javax.ejb.*;

import org.openxava.component.*;
import org.openxava.mapping.*;
import org.openxava.util.*;

/**
 * 
 * @author Javier Paniza
 */

public class MetaAggregateEjb extends MetaAggregate implements IMetaEjb {
	
	private MetaEjbImpl impl = new MetaEjbImpl(this);
	
	public Class getRemoteClass() throws XavaException {
		return impl.getRemoteClass();
	}
	public java.lang.String getHome() throws XavaException {
		return impl.getHome();
	}
	
	public void setHome(java.lang.String newHome) {
		impl.setHome(newHome);
	}
	
	public java.lang.String getJndi() throws XavaException {
		return impl.getJndi();
	}
	
	public void setJndi(java.lang.String newJndi) {
		impl.setJndi(newJndi);
	}
	
	public java.lang.String getPrimaryKey() throws XavaException {
		return impl.getPrimaryKey();
	}
	
	public void setPrimaryKey(java.lang.String newPrimaryKey) {
		impl.setPrimaryKey(newPrimaryKey);
	}
	
	public java.lang.String getRemote() throws XavaException {
		return impl.getRemote();
	}	
	
	public void setRemote(java.lang.String newRemote) {
		impl.setRemote(newRemote);
	}
				
	public Class getHomeClass() throws XavaException {
		return impl.getHomeClass();
	}
	
	public Class getPrimaryKeyClass() throws XavaException {
		return impl.getPrimaryKeyClass();
	}
	
	public EJBHome obtainHome() throws XavaException {
		return impl.obtainHome();
	}
	
	public ModelMapping getMapping() throws XavaException {
		return getMetaComponent().getAggregateMapping(getName());
	}
		
	public void setMetaComponent(MetaComponent metaComponent) {
		super.setMetaComponent(metaComponent);
	}
	
	public Object obtainPrimaryKeyFromAllValues(Map values) throws XavaException {
		return impl.obtainPrimaryKeyFromAllValues(values);
	}
	public Object obtainPrimaryKeyFromKey(Map keyValues) throws XavaException {		
		return impl.obtainPrimaryKeyFromKey(keyValues);
	}
	
	public Object obtainPrimaryKeyFromKeyWithoutConversors(Map keyValues) throws XavaException {		
		return impl.obtainPrimaryKeyAFromKeyWithoutConversors(keyValues);
	}
	
	public Map obtainMapFromPrimaryKey(Object primaryKey) throws XavaException {
		return impl.obtainMapFromPrimaryKey(primaryKey);
	}
	
	public String getClassName() throws XavaException {		
		return getRemote();
	}
	public Class getBeanClass() throws XavaException {
		throw new UnsupportedOperationException ("Still not supported");
		
	}
	
}

