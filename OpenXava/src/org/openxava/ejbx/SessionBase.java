package org.openxava.ejbx;

import java.rmi.*;

import javax.ejb.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Base class for help in the creation of a <i>SessionBean</i>. <p>
 *
 * @author  Javier Paniza
 */ 

public class SessionBase extends EJBBase implements SessionBean {

  private SessionContext context;
  private static Log log = LogFactory.getLog(SessionBase.class);

  public void ejbActivate() throws RemoteException {
  }
  public void ejbPassivate() throws RemoteException {
  }
  public void ejbRemove() throws RemoteException {
  }
  public SessionContext getSessionContext() {
  	return context;
  }
  public void setSessionContext(SessionContext context) throws RemoteException {
  	this.context = context;
  	super.setEJBContext(context);
  }
  
}
