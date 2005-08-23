package org.openxava.test.calculators;

import java.rmi.*;

import org.openxava.calculators.*;
import org.openxava.test.model.*;

/**
 * @author Javier Paniza
 */

public class TranslateCarrierNameCalculator implements IEntityCalculator {
	
	private ICarrier carrier;	

	public Object calculate() throws Exception {		
		String name = carrier.getName();
		String translated = name;
		if ("UNO".equals(name)) translated = "ONE";
		else if ("DOS".equals(name)) translated ="TWO";
		else if ("TRES".equals(name)) translated ="THREE";
		else if ("CUATRO".equals(name)) translated ="FOUR";
		else if ("CINCO".equals(name)) translated ="FIVE";
		else if ("ONE".equals(name)) translated = "UNO";
		else if ("TWO".equals(name)) translated ="DOS";
		else if ("THREE".equals(name)) translated ="TRES";
		else if ("FOUR".equals(name)) translated ="CUATRO";
		else if ("FIVE".equals(name)) translated ="CINCO";
		carrier.setName(translated);
		return null;
	}

	public void setEntity(Object entity) throws RemoteException {
		this.carrier = (ICarrier) entity;		
	}

}
