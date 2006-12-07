package org.openxava.ejbx;

import java.rmi.*;

import javax.ejb.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Base class for <i>EntityBeans</i>. <p>
 *
 * @author  Javier Paniza
 */

public class EntityBase extends EJBBase implements EntityBean {

  private EntityContext context;
  private static Log log = LogFactory.getLog(EntityBase.class);

  public void ejbActivate() throws RemoteException {
  }
  public void ejbLoad() throws RemoteException {
  }
  public void ejbPassivate() throws RemoteException {
  }
  public void ejbRemove() throws RemoteException, RemoveException {
  }
  public void ejbStore() throws RemoteException {
  }
  public EntityContext getEntityContext() {
  	return context;
  }
  public void setEntityContext(EntityContext context) {
		super.setEJBContext(context);
		this.context = context;
  }
  public void unsetEntityContext() {
		context = null;
		super.setEJBContext(null);
  }
  
}
