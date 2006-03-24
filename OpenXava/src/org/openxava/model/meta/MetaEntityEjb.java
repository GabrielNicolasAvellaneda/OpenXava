package org.openxava.model.meta;

import java.lang.reflect.*;
import java.util.*;

import javax.ejb.*;

import org.openxava.mapping.*;
import org.openxava.util.*;

/**
 * 
 * @author Javier Paniza
 */

public class MetaEntityEjb extends MetaEntity implements IMetaEjb {
	
	private MetaEjbImpl impl = new MetaEjbImpl(this);	
	private java.lang.String beanClassName;
	private Collection keyFields;
	
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
	public boolean isPrimaryKeyClassAvailable() { 
		return impl.isPrimaryKeyClassAvailable();
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
	
	public Collection getKeyFields() throws XavaException {
		if (keyFields == null) {
			keyFields = new ArrayList();	
			if (isEjbGenerated()) {
				keyFields.addAll(getAllKeyPropertiesNames());
			}
			else {		
				Field[] fields = getPrimaryKeyClass().getFields();				
				for (int i = 0; i < fields.length; i++) {
					keyFields.add(fields[i].getName());
				}
			}
		}		
		return keyFields;
	}
	
	public Object obtainPrimaryKeyFromAllValues(Map values)
		throws XavaException {		
		return impl.obtainPrimaryKeyFromKey(values);
	}

	public Object obtainPrimaryKeyFromKey(Map keyValues)
		throws XavaException {
			return impl.obtainPrimaryKeyFromKey(keyValues);
	}
	
	public Object obtainPrimaryKeyFromKeyWithoutConversors(Map keyValues)
		throws XavaException {
			return impl.obtainPrimaryKeyAFromKeyWithoutConversors(keyValues);
	}
	
	public Map obtainMapFromPrimaryKey(Object primaryKey) throws XavaException {
		return impl.obtainMapFromPrimaryKey(primaryKey);
	}		
	
	public ModelMapping getMapping() throws XavaException {
		return getMetaComponent().getEntityMapping();
	}

	public String getClassName() throws XavaException {		
		return getRemote();
	}
	
	public String getBeanClassName() throws XavaException {
		if (Is.emptyString(beanClassName)) {
			beanClassName = getMetaComponent().getPackageName() + "." + getName();
		}
		return beanClassName;
	}
	
	public void setBeanClassName(java.lang.String newClase) {
		beanClassName = newClase;
	}

	public Class getBeanClass() throws XavaException {
		try {
			return Class.forName(getBeanClassName());
		} 
		catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			throw new XavaException("no_class_for_model", getBeanClass(), getName());
		}
	}
	
	
}

