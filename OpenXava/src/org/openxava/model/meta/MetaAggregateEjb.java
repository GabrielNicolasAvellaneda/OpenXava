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
	/**
	 * 
	 * @return java.lang.String
	 * @throws XavaException
	 */
	public java.lang.String getHome() throws XavaException {
		return impl.getHome();
	}
	
	/**
	 * 
	 * @param newHome java.lang.String
	 */
	public void setHome(java.lang.String newHome) {
		impl.setHome(newHome);
	}
	
	
	/**
	 * 
	 * @return java.lang.String
	 * @throws XavaException
	 */
	public java.lang.String getJndi() throws XavaException {
		return impl.getJndi();
	}
	
	/**
	 * 
	 * @param newJndi java.lang.String
	 */
	public void setJndi(java.lang.String newJndi) {
		impl.setJndi(newJndi);
	}
	
	
	/**
	 * 
	 * @return java.lang.String
	 * @throws XavaException
	 */
	public java.lang.String getPrimaryKey() throws XavaException {
		return impl.getPrimaryKey();
	}
	
	/**
	 * 
	 * @param newPrimaryKey java.lang.String
	 */
	public void setPrimaryKey(java.lang.String newPrimaryKey) {
		impl.setPrimaryKey(newPrimaryKey);
	}
	
	
	/**
	 * 
	 * @return java.lang.String
	 * @throws XavaException
	 */
	public java.lang.String getRemote() throws XavaException {
		return impl.getRemote();
	}	
	
	
	
	/**
	 * 
	 * @param newRemote java.lang.String
	 */
	public void setRemote(java.lang.String newRemote) {
		impl.setRemote(newRemote);
	}
				
	public Class getHomeClass() throws XavaException {
		return impl.getHomeClass();
	}
	
	public Class getPrimaryKeyClass() throws XavaException {
		return impl.getPrimaryKeyClass();
	}
	
	/**
	 * Para usarse desde dentro de un EJB. <p>
	 *
	 * No se debería usar desde el cliente ya que lanza
	 * una <tt>EJBException</tt> si falla.<br>
	 *
	 * @exception EJBException Si hay algún problema
	 * @return Home moldado
	 * @throws XavaException
	 */
	public EJBHome obtainHome() throws XavaException {
		return impl.obtainHome();
	}
	
	public java.lang.Class getPropertiesClass() throws XavaException {
		return getRemoteClass();
	}
	/**
	 * @see org.openxava.model.meta.MetaModel#getMapping()
	 */
	public ModelMapping getMapping() throws XavaException {
		return getMetaComponent().getAggregateMapping(getName());
	}
		
	public void setMetaComponent(MetaComponent componente) {
		super.setMetaComponent(componente);
	}
	
	public Object obtainPrimaryKeyFromAllValues(Map valores) throws XavaException {
		return impl.obtainPrimaryKeyFromAllValues(valores);
	}
	public Object obtainPrimaryKeyFromKey(Map valoresClave) throws XavaException {		
		return impl.obtainPrimaryKeyFromKey(valoresClave);
	}
	
	public Object obtainPrimaryKeyFromKeyWithoutConversors(Map valoresClave) throws XavaException {		
		return impl.obtainPrimaryKeyAFromKeyWithoutConversors(valoresClave);
	}
	
	public String getClassName() throws XavaException {		
		return getRemote();
	}
	
}

