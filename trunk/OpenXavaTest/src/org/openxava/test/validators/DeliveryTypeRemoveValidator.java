package org.openxava.test.validators;

import org.openxava.test.model.*;
import org.openxava.util.*;
import org.openxava.validators.*;

/**
 * @author Javier Paniza
 */
public class DeliveryTypeRemoveValidator implements IRemoveValidator {
	
	private IDeliveryType deliveryType;

	public void setEntity(Object entity) throws Exception {
		this.deliveryType = (IDeliveryType) entity; 		
	}

	public void validate(Messages errors) throws Exception {
		if 	(!deliveryType.getDeliveries().isEmpty()) {
			errors.add("not_remove_delivery_type_if_in_deliveries");	
		}
	}

}
