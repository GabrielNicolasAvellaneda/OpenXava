package org.openxava.test.ejb.xejb;

import java.util.*;

import org.openxava.ejbx.*;
import org.openxava.test.ejb.*;
import org.openxava.validators.*;


/**
 * Bean implementation class for Enterprise Bean: Warehouse
 */
public class WarehouseBean extends EJBReplicableBase {
	
	public int zoneNumber;
	public int number;
	public String name;

	public void ejbActivate() throws java.rmi.RemoteException {		
	}
	
	public WarehouseKey ejbCreate(Map initialValues) throws javax.ejb.CreateException, ValidationException, java.rmi.RemoteException {		
		executeSets(initialValues);
		return null;
	}
	public void ejbPostCreate(Map initialValues)  {
	}
	
	
	public WarehouseKey ejbCreate(int zoneNumber, int number, String name) throws javax.ejb.CreateException, ValidationException{		
		this.zoneNumber = zoneNumber;
		this.number = number;
		this.name = name;
		return null;
	}	
	public void ejbPostCreate(int zoneNumber, int number, String name) throws javax.ejb.CreateException, ValidationException{
	}
	
	public void ejbLoad() throws java.rmi.RemoteException {		
	}
	
	public void ejbPassivate() throws java.rmi.RemoteException {
	}
	

	public void ejbRemove() throws javax.ejb.RemoveException, java.rmi.RemoteException {
	} 
	public void ejbStore() throws java.rmi.RemoteException {
	}
	
	public int getZoneNumber() {
		return zoneNumber;
	}

	public String getName() {
		return name;
	}

	public int getNumber() {
		return number;
	}

	public void setZoneNumber(int zoneNumber) {
		this.zoneNumber = zoneNumber;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumber(int number) {
		this.number = number;
	}

}
