package org.openxava.ejbx;

import java.rmi.*;

import javax.ejb.*;



/**
  Clase base para facilitar la creación de <i>EntityBeans</i>. <p>

  @author  Javier Paniza
*/

/*
99.11.19  Versión inicial
00.02.21  Se pasa de negocio.util a negocio.ejbx
		  Se facilita el acceso al context mediante
		  un método getter, y se pasa de protegido a
		  privado.
00.08.02  Se añade la propiedad modified
01.05.02  Se deja de usar la histora de versiones a nivel de clase
*/


public class EntityBase extends EJBBase implements EntityBean {

  private EntityContext context;

  /** No hace nada. */
  public void ejbActivate() throws RemoteException {
  }
  /**
   * Al cargar el estado de la entidad. <p>
   */
  public void ejbLoad() throws RemoteException {
  }
  /** No hace nada. */
  public void ejbPassivate() throws RemoteException {
  }
  /** No hace nada. */
  public void ejbRemove() throws RemoteException, RemoveException {
  }
  /** No hace nada. */
  public void ejbStore() throws RemoteException {
  }
  /** Devuelve el <code>EntityContext</code> asociado al bean. */
  public EntityContext getEntityContext() {
	return context;
  }
  /** Establece el contexto, <i>callback</i> de EJB. */
  public void setEntityContext(EntityContext context) {
		super.setEJBContext(context);
		this.context = context;
  }
  /** Elimina el contexto, <i>callback</i> de EJB. */
  public void unsetEntityContext() {
		context = null;
		super.setEJBContext(null);
  }
}
