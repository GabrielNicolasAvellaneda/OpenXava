package org.openxava.model.impl;


/**
 * This is a Home interface for the Session Bean
 */
public interface MapFacadeHome extends javax.ejb.EJBHome {

	org.openxava.model.impl.MapFacadeRemote create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
