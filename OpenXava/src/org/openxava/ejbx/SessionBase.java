package org.openxava.ejbx;

import java.rmi.*;

import javax.ejb.*;

/**
  Clase base para facilitar la implementación de un <i>SessionBean</i>. <p>

  @version 1.0 (19/11/1999)
  @author  Javier Paniza
*/

/*
99.11.19  Versión inical
00.02.21  Se mueve de negocio.util a negocio.ejbx
		  Se hace pasa de protegido a privado context, y
		  se le añade un getter.
*/

public class SessionBase extends EJBBase implements SessionBean {

  private SessionContext context;

  /** No hace nada. */
  public void ejbActivate() throws RemoteException {
  }
  /** No hace nada. */
  public void ejbPassivate() throws RemoteException {
  }
  /** No hace nada. */
  public void ejbRemove() throws RemoteException {
  }
  /** Devuelve el <code>SessionContext</code> asociado al bean. */
  public SessionContext getSessionContext() {
	return context;
  }
  /** Establece el contexto del bean, <i>callback</i> de EJB. */
  public void setSessionContext(SessionContext context) throws RemoteException {
	this.context = context;
	super.setEJBContext(context);
  }
}
