package org.openxava.tab.impl;

import java.rmi.*;
import java.util.*;

public interface IEntityTabDataProvider {
	
	DataChunk nextChunk(ITabProvider tabProvider, String nombreModelo, List nombresPropiedades, Collection calculadoresTab, Map indicesClave, Collection conversoresTab) throws RemoteException;
	int getResultSize(ITabProvider tabProvider) throws RemoteException;
		
}
