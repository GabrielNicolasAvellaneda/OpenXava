package org.openxava.util;

import java.rmi.*;
import java.util.*;

import javax.rmi.*;

import org.openxava.actions.*;
import org.openxava.calculators.*;
import org.openxava.component.*;
import org.openxava.ejbx.*;
import org.openxava.util.impl.*;


/**
 * Sirve para ejecutar facilmente codigo en el servidor. <p>
 * 
 * De momento calculadores, pero se puede ampliar para otro
 * tipo de comandos, validaciones, etc.
 * 
 * @author Javier Paniza
 */
public class Server {
	
	private static Map remotes;

	public static Object calculate(ICalculator calculador, String paquete) throws Exception {
		try {
			return getRemote(paquete).calculate(calculador);
		}
		catch (RemoteException ex) {
			anularRemote(paquete);
			return getRemote(paquete).calculate(calculador);
		}		
	}
	
	public static Object calculateWithoutTransaction(ICalculator calculador, String paquete) throws Exception {
		try {
			return getRemote(paquete).calculateWithoutTransaction(calculador);
		}
		catch (RemoteException ex) {
			anularRemote(paquete);
			return getRemote(paquete).calculateWithoutTransaction(calculador);
		}		
	}
	
	public static IRemoteAction execute(IRemoteAction accion, String paquete) throws Exception {
		try {
			return getRemote(paquete).execute(accion);
		}
		catch (RemoteException ex) {
			anularRemote(paquete);
			return getRemote(paquete).execute(accion);
		}
		
	}
	
	
	
	private static ServerRemote getRemote(String packageName) throws RemoteException {
		try {						
			packageName = Strings.change(packageName, ".", "/");
			ServerRemote remote = (ServerRemote) getRemotes().get(packageName);
			if (remote == null) {							
				Object ohome = null;
				try {					
					ohome = BeansContext.get().lookup("ejb/"+packageName+"/Server");
				}
				catch (Exception ex) {					
					packageName = MetaComponent.getQualifiedPackageForUnqualifiedPackage(packageName);
					ohome = BeansContext.get().lookup("ejb/"+packageName+"/Server"); 
				}				
				ServerHome home = (ServerHome) PortableRemoteObject.narrow(ohome, ServerHome.class);
				remote = home.create();
				getRemotes().put(packageName, remote);				
			}		
			return remote;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new RemoteException(XavaResources.getString("server_remote_exception"));
		}		
	}

	
	private static Map getRemotes() {
		if (remotes == null) {
			remotes = new HashMap();
		}
		return remotes;
	}
	
	private static void anularRemote(String paquete) {
		paquete = Strings.change(paquete, ".", "/");
		getRemotes().remove(paquete);			
	}	
		
}
