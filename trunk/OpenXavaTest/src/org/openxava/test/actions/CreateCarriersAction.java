package org.openxava.test.actions;

import java.util.*;

import org.openxava.model.*;

/**
 * tmp maybe we remove this action, it is used in CarriersTest
 * @author Mª Carmen Gimeno Alabau
 */
public class CreateCarriersAction extends DeleteAllAction {

	public void execute() throws Exception {
		super.execute();
		Map warehouse = new HashMap();
		warehouse.put("zoneNumber", new Integer(1));
		warehouse.put("number", new Integer(1));
		Map carrier = new HashMap();
		carrier.put("number", new Integer(1));
		carrier.put("name", "UNO");
		carrier.put("warehouse", warehouse);
		MapFacade.create("Carrier", carrier);
		
		carrier.put("number", new Integer(2));
		carrier.put("name", "DOS");
		MapFacade.create("Carrier", carrier);
		
		carrier.put("number", new Integer(3));
		carrier.put("name", "TRES");
		MapFacade.create("Carrier", carrier);
		
		carrier.put("number", new Integer(4));
		carrier.put("name", "CUATRO");
		MapFacade.create("Carrier", carrier);
		
		carrier.put("number", new Integer(5));
		carrier.put("name", "Cinco");
		warehouse.put("zoneNumber", new Integer(2));
		MapFacade.create("Carrier", carrier);

	}

}
