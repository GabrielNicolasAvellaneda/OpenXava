package org.openxava.test.ejb;

import java.rmi.*;

/**
 * @author Javier Paniza
 */
public interface IWithCity {
	
	String getCity() throws RemoteException;

}
