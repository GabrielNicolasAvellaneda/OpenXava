package org.openxava.test.ejb;

import java.rmi.*;

/**
 * @author Javier Paniza
 */
public interface IWithName {
	
	String getName() throws RemoteException;

}
